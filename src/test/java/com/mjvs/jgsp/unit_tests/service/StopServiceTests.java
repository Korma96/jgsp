package com.mjvs.jgsp.unit_tests.service;

import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.StringConstants;
import com.mjvs.jgsp.service.StopService;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.repository.StopRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StopServiceTests
{
    @Autowired
    private StopService stopService;

    @MockBean
    private StopRepository stopRepository;

    @Test
    public void FindByLatitudeAndLongitude_StopDoesNotExist_ReturnsNull()
    {
        // Arrange
        double lat = 1L;
        double lng = 2L;
        when(stopRepository.findByLatitudeAndLongitude(anyDouble(), anyDouble()))
                .thenReturn(null);

        // Act
        Result<Stop> result = stopService.findByLatitudeAndLongitude(lat, lng);

        // Assert
        assertNull(result.getData());
        assertEquals(String.format("%s with lat %f and lng %f doesn`t exist.",
                StringConstants.Stop, lat, lng), result.getMessage());
    }

    @Test
    public void FindByLatitudeAndLongitude_StopExists_ReturnsStop()
    {
        // Arrange
        double lat = 1L;
        double lng = 2L;
        Stop stop = new Stop();
        stop.setName("stop1");
        stop.setLatitude(lat);
        stop.setLongitude(lng);
        when(stopRepository.findByLatitudeAndLongitude(lat, lng))
                .thenReturn(stop);

        // Act
        Result<Stop> result = stopService.findByLatitudeAndLongitude(lat, lng);

        // Assert
        assertNotNull(result.getData());
        assertSame(result.getData(), stop);
        assertEquals(String.format("%s with lat %f and lng %f already exist!",
                StringConstants.Stop, lat, lng), result.getMessage());
    }

}
