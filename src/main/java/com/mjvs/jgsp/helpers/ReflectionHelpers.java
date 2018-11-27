package com.mjvs.jgsp.helpers;

import java.lang.reflect.Method;

public class ReflectionHelpers
{
    public static Long InvokeGetIdMethod(Object obj)
    {
        try
        {
            Method method = obj.getClass().getMethod("getId");
            return (Long) method.invoke(obj);
        }
        catch (Exception ex)
        {
            return null;
        }
    }

    public static boolean InvokeOptionalIsPresentMethod(Object obj)
    {
        try
        {
            Method method = obj.getClass().getMethod("isPresent");
            return (boolean)method.invoke(obj);
        }
        catch (Exception ex)
        {
            return false;
        }
    }

    public static Object InvokeOptionalGetMethod(Object obj)
    {
        try
        {
            Method method = obj.getClass().getMethod("get");
            return method.invoke(obj);
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
