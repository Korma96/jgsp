package com.mjvs.jgsp.unit_tests.helpers;

import com.mjvs.jgsp.model.LineZone;
import com.mjvs.jgsp.model.Zone;
import org.junit.Test;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import static com.mjvs.jgsp.helpers.ReflectionHelpers.*;
import static org.junit.Assert.*;

public class ReflectionHelpersTests
{
    @Test(expected = NoSuchMethodException.class)
    public void InvokeGetIdMethod_ObjHasNotGetIdMethod_ThrowsException() throws Exception
    {
        // Arrange
        Object objWithoutId = new Object();

        // Act
        InvokeGetIdMethod(objWithoutId);
    }

    @Test
    public void InvokeGetIdMethod_ObjHasGetIdMethodAndIdIsNull_ReturnsNull() throws Exception
    {
        // Arrange
        Zone zone = new Zone();

        // Act
        Long id = InvokeGetIdMethod(zone);

        // Assert
        assertNull(id);
    }

    @Test
    public void InvokeGetIdMethod_ObjHasGetIdMethodAndIdIsNotNull_ReturnsId() throws Exception
    {
        // Arrange
        Zone zone = new Zone();
        Long id = 5L;
        zone.setId(id);

        // Act
        Long reflectedId = InvokeGetIdMethod(zone);

        // Assert
        assertEquals(id, reflectedId);
    }

    @Test(expected = NoSuchMethodException.class)
    public void InvokeOptionalIsPresentMethod_ObjHasNotIsPresentMethod_ThrowsException() throws Exception
    {
        // Arrange
        Object obj = new Object();

        // Act
        InvokeOptionalIsPresentMethod(obj);
    }

    @Test
    public void InvokeOptionalIsPresentMethod_NotPresent_ReturnsFalse() throws Exception
    {
        // Arrange
        Optional<Object> optionalObj = Optional.empty();

        // Act
        boolean isPresent = InvokeOptionalIsPresentMethod(optionalObj);

        // Assert
        assertFalse(isPresent);
    }

    @Test
    public void InvokeOptionalIsPresentMethod_Present_ReturnsTrue() throws Exception
    {
        // Arrange
        Object obj = new Object();
        Optional<Object> optionalObj = Optional.of(obj);

        // Act
        boolean isPresent = InvokeOptionalIsPresentMethod(optionalObj);

        // Assert
        assertTrue(isPresent);
    }

    @Test(expected = NoSuchMethodException.class)
    public void InvokeOptionalGetMethod_ObjHasNotGetMethod_ThrowsException() throws Exception
    {
        // Arrange
        Object obj = new Object();

        // Act
        InvokeOptionalGetMethod(obj);
    }

    @Test(expected = InvocationTargetException.class)
    public void InvokeOptionalGetMethod_ObjNotPresentInOptional_ThrowsException() throws Exception
    {
        // Arrange
        Optional<Object> optionalObj = Optional.empty();

        // Act
        InvokeOptionalGetMethod(optionalObj);
    }

    @Test
    public void InvokeOptionalGetMethod_ObjectPresentInOptional_ReturnsObject() throws Exception
    {
        // Arrange
        Object obj = new Object();
        Optional<Object> optionalObj = Optional.of(obj);

        // Act
        Object result = InvokeOptionalGetMethod(optionalObj);

        // Assert
        assertSame(obj, result);
    }

    @Test(expected = NoSuchMethodException.class)
    public void InvokeSetNameMethod_ObjHasNotSetNameMethod_ThrowsException() throws Exception
    {
        // Arrange
        Object obj = new Object();

        // Act
        InvokeSetNameMethod(obj, "");
    }

    @Test
    public void InvokeSetNameMethod_NewNameIsNull_Success() throws Exception
    {
        // Arrange
        Zone zone = new Zone();

        // Act
        InvokeSetNameMethod(zone, null);

        // Assert
        assertNull(zone.getName());
    }

    @Test
    public void InvokeSetNameMethod_NewNameIsSomeString_Success() throws Exception
    {
        // Arrange
        Zone zone = new Zone();
        zone.setName("zone1");
        String newName = "zone2";

        // Act
        InvokeSetNameMethod(zone, newName);

        // Assert
        assertEquals(newName, zone.getName());
    }

    private abstract class AbstractClassHelper
    {

    }

    @Test(expected = InstantiationException.class)
    public void GetInstanceOfT_TypeDoesntHaveDefaultConstructor_ThrowsException() throws Exception
    {
        // Arrange
        Class<AbstractClassHelper> type = AbstractClassHelper.class;

        // Act
        GetInstanceOfT(type);
    }

    @Test
    public void GetInstanceOfT_TypeHasDefaultConstructor_ReturnsInstantiatedObject() throws Exception
    {
        // Arrange
        Class<Zone> type = Zone.class;

        // Act
        Zone zone = GetInstanceOfT(type);

        // Assert
        assertEquals(type, zone.getClass());
    }


}


