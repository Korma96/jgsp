package com.mjvs.jgsp.helpers;

public class StringExtensions
{
    public static boolean isEmptyOrWhitespace(String string)
    {
        if(string.trim().isEmpty())
        {
            return true;
        }
        return false;
    }
}
