package org.webserver.controllers;

import com.fasterxml.jackson.databind.DatabindException;
import org.webserver.http.exceptions.errors.client.StatusBadRequestException;
import org.webserver.http.exceptions.errors.server.StatusInternalServerException;
import org.webserver.logs.Logger;

import static org.webserver.config.AppConfig.objectMapper;

public class ControllerHelper {
    public static <A> A parsePayload(byte[] data, Class<A> clazz) {
        try {
            return objectMapper.readValue(data, clazz);
        } catch (DatabindException e) {
            throw new StatusBadRequestException("could not deserialize http request payload");
        } catch (Exception e) {
            Logger.log(e.getMessage());
            throw new StatusBadRequestException("invalid request");
        }
    }

    public static <B> byte[] serializePayload(B obj) {
        try {
            return objectMapper.writeValueAsBytes(obj);
        } catch (Exception e) {
            Logger.log(e.getMessage());
            throw new StatusInternalServerException("could not serialize response");
        }
    }
}
