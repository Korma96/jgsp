package com.mjvs.jgsp.unit_tests.helpers;

import com.mjvs.jgsp.helpers.ResponseHelpers;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ResponseHelpersTests
{
    @Test
    public void GetResponseData_DataIsNull_ReturnsResponseEntityWithNull()
    {
        // Act
        ResponseEntity response = ResponseHelpers.getResponseData(null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void GetResponseData_DataIsNotNull_ReturnsResponseEntityWithData()
    {
        // Arrange
        String data = "some data";

        // Act
        ResponseEntity response = ResponseHelpers.getResponseData(data);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(data.getClass(), response.getBody().getClass());
        assertEquals(data, response.getBody());
    }
}
