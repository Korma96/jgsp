package com.mjvs.jgsp.controller.exception;

public class DatabaseException extends RuntimeException
{
    public DatabaseException()
    {

    }

    public DatabaseException(String message)
    {
        super(message);
    }
}
