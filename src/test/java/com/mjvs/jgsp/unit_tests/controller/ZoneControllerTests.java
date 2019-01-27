package com.mjvs.jgsp.unit_tests.controller;

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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ZoneControllerTests
{
    @Mock
    ZoneService zoneService;

    @Autowired
    ZoneRepository zoneRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @InjectMocks
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

    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void Add_SaveFails_ThrowsDatabaseException() throws Exception
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
        doReturn(new Result<Boolean>(false)).when(zoneService).exists(name);
        doReturn(new Result<Zone>(null, false,
                Messages.ErrorSaving(StringConstants.Zone, Messages.DatabaseError())))
                .when(zoneService).save(any());

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/zone/addZone")
                .content(objectMapper.writeValueAsString(newZoneDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(new ErrorDTO(Messages.ErrorSaving(StringConstants.Zone,
                Messages.DatabaseError()))), response.getContentAsString());
    }
}
