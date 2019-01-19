package com.gitub.lulewiczg.jetty.exceptions;

import com.gitub.lulewiczg.jetty.resource.Message;

/**
 * Exception for missing messages.
 *
 * @author Grzegurz
 */
public class NoMessageException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoMessageException(Message m) {
        super(String.format("Message %s not found!", m));
    }
}
