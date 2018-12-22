package com.mjvs.jgsp.helpers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ResponseHelpers
{
    public static <T> ResponseEntity<T> getResponseData(T data)
    {
        MultiValueMap<String, String> headers = new MultiValueMap<String, String>();
        ResponseEntity response = new ResponseEntity<>(data, HttpStatus.OK);
        response.
    }
}
