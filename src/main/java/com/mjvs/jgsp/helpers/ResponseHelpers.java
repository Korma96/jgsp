package com.mjvs.jgsp.helpers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHelpers
{
    public static <T> ResponseEntity<T> getResponseData(T data)
    {
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
