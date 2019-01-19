package com.gitub.lulewiczg.jetty.resource;

import java.util.HashMap;
import java.util.Map;

import com.gitub.lulewiczg.jetty.exceptions.NoMessageException;

/**
 * Default hanlder for messages.
 *
 * @author Grzegurz
 */
public final class DefaultMessageHandler {
    private static final Map<Message, String> MESSAGES = new HashMap<>();
    static {
        MESSAGES.put(Message.SERVER_START, "Server started!");
        MESSAGES.put(Message.SERVER_STOP, "Server stopped!");
        MESSAGES.put(Message.SERVER_STARTING, "Server starting!");
        MESSAGES.put(Message.SERVER_STOPPING, "Server stopping!");
        MESSAGES.put(Message.ALREADY_RUNNING, "Server already running!");
        MESSAGES.put(Message.ALREADY_STOPPED, "Server already stopped!");
    }

    /**
     * Gets message.
     *
     * @param m
     *            message type
     * @return message
     */
    public String getMsg(Message m) {
        if (!MESSAGES.containsKey(m)) {
            throw new NoMessageException(m);
        }
        return MESSAGES.get(m);
    }

}
