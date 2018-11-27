package com.mjvs.jgsp.helpers;

import java.lang.reflect.Method;

public class ReflectionHelpers
{
    public static String InvokeGetNameMethod(Object obj)
    {
        try
        {
            Method method = obj.getClass().getMethod("getName");
            return method.invoke(obj).toString();
        }
        catch (Exception ex)
        {
            return null;
        }
    }
}
