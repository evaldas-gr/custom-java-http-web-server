package org.webserver.http;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ChangeRequest {
    public static final int WRITE_OPS = SelectionKey.OP_WRITE;

    public SocketChannel socket;
    public int type;
    public int ops;

    public ChangeRequest(SocketChannel socket, int type, int ops) {
        this.socket = socket;
        this.type = type;
        this.ops = ops;
    }
}
