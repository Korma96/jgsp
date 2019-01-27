package com.mjvs.jgsp.unit_tests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjvs.jgsp.controller.StopController;
import com.mjvs.jgsp.dto.ErrorDTO;
import com.mjvs.jgsp.dto.StopDTO;
import com.mjvs.jgsp.dto.StopLiteDTO;
import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.StringConstants;
import com.mjvs.jgsp.helpers.exception.ExceptionResolver;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.service.StopService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StopControllerTests
{
    @Mock
    private StopService stopService;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @InjectMocks
    private StopController stopController;

    @Before
    public void init()
    {
        // We would need this line if we would not use MockitoJUnitRunner
        MockitoAnnotations.initMocks(this);

        // Initializes the JacksonTester
        JacksonTester.initFields(this, new ObjectMapper());

        mockMvc = MockMvcBuilders.standaloneSetup(stopController)
                .setControllerAdvice(new ExceptionResolver())
                .build();
    }

    @Test
    public void Add_SaveFails_ThrowsDatabaseException() throws Exception
    {
        // Arrange
        StopDTO stopDTO = new StopDTO()
        {{
            setName("1");
            setLatitude(1);
            setLongitude(1);
        }};
        doReturn(new Result<Stop>(null))
                .when(stopService).findByLatitudeAndLongitude(anyDouble(), anyDouble());
        doReturn(new Result<Stop>(null, false,
                Messages.ErrorSaving(StringConstants.Stop, Messages.DatabaseError())))
                .when(stopService).save(any());

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/stop/add")
                .content(objectMapper.writeValueAsString(stopDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        assertEquals(objectMapper.writeValueAsString(
                new ErrorDTO(Messages.ErrorSaving(StringConstants.Stop, Messages.DatabaseError()))),
                response.getContentAsString());

    }

    @Test
    public void ChangeCoordinates_SaveFails_ThrowsDatabaseException() throws Exception
    {
        // Arrange
        double lat = 5;
        double lng = 5;
        String name = "stop";
        Stop stop = new Stop(lat, lng, name);
        long id = 6L;
        StopLiteDTO stopDTO = new StopLiteDTO()
        {{
            setId(id);
            setLatitude(lat + 8);
            setLongitude(lng - 4);
        }};
        doReturn(new Result<Stop>(stop)).when(stopService).findById(id);
        doReturn(new Result<Stop>(null))
                .when(stopService).findByLatitudeAndLongitude(anyDouble(), anyDouble());
        doReturn(new Result<Stop>(null, false,
                Messages.ErrorSaving(StringConstants.Stop, Messages.DatabaseError())))
                .when(stopService).save(any());

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/stop/coordinates")
                .content(objectMapper.writeValueAsString(stopDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        assertEquals(objectMapper.writeValueAsString(
                new ErrorDTO(Messages.ErrorSaving(StringConstants.Stop, Messages.DatabaseError()))),
                response.getContentAsString());
    }

    @Test
    public void GetAll_ErrorOccurredWhileRetrievingData_ThrowsDatabaseException() throws Exception
    {
        // Arrange
        doReturn(new Result<List<Stop>>(null, false, Messages.DatabaseError())).when(stopService).getAll();

        // Act
        MockHttpServletResponse response = mockMvc.perform(get("/stop/all")).andReturn().getResponse();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        assertEquals(objectMapper.writeValueAsString(
                new ErrorDTO(Messages.DatabaseError())),
                response.getContentAsString());
    }

    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void Delete_DeleteFails_ThrowsDatabaseException() throws Exception
    {
        // Arrange
        long id = 1L;
        Stop stop = new Stop();
        doReturn(new Result<>(stop)).when(stopService).findById(any());
        doReturn(new Result<Stop>(null, false,
                Messages.ErrorDeleting(StringConstants.Stop, id, Messages.DatabaseError())))
                .when(stopService).delete(any());

        // Act
        MockHttpServletResponse response = mockMvc.perform(delete("/stop/" + id + "/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(
                new ErrorDTO(Messages.ErrorDeleting(StringConstants.Stop, id, Messages.DatabaseError()))),
                response.getContentAsString());
    }
}
