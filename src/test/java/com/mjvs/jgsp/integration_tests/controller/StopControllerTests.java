package com.mjvs.jgsp.integration_tests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjvs.jgsp.controller.StopController;
import com.mjvs.jgsp.dto.BaseDTO;
import com.mjvs.jgsp.dto.ErrorDTO;
import com.mjvs.jgsp.dto.StopDTO;
import com.mjvs.jgsp.dto.StopLiteDTO;
import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.StringConstants;
import com.mjvs.jgsp.helpers.exception.ExceptionResolver;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.repository.StopRepository;
import com.mjvs.jgsp.service.StopService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StopControllerTests
{
    @Autowired
    StopService stopService;

    @Autowired
    StopRepository stopRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Autowired
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

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void Add_StopNameIsEmptyOrWhiteSpace_ThrowsBadRequestException() throws Exception
    {
        // Arrange
        StopDTO stopDTO = new StopDTO()
        {{
           setName("  ");
           setLatitude(1);
           setLongitude(1);
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/stop/add")
                .content(objectMapper.writeValueAsString(stopDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(new ErrorDTO(
                Messages.CantBeEmptyOrWhitespace(StringConstants.Stop))), response.getContentAsString());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void Add_StopExists_ThrowsBadRequestException() throws Exception
    {
        // Arrange
        double lat = 1;
        double lng = 1;
        stopService.save(new Stop(lat, lng, "stop1"));
        StopDTO stopDTO = new StopDTO()
        {{
            setName("stop2");
            setLatitude(lat);
            setLongitude(lng);
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/stop/add")
                .content(objectMapper.writeValueAsString(stopDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(
                new ErrorDTO(String.format("%s with lat %f and lng %f already exist!", StringConstants.Stop, lat, lng))),
                response.getContentAsString());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void Add_StopDoesNotExist_StopSaved() throws Exception
    {
        // Arrange
        stopService.save(new Stop(1, 1, "stop1"));
        double lat = 2;
        double lng = 5;
        StopDTO stopDTO = new StopDTO()
        {{
            setName("stop2");
            setLatitude(lat);
            setLongitude(lng);
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/stop/add")
                .content(objectMapper.writeValueAsString(stopDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(Messages.SuccessfullySaved(StringConstants.Stop), response.getContentAsString());
        assertNotNull(stopRepository.findByLatitudeAndLongitude(lat, lng));
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void ChangeCoordinates_StopDoesNotExist_ThrowsBadRequestException() throws Exception
    {
        // Arrange
        StopLiteDTO stopLiteDTO = new StopLiteDTO()
        {{
           setId(-1L);
           setLatitude(5);
           setLongitude(5);
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/stop/coordinates")
                .content(objectMapper.writeValueAsString(stopLiteDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(
                new ErrorDTO(Messages.DoesNotExist(StringConstants.Stop, stopLiteDTO.getId()))),
                response.getContentAsString());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void ChangeCoordinates_StopWithSameLatAndLngAlreadyExists_ThrowsBadRequestException() throws Exception
    {
        // Arrange
        double lat = 5;
        double lng = 5;
        String name = "stop";
        Stop stop1 = new Stop(lat, lng, name);
        stopRepository.save(stop1);
        Stop stop2 = new Stop(lat + 5, lng + 5, name);
        stopRepository.save(stop2);
        Long id = stopRepository.findByLatitudeAndLongitude(lat + 5, lng + 5).getId();
        StopLiteDTO stopLiteDTO = new StopLiteDTO()
        {{
            setId(id);
            setLatitude(lat);
            setLongitude(lng);
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/stop/coordinates")
                .content(objectMapper.writeValueAsString(stopLiteDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(
                new ErrorDTO(String.format("%s with lat %f and lng %f already exist!",
                        StringConstants.Stop, lat, lng))),
                response.getContentAsString());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void ChangeCoordinates_UpdatingUnchangedCoordinates_StopUpdated() throws Exception
    {
        // Arrange
        double lat = 5;
        double lng = 5;
        String name = "stop";
        Stop stop = new Stop(lat, lng, name);
        stopRepository.save(stop);
        Long id = stopRepository.findByLatitudeAndLongitude(lat, lng).getId();
        StopLiteDTO stopLiteDTO = new StopLiteDTO()
        {{
            setId(id);
            setLatitude(lat);
            setLongitude(lng);
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/stop/coordinates")
                .content(objectMapper.writeValueAsString(stopLiteDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(Messages.SuccessfullySaved(StringConstants.Stop), response.getContentAsString());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void ChangeCoordinates_UpdatingChangedCoordinates_StopUpdated() throws Exception
    {
        // Arrange
        double lat = 5;
        double lng = 5;
        String name = "stop";
        Stop stop = new Stop(lat, lng, name);
        stopRepository.save(stop);
        Long id = stopRepository.findByLatitudeAndLongitude(lat, lng).getId();
        double newLat = 11;
        double newLng = 23;
        StopLiteDTO stopLiteDTO = new StopLiteDTO()
        {{
            setId(id);
            setLatitude(newLat);
            setLongitude(newLng);
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/stop/coordinates")
                .content(objectMapper.writeValueAsString(stopLiteDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(Messages.SuccessfullySaved(StringConstants.Stop), response.getContentAsString());
        Stop dbStop = stopRepository.findById(id).get();
        assertEquals(newLat, dbStop.getLatitude(), 0);
        assertEquals(newLng, dbStop.getLongitude(), 0);
    }

    @Transactional
    @Rollback(true)
    @Test
    public void GetAll_RetrievesOnlyNonDeletedStops_ReturnsData() throws Exception
    {
        // Arrange
        List<Stop> stops = new ArrayList<Stop>()
        {{
            add(new Stop(1, 2, "stop1"));
            add(new Stop(2, 3, "stop2"));
            add(new Stop(5, 4, "stop3"));
        }};
        stops.get(1).setDeleted(true);
        for(Stop s : stops) {
            stopRepository.save(s);
        }
        List<Stop> assertStops = new ArrayList<Stop>()
        {{
           add(stopRepository.findByName("stop1"));
           add(stopRepository.findByName("stop3"));
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(get("/stop/all")).andReturn().getResponse();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(objectMapper.writeValueAsString(assertStops), response.getContentAsString());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void Rename_NameIsEmptyOrWhitespace_ThrowsBadRequestException() throws Exception
    {
        // Arrange
        BaseDTO baseDTO = new BaseDTO()
        {{
           setId(1L);
           setName("  ");
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/stop/rename")
                .content(objectMapper.writeValueAsString(baseDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(new ErrorDTO(
                Messages.CantBeEmptyOrWhitespace(StringConstants.Stop))), response.getContentAsString());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void Rename_StopDoesNotExist_ThrowsBadRequestException() throws Exception
    {
        // Arrange
        BaseDTO baseDTO = new BaseDTO()
        {{
            setId(1111L);
            setName("STOP");
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/stop/rename")
                .content(objectMapper.writeValueAsString(baseDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(
                new ErrorDTO(Messages.DoesNotExist(StringConstants.Stop, baseDTO.getId()))),
                response.getContentAsString());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void Rename_StopWithSameNameDoesNotExist_StopRenamed() throws Exception
    {
        // Arrange
        stopRepository.save(new Stop(5, 5, "stop"));
        stopRepository.save(new Stop(6, 4, "stop3"));

        Long id = stopRepository.findByLatitudeAndLongitude(5, 5).getId();
        String newName = "new stop";
        BaseDTO baseDTO = new BaseDTO()
        {{
            setId(id);
            setName(newName);
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/stop/rename")
                .content(objectMapper.writeValueAsString(baseDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(Messages.SuccessfullySaved(StringConstants.Stop), response.getContentAsString());
        assertEquals(newName, stopRepository.findById(id).get().getName());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void Rename_StopWithSameNameExists_StopRenamed() throws Exception
    {
        // Arrange
        String newName = "new stop";
        stopRepository.save(new Stop(5, 5, "stop3"));
        stopRepository.save(new Stop(6, 4, newName));

        Long id = stopRepository.findByLatitudeAndLongitude(5, 5).getId();
        BaseDTO baseDTO = new BaseDTO()
        {{
            setId(id);
            setName(newName);
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/stop/rename")
                .content(objectMapper.writeValueAsString(baseDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(Messages.SuccessfullySaved(StringConstants.Stop), response.getContentAsString());
        assertEquals(newName, stopRepository.findById(id).get().getName());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void Rename_StopNameIsUnchanged_StopRenamed() throws Exception
    {
        // Arrange
        String newName = "new stop";
        stopRepository.save(new Stop(5, 5, newName));
        stopRepository.save(new Stop(6, 4, "stop4"));

        Long id = stopRepository.findByLatitudeAndLongitude(5, 5).getId();
        BaseDTO baseDTO = new BaseDTO()
        {{
            setId(id);
            setName(newName);
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/stop/rename")
                .content(objectMapper.writeValueAsString(baseDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(Messages.SuccessfullySaved(StringConstants.Stop), response.getContentAsString());
        assertEquals(newName, stopRepository.findById(id).get().getName());
    }
}
