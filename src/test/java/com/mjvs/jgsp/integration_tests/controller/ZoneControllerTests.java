package com.mjvs.jgsp.integration_tests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjvs.jgsp.controller.ZoneController;
import com.mjvs.jgsp.dto.ErrorDTO;
import com.mjvs.jgsp.dto.NewZoneDTO;
import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.StringConstants;
import com.mjvs.jgsp.helpers.exception.ExceptionResolver;
import com.mjvs.jgsp.model.TransportType;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.repository.ZoneRepository;
import com.mjvs.jgsp.service.ZoneService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ZoneControllerTests
{
    @Autowired
    ZoneService zoneService;

    @Autowired
    ZoneRepository zoneRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Autowired
    private ZoneController zoneController;


    @Before
    public void init()
    {
        // We would need this line if we would not use MockitoJUnitRunner
        MockitoAnnotations.initMocks(this);

        // Initializes the JacksonTester
        JacksonTester.initFields(this, new ObjectMapper());

        mockMvc = MockMvcBuilders.standaloneSetup(zoneController)
                .setControllerAdvice(new ExceptionResolver())
                .build();
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void Add_InvalidTransportType_ThrowsBadRequestException() throws Exception
    {
        // Arrange
        NewZoneDTO newZoneDTO = new NewZoneDTO()
        {{
           setTransportType(6);
           setName("zone5");
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/zone/addZone")
                .content(objectMapper.writeValueAsString(newZoneDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(new ErrorDTO("Transport type must be 0, 1 or 2 !")),
                                                                            response.getContentAsString());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void Add_NameIsWhitespace_ThrowsBadRequestException() throws Exception
    {
        // Arrange
        NewZoneDTO newZoneDTO = new NewZoneDTO()
        {{
            setTransportType(1);
            setName("   ");
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/zone/addZone")
                .content(objectMapper.writeValueAsString(newZoneDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(new ErrorDTO(Messages.CantBeEmptyOrWhitespace(StringConstants.Zone))),
                response.getContentAsString());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void Add_NameAlreadyExists_ThrowsBadRequestException() throws Exception
    {
        // Arrange
        String name = "zone5";
        zoneRepository.save(new Zone(name, TransportType.BUS));
        NewZoneDTO newZoneDTO = new NewZoneDTO()
        {{
            setTransportType(1);
            setName(name);
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/zone/addZone")
                .content(objectMapper.writeValueAsString(newZoneDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(new ErrorDTO(Messages.AlreadyExists(StringConstants.Zone, name))),
                response.getContentAsString());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void Add_SuccessfullySaved_ReturnsTrue() throws Exception
    {
        // Arrange
        String name = "zone1";
        Zone zone = new Zone(name, TransportType.BUS);
        zone.setDeleted(true);
        zoneRepository.save(zone);
        NewZoneDTO newZoneDTO = new NewZoneDTO()
        {{
            setTransportType(1);
            setName(name);
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/zone/addZone")
                .content(objectMapper.writeValueAsString(newZoneDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        Result<Boolean> res = new Result<>(true, Messages.SuccessfullySaved(StringConstants.Zone, name));
        assertEquals(objectMapper.writeValueAsString(res), response.getContentAsString());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void addLineToZone_ZoneDoesNotExists_ThrowsBadRequestException()
    {

    }



}
