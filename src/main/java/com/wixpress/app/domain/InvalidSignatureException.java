package com.wixpress.app.domain;

/**
 * Created by : doron
 * Since: 7/1/12
 */

public class InvalidSignatureException extends RuntimeException
{
    protected InvalidSignatureException(String message)
    {
        super(message);
    }
}
