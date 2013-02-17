package com.wixpress.app.dao;

/**
 * @author Yoav
 * @since 2/16/13
 */
public class SampleAppDaoException extends RuntimeException {
    public SampleAppDaoException(String message, Exception cause, Object ... args) {
        super(String.format(message, args), cause);
    }
}
