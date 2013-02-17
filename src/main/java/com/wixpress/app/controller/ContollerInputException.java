package com.wixpress.app.controller;

/**
 * @author yoav
 * @since 2/17/13
 */
public class ContollerInputException extends RuntimeException {
    public ContollerInputException(String message, Exception cause, Object... args) {
        super(String.format(message, args), cause);
    }
}
