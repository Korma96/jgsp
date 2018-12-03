package com.mjvs.jgsp.helpers;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionHelpers
{

    public static Long InvokeGetIdMethod(Object obj)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        Method method = obj.getClass().getMethod("getId");
        return (Long) method.invoke(obj);
    }

    public static boolean InvokeOptionalIsPresentMethod(Object obj)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        Method method = obj.getClass().getMethod("isPresent");
        return (boolean)method.invoke(obj);
    }

    public static Object InvokeOptionalGetMethod(Object obj)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        Method method = obj.getClass().getMethod("get");
        return method.invoke(obj);
    }

    public static void InvokeSetNameMethod(Object obj, String name)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        Method method = obj.getClass().getMethod("setName", String.class);
        method.invoke(obj, name);
    }

    public static <T> T GetInstanceOfT(Class<T> aClass)
            throws IllegalAccessException, InstantiationException
    {
        return aClass.newInstance();
    }

    public static String InvokeGetNameMethod(Object obj)
            throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        Method method = obj.getClass().getMethod("getName");
        return (String)method.invoke(obj);
    }

}
