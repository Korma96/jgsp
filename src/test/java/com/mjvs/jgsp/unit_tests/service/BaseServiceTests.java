package com.mjvs.jgsp.unit_tests.service;

import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.StringConstants;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.repository.ScheduleRepository;
import com.mjvs.jgsp.service.ScheduleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseServiceTests
{
    @MockBean
    private ScheduleRepository baseRepository;

    @Autowired
    private ScheduleService baseService;

    @Test
    public void Exists_ObjDoesNotExist_ReturnsFalse() throws Exception
    {
        // Arrange
        Long id = 1L;
        Optional<Schedule> opt = Optional.empty();
        when(baseRepository.findById(id)).thenReturn(opt);

        // Act
        Result<Boolean> result = baseService.exists(id);

        // Assert
        assertFalse(result.getData());
    }

    @Test
    public void Exists_ObjExists_ReturnsTrue() throws Exception
    {
        // Arrange
        Long id = 1L;
        Schedule s = new Schedule();
        s.setId(id);
        when(baseRepository.findById(id)).thenReturn(Optional.of(s));

        // Act
        Result<Boolean> result = baseService.exists(id);

        // Assert
        assertTrue(result.getData());
        assertEquals(Messages.AlreadyExists(StringConstants.Schedule, id), result.getMessage());
    }

    @Test
    public void FindById_ObjNotFound_ReturnsNull() throws Exception
    {
        // Arrange
        Long id = 1L;
        Optional<Schedule> opt = Optional.empty();
        when(baseRepository.findById(id)).thenReturn(opt);

        // Act
        Result<Schedule> result = baseService.findById(id);

        // Assert
        assertNull(result.getData());
        assertEquals(Messages.DoesNotExist(StringConstants.Schedule, id), result.getMessage());
    }

    @Test
    public void FindById_ObjFound_ReturnsObj() throws Exception
    {
        // Arrange
        Long id = 1L;
        Schedule s = new Schedule();
        s.setId(id);
        when(baseRepository.findById(id)).thenReturn(Optional.of(s));

        // Act
        Result<Schedule> result = baseService.findById(id);

        // Assert
        assertNotNull(result.getData());
        assertEquals(s.getId(), result.getData().getId());
    }

    @Test
    public void Delete_ErrorOccurredDuringDeletingObj_ReturnsFalse() throws Exception
    {
        // Arrange
        Long id = 1L;
        Schedule s = new Schedule();
        s.setId(id);
        doThrow(IllegalArgumentException.class).when(baseRepository).save(s);

        // Act
        Result<Boolean> result = baseService.delete(s);

        // Assert
        assertFalse(result.getData());
        assertTrue(result.isFailure());
        assertEquals(Messages.DatabaseError(), result.getMessage());
    }

    @Test
    public void Delete_DeletedSuccessfully_ReturnsTrue() throws Exception
    {
        // Arrange
        Long id = 1L;
        Schedule s = new Schedule();
        s.setId(id);

        // Act
        Result<Boolean> result = baseService.delete(s);

        // Assert
        assertTrue(result.getData());
        assertTrue(result.isSuccess());
        assertEquals(Messages.SuccessfullyDeleted(StringConstants.Schedule, id), result.getMessage());
    }

    @Test
    public void GetAll_UnableToFindAll_ThrowsException()
    {
        // Arrange
        when(baseRepository.findByDeleted(false)).thenThrow(EntityExistsException.class);

        // Act
        Result<List<Schedule>> result = baseService.getAll();

        // Assert
        assertNull(result.getData());
        assertFalse(result.isSuccess());
    }

    @Test
    public void GetAll_Success_ReturnsData()
    {
        // Arrange
        List<Schedule> data = new ArrayList<Schedule>()
        {{
           add(new Schedule());
           add(new Schedule());
        }};
        doReturn(data).when(baseRepository).findByDeleted(false);

        // Act
        Result<List<Schedule>> result = baseService.getAll();

        // Assert
        assertTrue(result.isSuccess());
        assertNotNull(result.getData());
        assertEquals(data.size(), result.getData().size());
    }

    @Test(expected = Exception.class)
    public void Save_ObjIsNull_ThrowsException() throws Exception
    {
        // Act
        baseService.save(null);
    }

    @Test
    public void Save_SavingObjUnsuccessfully_ReturnsFalse() throws Exception
    {
        // Arrange
        Schedule s = new Schedule();
        doThrow(EntityNotFoundException.class).when(baseRepository).save(s);

        // Act
        Result<Boolean> result = baseService.save(s);

        // Assert
        assertFalse(result.getData());
        assertFalse(result.isSuccess());
        assertEquals(Messages.DatabaseError(), result.getMessage());
    }

    @Test
    public void Save_SavingObjSuccessfully_ReturnsTrue() throws Exception
    {
        // Arrange
        Schedule s = new Schedule();

        // Act
        Result<Boolean> result = baseService.save(s);

        // Assert
        assertTrue(result.getData());
        assertTrue(result.isSuccess());
        assertEquals(Messages.SuccessfullySaved(StringConstants.Schedule), result.getMessage());
    }

}
