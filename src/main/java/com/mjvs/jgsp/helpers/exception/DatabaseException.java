package com.mjvs.jgsp.helpers.exception;

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
