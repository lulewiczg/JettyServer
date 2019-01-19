package com.gitub.lulewiczg.jetty.exceptions;

/**
 * Exception for server errors.
 *
 * @author Grzegurz
 */
public class JettyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public JettyException(Exception e) {
        super(e);
    }
}
