package com.mjvs.jgsp.controller.exception;

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
