package com.mjvs.jgsp.helpers;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringExtensionsTests
{
    @Test
    public void IsEmptyOrWhitespace_StringIsNull_ReturnsTrue()
    {
        // Act
        boolean result = StringExtensions.isEmptyOrWhitespace(null);

        // Assert
        assertTrue(result);
    }

    @Test
    public void IsEmptyOrWhitespace_StringIsEmtpyOrWhitespace_ReturnsTrue()
    {
        // Arrange
        String str1 = "";
        String str2 = " ";
        String str3 = "\n";
        String str4 = "\r";

        // Act
        boolean result1 = StringExtensions.isEmptyOrWhitespace(str1);
        boolean result2 = StringExtensions.isEmptyOrWhitespace(str2);
        boolean result3 = StringExtensions.isEmptyOrWhitespace(str3);
        boolean result4 = StringExtensions.isEmptyOrWhitespace(str4);

        // Assert
        assertTrue(result1);
        assertTrue(result2);
        assertTrue(result3);
        assertTrue(result4);
    }

    @Test
    public void IsEmptyOrWhitespace_StringIsNotEmptyOrWhitespace_ReturnsFalse()
    {
        // Arrange
        String str1 = "a  b c";
        String str2 = " \n a\r ";

        // Act
        boolean result1 = StringExtensions.isEmptyOrWhitespace(str1);
        boolean result2 = StringExtensions.isEmptyOrWhitespace(str2);

        // Assert
        assertFalse(result1);
        assertFalse(result2);
    }
}
