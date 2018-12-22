package com.mjvs.jgsp.unit_tests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjvs.jgsp.controller.LineController;
import com.mjvs.jgsp.dto.BaseDTO;
import com.mjvs.jgsp.dto.ErrorDTO;
import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.StringConstants;
import com.mjvs.jgsp.helpers.converter.LineConverter;
import com.mjvs.jgsp.helpers.exception.ExceptionResolver;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.service.LineService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LineControllerTests
{
    @Spy
    private LineService lineService;

    private MockMvc mockMvc;

    @InjectMocks
    private LineController lineController;

    private JacksonTester<List<BaseDTO>> jsonTester;
    private JacksonTester<ErrorDTO> jsonTesterError;
    private JacksonTester<List<Stop>> jsonTesterStops;
    private JacksonTester<List<Schedule>> jsonTesterSchedules;

    @Before
    public void init()
    {
        // We would need this line if we would not use MockitoJUnitRunner
        MockitoAnnotations.initMocks(this);

        // Initializes the JacksonTester
        JacksonTester.initFields(this, new ObjectMapper());

        mockMvc = MockMvcBuilders.standaloneSetup(lineController)
                .setControllerAdvice(new ExceptionResolver())
                .build();
    }

    @Test
    public void GetActive_ServiceGetActiveMethodFails_ThrowsDatabaseException() throws Exception
    {
        // Arrange
        Result<List<BaseDTO>> linesResult = new Result<>(null, false, Messages.DatabaseError());
        when(lineService.getActiveLines()).thenReturn(linesResult);

        // Act
        mockMvc.perform(get("/line/active"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("{\"error\":\"" + Messages.DatabaseError() + "\"}"));
    }

    @Test
    public void GetActive_ServiceGetActiveMethodSucceeded_ReturnsData() throws Exception
    {
        // Arrange
        List<BaseDTO> lines = new ArrayList<BaseDTO>()
        {{
            add(new BaseDTO()
            {{
                setId(1L);
                setName("line1");
            }});
            add(new BaseDTO()
            {{
                setId(2L);
                setName("line1");
            }});
        }};

        Result<List<BaseDTO>> linesResult = new Result<>(lines);
        when(lineService.getActiveLines()).thenReturn(linesResult);

        // Act
        MockHttpServletResponse response = mockMvc.perform(get("/line/active")).andReturn().getResponse();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(response.getContentAsString(), jsonTester.write(lines).getJson());
    }

    @Test
    public void GetAll_ServiceGetAllMethodFails_ThrowsDatabaseException() throws Exception
    {
        // Arrange
        Result<List<Line>> linesResult = new Result<>(null, false, Messages.DatabaseError());
        when(lineService.getAll()).thenReturn(linesResult);

        // Act
        mockMvc.perform(get("/line/all"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("{\"error\":\"" + Messages.DatabaseError() + "\"}"));
    }

    @Test
    public void GetAll_ServiceGetAllMethodSucceeded_ReturnsData() throws Exception
    {
        // Arrange
        List<Line> lines = new ArrayList<Line>()
        {{
            add(new Line("line1")
            {{
                setId(1L);
            }});
            add(new Line("line2")
            {{
                setId(2L);
            }});
        }};
        Result<List<Line>> linesResult = new Result<>(lines, Messages.DatabaseError());
        when(lineService.getAll()).thenReturn(linesResult);
        List<BaseDTO> responseData = LineConverter.ConvertLinesToBaseDTOs(lines);

        // Act
        MockHttpServletResponse response = mockMvc.perform(get("/line/all")).andReturn().getResponse();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(response.getContentAsString(), jsonTester.write(responseData).getJson());
    }

    @Test
    public void GetLineStops_LineDoesNotExist_ThrowsNotFoundException() throws Exception
    {
        // Arrange
        Long id = 1L;
        Result<Line> lineResult = new Result<>(null, false, Messages.DoesNotExist(StringConstants.Line, id));
        when(lineService.findById(id)).thenReturn(lineResult);

        // Act
        MockHttpServletResponse response = mockMvc.perform(get("/line/1/stop")).andReturn().getResponse();

        // Assert
        assertEquals(response.getContentAsString(), jsonTesterError.write(new ErrorDTO(lineResult.getMessage())).getJson());
    }

    @Test
    public void GetLineStops_LineExist_ReturnsStops() throws Exception
    {
        // Arrange
        Long id = 1L;
        Line line = new Line();
        line.setId(id);
        List<Stop> stops = new ArrayList<Stop>()
        {{
            add(new Stop(){{ setId(2L);}});
            add(new Stop(){{ setId(3L);}});
            add(new Stop(){{ setId(1L);}});
        }};
        line.setStops(stops);
        Result<Line> lineResult = new Result<>(line, Messages.DoesNotExist(StringConstants.Line, id));
        when(lineService.findById(id)).thenReturn(lineResult);
        List<Stop> sortedStops = lineService.getSortedStopsById(stops);

        // Act
        MockHttpServletResponse response = mockMvc.perform(get("/line/1/stop")).andReturn().getResponse();

        // Assert
        assertEquals(jsonTesterStops.write(sortedStops).getJson(), response.getContentAsString());
    }

    @Test
    public void GetLineSchedules_LineDoesNotExist_ThrowsNotFoundException() throws Exception
    {
        // Arrange
        Long id = 1L;
        Result<Line> lineResult = new Result<>(null, false, Messages.DoesNotExist(StringConstants.Line, id));
        when(lineService.findById(id)).thenReturn(lineResult);

        // Act
        MockHttpServletResponse response = mockMvc.perform(get("/line/1/schedule")).andReturn().getResponse();

        // Assert
        assertEquals(response.getContentAsString(), jsonTesterError.write(new ErrorDTO(lineResult.getMessage())).getJson());
    }

    @Test
    public void GetLineSchedules_LineExist_ReturnsSchedules() throws Exception
    {
        // Arrange
        Long id = 1L;
        Line line = new Line();
        line.setId(id);
        List<Schedule> schedules = new ArrayList<Schedule>()
        {{
            add(new Schedule(){{ setDateFrom(LocalDate.of(2018, 12,3));}});
            add(new Schedule(){{ setDateFrom(LocalDate.of(2018, 11,22));}});
            add(new Schedule(){{ setDateFrom(LocalDate.of(2018, 12,15));}});
        }};
        line.setSchedules(schedules);
        Result<Line> lineResult = new Result<>(line, Messages.DoesNotExist(StringConstants.Line, id));
        when(lineService.findById(id)).thenReturn(lineResult);
        List<Schedule> latestSchedules = lineService.getLatestSchedules(schedules);

        // Act
        MockHttpServletResponse response = mockMvc.perform(get("/line/1/schedule")).andReturn().getResponse();

        // Assert
        assertEquals(jsonTesterSchedules.write(latestSchedules).getJson(), response.getContentAsString());
    }


}
