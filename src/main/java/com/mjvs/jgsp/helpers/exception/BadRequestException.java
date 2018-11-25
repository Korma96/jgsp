package com.mjvs.jgsp.helpers.exception;

public class BadRequestException extends Exception
{
    public BadRequestException()
    {

    }

    public BadRequestException(String message)
    {
        super(message);
    }
}
