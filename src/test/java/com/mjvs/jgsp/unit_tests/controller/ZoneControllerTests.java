package com.mjvs.jgsp.unit_tests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjvs.jgsp.controller.ZoneController;
import com.mjvs.jgsp.dto.BaseLiteDTO;
import com.mjvs.jgsp.dto.ErrorDTO;
import com.mjvs.jgsp.dto.NewZoneDTO;
import com.mjvs.jgsp.dto.ZoneWithLineDTO;
import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.StringConstants;
import com.mjvs.jgsp.helpers.exception.ExceptionResolver;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.TransportType;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.repository.LineRepository;
import com.mjvs.jgsp.repository.ZoneRepository;
import com.mjvs.jgsp.service.LineService;
import com.mjvs.jgsp.service.ZoneService;
import org.junit.Before;
import org.junit.Ignore;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ZoneControllerTests
{
    @Mock
    ZoneService zoneService;

    @Mock
    LineService lineService;

    @Autowired
    ZoneRepository zoneRepository;

    @Autowired
    LineRepository lineRepository;

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
    public void AddZone_SaveFails_ThrowsDatabaseException() throws Exception
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
        doReturn(new Result<>(false)).when(zoneService).exists(name);
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

    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void AddLineToZone_SavingLineFails_ThrowsDatabaseException() throws Exception
    {
        // Arrange
        String zoneName = "zone33";
        Zone zone = new Zone(zoneName, TransportType.BUS);
        String lineName = "line33";
        Line line = new Line(lineName);
        ZoneWithLineDTO zoneWithLineDTO = new ZoneWithLineDTO()
        {{
            setZoneId(1L);
            setLineId(3L);
        }};
        doReturn(new Result<>(line)).when(lineService).findById(any());
        doReturn(new Result<>(zone)).when(zoneService).findById(any());
        doReturn(new Result<Boolean>(null, false,
                Messages.ErrorSaving(StringConstants.Line, Messages.DatabaseError())))
                .when(lineService).save(any());

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/zone/line/add")
                .content(objectMapper.writeValueAsString(zoneWithLineDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(new ErrorDTO(Messages.ErrorSaving(StringConstants.Line,
                Messages.DatabaseError()))), response.getContentAsString());
    }

    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void AddLineToZone_SavingZoneFails_ThrowsDatabaseException() throws Exception
    {
        // Arrange
        String zoneName = "zone33";
        Zone zone = new Zone(zoneName, TransportType.BUS);
        String lineName = "line33";
        Line line = new Line(lineName);
        ZoneWithLineDTO zoneWithLineDTO = new ZoneWithLineDTO()
        {{
            setZoneId(1L);
            setLineId(3L);
        }};
        doReturn(new Result<>(line)).when(lineService).findById(zoneWithLineDTO.getLineId());
        doReturn(new Result<>(zone)).when(zoneService).findById(zoneWithLineDTO.getZoneId());
        doReturn(new Result<Boolean>(null, true)).when(lineService).save(line);
        doReturn(new Result<Boolean>(null, false,
                Messages.ErrorSaving(StringConstants.Zone, Messages.DatabaseError())))
                .when(zoneService).save(zone);

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/zone/line/add")
                .content(objectMapper.writeValueAsString(zoneWithLineDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(new ErrorDTO(Messages.ErrorSaving(StringConstants.Zone,
                Messages.DatabaseError()))), response.getContentAsString());
    }

    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void Delete_SavingLineFails_ThrowsDatabaseException() throws Exception
    {
        // Arrange
        long zoneId = 201L;
        Zone zone = new Zone("testZone", TransportType.BUS);
        List<Line> zoneLines = new ArrayList<>();
        zoneLines.add(new Line("line1"));
        zone.setLines(zoneLines);

        doReturn(new Result<>(zone)).when(zoneService).findById(any());
        doReturn(new Result<Boolean>(null, false,
                Messages.ErrorSaving(StringConstants.Line, Messages.DatabaseError())))
                .when(lineService).save(any());

        // Act
        MockHttpServletResponse response = mockMvc.perform(delete("/zone/" + zoneId + "/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(new ErrorDTO(Messages.ErrorSaving(StringConstants.Line,
                Messages.DatabaseError()))), response.getContentAsString());
    }

    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void Delete_SavingZoneFails_ThrowsDatabaseException() throws Exception
    {
        // Arrange
        long zoneId = 201L;
        Zone zone = new Zone("testZone", TransportType.BUS);
        List<Line> zoneLines = new ArrayList<>();
        zoneLines.add(new Line("line1"));
        zone.setLines(zoneLines);

        doReturn(new Result<>(zone)).when(zoneService).findById(any());
        doReturn(new Result<Boolean>(null, true)).when(lineService).save(any());
        doReturn(new Result<Boolean>(null, false,
                Messages.ErrorDeleting(StringConstants.Zone, zoneId, Messages.DatabaseError())))
                .when(zoneService).delete(any());

        // Act
        MockHttpServletResponse response = mockMvc.perform(delete("/zone/" + zoneId + "/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(new ErrorDTO(Messages.ErrorDeleting(StringConstants.Zone, zoneId,
                Messages.DatabaseError()))), response.getContentAsString());
    }

    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void GetAll_ErrorOccurred_ThrowsDatabaseException() throws Exception
    {
        // Arrange
        doReturn(new Result<List<Zone>>(null, false,Messages.DatabaseError())).when(zoneService).getAll();

        // Act
        MockHttpServletResponse response = mockMvc.perform(get("/zone/all")).andReturn().getResponse();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        assertEquals(objectMapper.writeValueAsString(
                new ErrorDTO(Messages.DatabaseError())),
                response.getContentAsString());
    }

    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void GetAllByTransportType_ErrorOccurred_ThrowsDatabaseException() throws Exception
    {
        // Arrange
        int validTransportType = 1;
        doReturn(new Result<List<Zone>>(null, false,Messages.DatabaseError())).when(zoneService).getAll();

        // Act
        MockHttpServletResponse response = mockMvc.perform(get("/zone/all/"+validTransportType))
                                                                                        .andReturn().getResponse();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatus());
        assertEquals(objectMapper.writeValueAsString(
                new ErrorDTO(Messages.DatabaseError())),
                response.getContentAsString());
    }

    @Test
    @Ignore
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void Add_SaveFails_ThrowsDatabaseException() throws Exception
    {
        // Arrange
        String name = "zone1";
        BaseLiteDTO newZoneDTO = new BaseLiteDTO();
        newZoneDTO.setName(name);

        doReturn(new Result<Boolean>(false, true)).when(zoneService).exists(name);
        doReturn(new Result<Zone>(null, false,
                Messages.ErrorSaving(StringConstants.Zone, Messages.DatabaseError())))
                .when(zoneService).save(any());

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/zone/add")
                .content(objectMapper.writeValueAsString(newZoneDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(new ErrorDTO(Messages.ErrorSaving(StringConstants.Zone,
                Messages.DatabaseError()))), response.getContentAsString());
    }
}
