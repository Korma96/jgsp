package com.mjvs.jgsp.unit_tests.service;

import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.StringConstants;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.repository.ZoneRepository;
import com.mjvs.jgsp.service.ZoneService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityNotFoundException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExtendedBaseServiceTests
{
    @MockBean
    private ZoneRepository baseRepository;

    @Autowired
    private ZoneService baseService;

    @Test
    public void Save_SavingObjUnsuccessfully_ReturnsFalse() throws Exception
    {
        // Arrange
        Zone z = new Zone();
        doThrow(EntityNotFoundException.class).when(baseRepository).save(z);

        // Act
        Result<Boolean> result = baseService.save(z);

        // Assert
        assertFalse(result.getData());
        assertFalse(result.isSuccess());
        assertEquals(Messages.DatabaseError(), result.getMessage());
    }

    @Test
    public void Save_SavingObjSuccessfully_ReturnsTrue() throws Exception
    {
        // Arrange
        String name = "zone1";
        Zone z = new Zone();
        z.setName(name);

        // Act
        Result<Boolean> result = baseService.save(z);

        // Assert
        assertTrue(result.getData());
        assertTrue(result.isSuccess());
        assertEquals(Messages.SuccessfullySaved(StringConstants.Zone, name), result.getMessage());
    }

    @Test
    public void Exists_ObjDoesNotExist_ReturnsFalse() throws Exception
    {
        // Arrange
        String name = "zone1";
        when(baseRepository.findByName(name)).thenReturn(null);

        // Act
        Result<Boolean> result = baseService.exists(name);

        // Assert
        assertFalse(result.getData());
    }

    @Test
    public void Exists_ObjExists_ReturnsTrue() throws Exception
    {
        // Arrange
        String name = "zone1";
        Zone z = new Zone();
        z.setName(name);
        when(baseRepository.findByName(name)).thenReturn(z);

        // Act
        Result<Boolean> result = baseService.exists(name);

        // Assert
        assertTrue(result.getData());
        assertEquals(Messages.AlreadyExists(StringConstants.Zone, name), result.getMessage());
    }
}
