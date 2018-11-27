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

    public static String ErrorSaving(String object, String message)
    {
        return String.format("Error saving %s message %s", object, message);
    }

    public static String ErrorDeleting(String object, Long id, String message)
    {
        return String.format("Error deleting %s with id %d%n message %s", object, id, message);
    }

    public static String ErrorDeleting(String object, String message)
    {
        return String.format("Error deleting %s message %s", object, message);
    }

    public static String SuccessfullySaved(String object)
    {
        return String.format("%s successfully saved!", object);
    }

    public static String SuccessfullyDeleted(String object, Long id)
    {
        return String.format("%s %d%n successfully deleted!", object, id);
    }

    public static String SuccessfullyDeleted(String object)
    {
        return String.format("%s successfully deleted!", object);
    }
}
