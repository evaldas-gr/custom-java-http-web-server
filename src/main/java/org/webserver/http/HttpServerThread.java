package org.webserver.http;

import org.webserver.http.logs.HttpLogger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ProtocolFamily;
import java.net.SocketException;
import java.net.StandardProtocolFamily;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.*;

public class HttpServerThread extends Thread {
    private volatile boolean running = false;
    private final ServerSocketChannel serverSocketChannel;
    private final Selector selector;
    private final HttpEventDispatcher eventDispatcher;
    private final List<ChangeRequest> changeRequests = new LinkedList<>();
    private final Map<SocketChannel, List<ByteBuffer>> pendingData = new HashMap<>();
    private final Integer readBufferSize;

    public HttpServerThread(String address, int port, ProtocolFamily protocolFamily, HttpEventDispatcher eventDispatcher) throws IOException {
        HttpLogger.log("initializing...", this.getClass());
        serverSocketChannel = ServerSocketChannel.open(protocolFamily);
        serverSocketChannel.bind(new InetSocketAddress(address, port), 0);
        serverSocketChannel.configureBlocking(false);

        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        this.eventDispatcher = eventDispatcher;
        this.readBufferSize = 64 * 1024;
        HttpLogger.log("server address: " + address, this.getClass());
        HttpLogger.log("server port: " + port, this.getClass());
        HttpLogger.log("initialized successfully", this.getClass());
    }

    public HttpServerThread(String address, int port, HttpEventDispatcher eventDispatcher, Integer readBufferSize) throws IOException {
        HttpLogger.log("initializing...");
        serverSocketChannel = ServerSocketChannel.open(StandardProtocolFamily.INET);
        serverSocketChannel.bind(new InetSocketAddress(address, port), 0);
        serverSocketChannel.configureBlocking(false);

        selector = Selector.open();
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        this.eventDispatcher = eventDispatcher;
        this.readBufferSize = readBufferSize;
        HttpLogger.log("initialized successfully");
    }

    private void accept(SelectionKey key) throws IOException {
        if (key.channel() == serverSocketChannel) {
            SocketChannel clientChannel = serverSocketChannel.accept();
            clientChannel.configureBlocking(false);
            clientChannel.register(selector, SelectionKey.OP_READ);
        }
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel clientChannel = (SocketChannel) key.channel();
        ByteBuffer bb = ByteBuffer.allocate(this.readBufferSize);
        ByteArrayOutputStream bbStream = new ByteArrayOutputStream();

        try {
            while (true) {
                bb.clear();
                int readStatus = clientChannel.read(bb);

                if (readStatus == -1) {
                    clientChannel.close();
                    key.cancel();
                    return;
                }

                bb.flip();
                byte[] chunk = new byte[bb.remaining()];
                bb.get(chunk);
                bbStream.write(chunk);

                if (readStatus == 0) {
                    break;
                }
            }
        } catch (SocketException e) {
            HttpLogger.error("socket exception", this, e);
            key.cancel();
            return;
        }

        this.eventDispatcher.dispatch(this, clientChannel, bbStream.toByteArray());
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        synchronized (this.pendingData) {
            List<ByteBuffer> queue = this.pendingData.get(socketChannel);

            try {
                while (!queue.isEmpty()) {
                    ByteBuffer bb = queue.getFirst();
                    socketChannel.write(bb);
                    if (bb.remaining() > 0) {
                        break;
                    }
                    queue.removeFirst();
                }
                if (queue.isEmpty()) {
                    socketChannel.close();
                    HttpLogger.log("written and connection closed");
                }
            } catch (Exception e) {
                socketChannel.close();
                HttpLogger.error("WRITE exception - closing", this, e);
            }
        }
    }

    public void send(SocketChannel socket, byte[] data) {
        synchronized (this.changeRequests) {
            this.changeRequests.add(new ChangeRequest(socket, ChangeRequest.WRITE_OPS, SelectionKey.OP_WRITE));

            synchronized (this.pendingData) {
                List<ByteBuffer> queue = this.pendingData.computeIfAbsent(socket, unit -> new ArrayList<>());
                queue.add(ByteBuffer.wrap(data));
            }
        }
        this.selector.wakeup();
    }

    @Override
    public void run() {
        HttpLogger.log("starting", this.getClass());
        var dispatcherThread = new Thread(eventDispatcher);
        dispatcherThread.setDaemon(true);
        dispatcherThread.start();
        this.running = true;

        try {
            while(this.running) {
                synchronized (this.changeRequests) {
                    for (ChangeRequest change : this.changeRequests) {
                        switch (change.type) {
                            case ChangeRequest.WRITE_OPS:
                                SelectionKey key = change.socket.keyFor(this.selector);
                                if (key != null && key.isValid()) {
                                    key.interestOps(change.ops);
                                }
                        }
                    }
                    this.changeRequests.clear();
                }
                // Blocking
                selector.select();

                Iterator<SelectionKey> selectionKeys = selector.selectedKeys().iterator();

                while (selectionKeys.hasNext()) {
                    SelectionKey selectedKey = selectionKeys.next();
                    selectionKeys.remove();

                    if (!selectedKey.isValid()){
                        continue;
                    }
                    if (selectedKey.isAcceptable()) {
                        this.accept(selectedKey);
                    } else if (selectedKey.isReadable()) {
                        this.read(selectedKey);
                    } else if (selectedKey.isWritable()) {
                        this.write(selectedKey);
                    }
                }
            }
            selector.close();
        } catch (IOException e) {
            HttpLogger.error("http server closing", this, e);
            e.printStackTrace();
        }
    }
}
