package com.mjvs.jgsp.unit_tests.service;

import com.mjvs.jgsp.dto.BaseDTO;
import com.mjvs.jgsp.dto.StopDTO;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.model.*;
import com.mjvs.jgsp.repository.LineRepository;
import com.mjvs.jgsp.service.LineService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.anyBoolean;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineServiceTests
{
    @Autowired
    private LineService lineService;

    @MockBean
    private LineRepository lineRepository;


    @Test(expected = Exception.class)
    public void GetActiveLines_FindByActiveThrowsException_ExceptionThrown()
    {
        // Arrange
        when(lineRepository.findByActive(anyBoolean())).thenThrow(new Exception());

        // Act
        Result result = lineService.getActiveLines();

        // Assert
        assertNull(result.getData());
        assertFalse(result.isSuccess());
    }

    @Test
    public void GetActiveLines_FindByActiveSucceeded_ReturnsActiveLines()
    {
        // Arrange
        Line line1 = new Line();
        line1.setName("1");
        line1.setActive(true);
        Line line3 = new Line();
        line3.setName("3");
        line3.setActive(true);
        when(lineRepository.findByActive(true)).thenReturn(new ArrayList<Line>()
        {{
            add(line1);
            add(line3);
        }});

        // Act
        Result<List<BaseDTO>> result = lineService.getActiveLines();

        // Assert
        List<BaseDTO> baseDTOs = result.getData();
        assertTrue(result.isSuccess());
        assertEquals(2, baseDTOs.size());
        assertEquals(line1.getName(), baseDTOs.get(0).getName());
        assertEquals(line3.getName(), baseDTOs.get(1).getName());
    }

    @Test
    public void GetSortedStopsById_UnsortedStops_ReturnsSortedStops()
    {
        // Arrange
        Stop stop1 = new Stop();
        stop1.setId(3L);
        stop1.setName("stop3");
        Stop stop2 = new Stop();
        stop2.setId(1L);
        stop2.setName("stop1");
        Stop stop3 = new Stop();
        stop3.setId(2L);
        stop3.setName("stop2");
        List<Stop> stops = new ArrayList<Stop>()
        {{
            add(stop1);
            add(stop2);
            add(stop3);
        }};

        // Act
        List<StopDTO> sortedStops = lineService.getSortedStopsById(stops);

        // Assert
        assertEquals(stops.size(), sortedStops.size());
        assertSame(stop2.getName(), sortedStops.get(0).getName());
        assertSame(stop3.getName(), sortedStops.get(1).getName());
        assertSame(stop1.getName(), sortedStops.get(2).getName());
    }

    @Test
    public void GetSortedStopsById_EmptyList_ReturnsEmptyList()
    {
        // Act
        List<StopDTO> sortedStops = lineService.getSortedStopsById(new ArrayList<>());

        // Assert
        assertEquals(0, sortedStops.size());
    }

    @Test(expected = NullPointerException.class)
    public void GetSortedStopsById_OneStopsIdIsNull_ThrowsNullPointerException()
    {
        // Arrange
        Stop stop1 = new Stop();
        stop1.setId(3L);
        Stop stop2 = new Stop();
        stop2.setId(null);
        Stop stop3 = new Stop();
        stop3.setId(2L);
        List<Stop> stops = new ArrayList<Stop>()
        {{
            add(stop1);
            add(stop2);
            add(stop3);
        }};

        // Act
        lineService.getSortedStopsById(stops);
    }

    @Test
    public void GetLatestSchedules_EmptyList_ReturnsEmptyList()
    {
        // Act
        List<Schedule> schedules = lineService.getLatestSchedules(new ArrayList<>());

        // Assert
        assertEquals(0, schedules.size());
    }

    @Test
    public void GetLatestSchedules_UnsortedSchedules_ReturnsLatestSchedules()
    {
        // Arrange
        LocalDate latestDate = LocalDate.now();
        Schedule schedule1 = new Schedule(DayType.WORKDAY, latestDate, new ArrayList<>());
        Schedule schedule2 = new Schedule(DayType.WORKDAY, LocalDate.of(2018, 3, 22), new ArrayList<>());
        Schedule schedule3 = new Schedule(DayType.SATURDAY, LocalDate.of(2018, 9, 5), new ArrayList<>());
        Schedule schedule4 = new Schedule(DayType.SUNDAY, latestDate, new ArrayList<>());
        Schedule schedule5 = new Schedule(DayType.SUNDAY, LocalDate.of(2017, 3, 22), new ArrayList<>());
        Schedule schedule6 = new Schedule(DayType.SATURDAY, latestDate, new ArrayList<>());
        List<Schedule> schedules = new ArrayList<Schedule>()
        {{
           add(schedule1);
           add(schedule2);
           add(schedule3);
           add(schedule4);
           add(schedule5);
           add(schedule6);
        }};

        // Act
        List<Schedule> latestSchedules = lineService.getLatestSchedules(schedules);

        // Assert
        assertEquals(3, latestSchedules.size());
        assertEquals(latestDate, latestSchedules.get(0).getDateFrom());
        assertEquals(latestDate, latestSchedules.get(1).getDateFrom());
        assertEquals(latestDate, latestSchedules.get(2).getDateFrom());
    }

    @Test
    public void CheckIfLineCanBeActive_ZoneIsNull_LineIsNotActive()
    {
        // Arrange
        Line line1 = new Line("line1");
        Line line2 = new Line("line2");
        line1.setZone(null);
        line2.setZone(null);
        line1.setActive(true);
        line2.setActive(false);

        // Act
        lineService.checkIfLineCanBeActive(line1);
        lineService.checkIfLineCanBeActive(line2);

        // Assert
        assertFalse(line1.isActive());
        assertFalse(line2.isActive());
    }

    @Test
    public void CheckIfLineCanBeActive_MinutesRequiredForWholeRouteIsZero_LineIsNotActive()
    {
        // Arrange
        Zone zone = new Zone();
        int minutes = 0;

        Line line1 = new Line("line1");
        Line line2 = new Line("line2");
        line1.setZone(zone);
        line2.setZone(zone);
        line1.setMinutesRequiredForWholeRoute(minutes);
        line2.setMinutesRequiredForWholeRoute(minutes);
        line1.setActive(true);
        line2.setActive(false);

        // Act
        lineService.checkIfLineCanBeActive(line1);
        lineService.checkIfLineCanBeActive(line2);

        // Assert
        assertFalse(line1.isActive());
        assertFalse(line2.isActive());
    }

    @Test
    public void CheckIfLineCanBeActive_LineDoesNotHaveEnoughStops_LineIsNotActive()
    {
        // Arrange
        Zone zone = new Zone();
        int minutes = 25;
        List<Stop> stops = new ArrayList<>();
        stops.add(new Stop());

        Line line1 = new Line("line1");
        Line line2 = new Line("line2");
        line1.setZone(zone);
        line2.setZone(zone);
        line1.setMinutesRequiredForWholeRoute(minutes);
        line2.setMinutesRequiredForWholeRoute(minutes);
        line1.setStops(stops);
        line2.setStops(stops);
        line1.setActive(true);
        line2.setActive(false);

        // Act
        lineService.checkIfLineCanBeActive(line1);
        lineService.checkIfLineCanBeActive(line2);

        // Assert
        assertFalse(line1.isActive());
        assertFalse(line2.isActive());
    }

    @Test
    public void CheckIfLineCanBeActive_LineDoesNotHaveAnySchedule_LineIsNotActive()
    {
        // Arrange
        Zone zone = new Zone();
        int minutes = 25;
        List<Stop> stops = new ArrayList<>();
        stops.add(new Stop());
        stops.add(new Stop());
        List<Schedule> schedules = new ArrayList<>();

        Line line1 = new Line("line1");
        Line line2 = new Line("line2");
        line1.setZone(zone);
        line2.setZone(zone);
        line1.setMinutesRequiredForWholeRoute(minutes);
        line2.setMinutesRequiredForWholeRoute(minutes);
        line1.setStops(stops);
        line2.setStops(stops);
        line1.setSchedules(schedules);
        line2.setSchedules(schedules);
        line1.setActive(true);
        line2.setActive(false);

        // Act
        lineService.checkIfLineCanBeActive(line1);
        lineService.checkIfLineCanBeActive(line2);

        // Assert
        assertFalse(line1.isActive());
        assertFalse(line2.isActive());
    }

    @Test
    public void CheckIfLineCanBeActive_OneOfLineSchedulesDoesNotContainAnyDepartureTime_LineIsNotActive()
    {
        // Arrange
        Zone zone = new Zone();
        int minutes = 25;
        List<Stop> stops = new ArrayList<>();
        stops.add(new Stop());
        stops.add(new Stop());
        List<Schedule> schedules = new ArrayList<>();
        LocalDate localDate = LocalDate.of(2019, 2, 5);
        List<MyLocalTime> departureTimes = new ArrayList<>();
        departureTimes.add(new MyLocalTime());
        Schedule schedule1 = new Schedule(DayType.WORKDAY, localDate, departureTimes);
        Schedule schedule2 = new Schedule(DayType.SATURDAY, localDate, departureTimes);
        Schedule schedule3 = new Schedule(DayType.SUNDAY, localDate, new ArrayList<>());
        schedules.add(schedule1);
        schedules.add(schedule2);
        schedules.add(schedule3);

        Line line1 = new Line("line1");
        Line line2 = new Line("line2");
        line1.setZone(zone);
        line2.setZone(zone);
        line1.setMinutesRequiredForWholeRoute(minutes);
        line2.setMinutesRequiredForWholeRoute(minutes);
        line1.setStops(stops);
        line2.setStops(stops);
        line1.setSchedules(schedules);
        line2.setSchedules(schedules);
        line1.setActive(true);
        line2.setActive(false);

        // Act
        lineService.checkIfLineCanBeActive(line1);
        lineService.checkIfLineCanBeActive(line2);

        // Assert
        assertFalse(line1.isActive());
        assertFalse(line2.isActive());
    }

    @Test
    public void CheckIfLineCanBeActive_LineCanBeActive_LineIsActive()
    {
        // Arrange
        Zone zone = new Zone();
        int minutes = 25;
        List<Stop> stops = new ArrayList<>();
        stops.add(new Stop());
        stops.add(new Stop());
        List<Schedule> schedules = new ArrayList<>();
        LocalDate localDate = LocalDate.of(2019, 2, 5);
        List<MyLocalTime> departureTimes = new ArrayList<>();
        departureTimes.add(new MyLocalTime());
        Schedule schedule1 = new Schedule(DayType.WORKDAY, localDate, departureTimes);
        Schedule schedule2 = new Schedule(DayType.SATURDAY, localDate, departureTimes);
        Schedule schedule3 = new Schedule(DayType.SUNDAY, localDate, departureTimes);
        schedules.add(schedule1);
        schedules.add(schedule2);
        schedules.add(schedule3);

        Line line1 = new Line("line1");
        Line line2 = new Line("line2");
        line1.setZone(zone);
        line2.setZone(zone);
        line1.setMinutesRequiredForWholeRoute(minutes);
        line2.setMinutesRequiredForWholeRoute(minutes);
        line1.setStops(stops);
        line2.setStops(stops);
        line1.setSchedules(schedules);
        line2.setSchedules(schedules);
        line1.setActive(true);
        line2.setActive(false);

        // Act
        lineService.checkIfLineCanBeActive(line1);
        lineService.checkIfLineCanBeActive(line2);

        // Assert
        assertTrue(line1.isActive());
        assertTrue(line2.isActive());
    }
}
