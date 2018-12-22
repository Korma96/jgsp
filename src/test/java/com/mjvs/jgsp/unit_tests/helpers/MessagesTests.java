package com.mjvs.jgsp.unit_tests.helpers;

import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.StringConstants;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessagesTests
{
    @Test
    public void AlreadyContainsTest()
    {
        // Arrange
        String message = "Line with id 1 already contains Stop with id 2";

        // Act
        String result = Messages.AlreadyContains(StringConstants.Line, 1L, StringConstants.Stop, 2L);

        // Assert
        assertEquals(message, result);
    }

    @Test
    public void AlreadyExistsTest()
    {
        // Arrange
        String message = "Line with id 1 already exists!";

        // Act
        String result = Messages.AlreadyExists(StringConstants.Line, 1L);

        // Assert
        assertEquals(message, result);
    }

    @Test
    public void AlreadyExistsTest2()
    {
        // Arrange
        String message = "Line with name 1 already exists!";

        // Act
        String result = Messages.AlreadyExists(StringConstants.Line, "1");

        // Assert
        assertEquals(message, result);
    }

    @Test
    public void CantBeEmptyOrWhitespaceTest()
    {
        // Arrange
        String message = "Line can`t be empty or whitespace!";

        // Act
        String result = Messages.CantBeEmptyOrWhitespace(StringConstants.Line);

        // Assert
        assertEquals(message, result);
    }

    @Test
    public void CantBeNullTest()
    {
        // Arrange
        String message = "Line can`t be null!";

        // Act
        String result = Messages.CantBeNull(StringConstants.Line);

        // Assert
        assertEquals(message, result);
    }

    @Test
    public void DoesNotExistTest()
    {
        // Arrange
        String message = "Line with id 1 does not exist.";

        // Act
        String result = Messages.DoesNotExist(StringConstants.Line, 1L);

        // Assert
        assertEquals(message, result);
    }

    @Test
    public void DoesNotContainTest()
    {
        // Arrange
        String message = "Line with id 1 doesn`t contains Stop with id 2";

        // Act
        String result = Messages.DoesNotContain(StringConstants.Line, 1L, StringConstants.Stop, 2L);

        // Assert
        assertEquals(message, result);
    }

    @Test
    public void ErrorSavingTest()
    {
        // Arrange
        String message = "Error saving Line, exception occurred!";

        // Act
        String result = Messages.ErrorSaving(StringConstants.Line, "exception occurred!");

        // Assert
        assertEquals(message, result);
    }

    @Test
    public void ErrorSavingTest2()
    {
        // Arrange
        String message = "Error saving Line 1, exception occurred!";

        // Act
        String result = Messages.ErrorSaving(StringConstants.Line, "1", "exception occurred!");

        // Assert
        assertEquals(message, result);
    }

    @Test
    public void ErrorDeletingTest()
    {
        // Arrange
        String message = "Error deleting Line with id 1, exception occurred!";

        // Act
        String result = Messages.ErrorDeleting(StringConstants.Line, 1L, "exception occurred!");

        // Assert
        assertEquals(message, result);
    }

    @Test
    public void SuccessfullySavedTest()
    {
        // Arrange
        String message = "Line 1 successfully saved!";

        // Act
        String result = Messages.SuccessfullySaved(StringConstants.Line, "1");

        // Assert
        assertEquals(message, result);
    }

    @Test
    public void SuccessfullySavedTest2()
    {
        // Arrange
        String message = "Line successfully saved!";

        // Act
        String result = Messages.SuccessfullySaved(StringConstants.Line);

        // Assert
        assertEquals(message, result);
    }

    @Test
    public void SuccessfullyDeletedTest()
    {
        // Arrange
        String message = "Line with id 1 successfully deleted!";

        // Act
        String result = Messages.SuccessfullyDeleted(StringConstants.Line, 1L);

        // Assert
        assertEquals(message, result);
    }

}
