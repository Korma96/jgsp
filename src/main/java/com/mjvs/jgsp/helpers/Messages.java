package com.mjvs.jgsp.helpers;

public class Messages
{
    public static String AlreadyContains(String object1, Long id1, String object2, Long id2)
    {
        return String.format("%s with id %d%n already contains %s with id %d%n", object1, id1, object2, id2);
    }

    public static String AlreadyExists(String object, Long id)
    {
        return String.format("%s with id %d%n already exists!", object, id);
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

    public static String DoesNotExists(String object, Long id)
    {
        return String.format("%s with id %d%n does not exist.", object, id);
    }

    public static String DoesNotContains(String object1, Long id1, String object2, Long id2)
    {
        return String.format("%s with id %d%n doesn`t contains %s with id %d%n", object1, id1, object2, id2);
    }
    public static String ErrorAdding(String object, String name, String message)
    {
        return String.format("Error adding %s with name %s message %s", object, name, message);
    }

    public static String ErrorDeleting(String object, String name, String message)
    {
        return String.format("Error deleting %s with name %s message %s", object, name, message);
    }

    public static String SuccessfullyAdded(String object, String name)
    {
        return String.format("%s %s successfully added!", object, name);
    }

    public static String SuccessfullyDeleted(String object, String name)
    {
        return String.format("%s %s successfully deleted!", object, name);
    }
}
