package com.mjvs.jgsp.helpers;

public class Messages
{
    public static String AlreadyContains(String object1, Long id1, String object2, Long id2)
    {
        return String.format("%s with id %d already contains %s with id %d", object1, id1, object2, id2);
    }

    public static String AlreadyExists(String object, Long id)
    {
        return String.format("%s with id %d already exists!", object, id);
    }

    public static String AlreadyExists(String object, String name)
    {
        return String.format("%s with name %s already exists!", object, name);
    }

    public static String CantBeEmptyOrWhitespace(String object)
    {
        return String.format("%s can`t be empty or whitespace!", object);
    }

    public static String CantBeNull(String object)
    {
        return String.format("%s can`t be null!", object);
    }

    public static String DoesNotExist(String object, Long id)
    {
        return String.format("%s with id %d does not exist.", object, id);
    }

    public static String DoesNotContain(String object1, Long id1, String object2, Long id2)
    {
        return String.format("%s with id %d doesn`t contains %s with id %d", object1, id1, object2, id2);
    }

    public static String ErrorSaving(String object, String message)
    {
        return String.format("Error saving %s, %s", object, message);
    }

    public static String ErrorSaving(String object, String name, String message)
    {
        return String.format("Error saving %s %s, %s", object, name, message);
    }

    public static String ErrorDeleting(String object, Long id, String message)
    {
        return String.format("Error deleting %s with id %d, %s", object, id, message);
    }

    public static String SuccessfullySaved(String object, String name)
    {
        return String.format("%s %s successfully saved!", object, name);
    }

    public static String SuccessfullySaved(String object)
    {
        return String.format("%s successfully saved!", object);
    }

    public static String SuccessfullyDeleted(String object, Long id)
    {
        return String.format("%s with id %d successfully deleted!", object, id);
    }
}
