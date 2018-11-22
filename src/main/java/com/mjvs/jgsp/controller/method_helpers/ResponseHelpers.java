package com.mjvs.jgsp.controller.method_helpers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHelpers
{
    public static ResponseEntity getResponseBasedOnResult(boolean result)
    {
        if(result)
        {
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    public static <T> ResponseEntity<T> getResponseData(T data)
    {
        return new ResponseEntity<>(data, HttpStatus.OK);
    }
}
