package com.mjvs.jgsp.integration_tests.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mjvs.jgsp.controller.ZoneController;
import com.mjvs.jgsp.dto.*;
import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.StringConstants;
import com.mjvs.jgsp.helpers.converter.ZoneConverter;
import com.mjvs.jgsp.helpers.exception.ExceptionResolver;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.TransportType;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.repository.LineRepository;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ZoneControllerTests
{
    @Autowired
    ZoneService zoneService;

    @Autowired
    LineRepository lineRepository;

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
    public void AddZone_InvalidTransportType_ThrowsBadRequestException() throws Exception
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
    public void AddZone_NameIsWhitespace_ThrowsBadRequestException() throws Exception
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
    public void AddZone_NameAlreadyExists_ThrowsBadRequestException() throws Exception
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
    public void AddZone_SuccessfullySaved_ReturnsTrue() throws Exception
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
    public void AddLineToZone_ZoneDoesNotExists_ThrowsBadRequestException() throws Exception
    {
        // Arrange
        long zoneId = 1115L;
        ZoneWithLineDTO zoneWithLineDTO = new ZoneWithLineDTO()
        {{
            setZoneId(zoneId);
            setLineId(1114L);
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/zone/line/add")
                .content(objectMapper.writeValueAsString(zoneWithLineDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(new ErrorDTO(Messages.DoesNotExist(StringConstants.Zone, zoneId))),
                                                                                response.getContentAsString());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void AddLineToZone_LineDoesNotExists_ThrowsBadRequestException() throws Exception
    {
        // Arrange
        String zoneName = "zone33";
        zoneRepository.save(new Zone(zoneName, TransportType.BUS));
        long zoneId = zoneRepository.findByName("zone33").getId();
        long lineId = 1114L;
        ZoneWithLineDTO zoneWithLineDTO = new ZoneWithLineDTO()
        {{
            setZoneId(zoneId);
            setLineId(lineId);
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/zone/line/add")
                .content(objectMapper.writeValueAsString(zoneWithLineDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(new ErrorDTO(Messages.DoesNotExist(StringConstants.Line, lineId))),
                response.getContentAsString());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void AddLineToZone_ZoneAlreadyContainsLine_ThrowsBadRequestException() throws Exception
    {
        // Arrange
        String zoneName = "zone33";
        zoneRepository.save(new Zone(zoneName, TransportType.BUS));
        Zone zone = zoneRepository.findByName("zone33");
        long zoneId = zone.getId();
        String lineName = "line33";
        lineRepository.save(new Line(lineName));
        Line line = lineRepository.findByName(lineName);
        long lineId = line.getId();
        List<Line> zoneLines = zone.getLines();
        zoneLines.add(line);
        zone.setLines(zoneLines);
        line.setZone(zone);
        lineRepository.save(line);
        zoneRepository.save(zone);
        ZoneWithLineDTO zoneWithLineDTO = new ZoneWithLineDTO()
        {{
            setZoneId(zoneId);
            setLineId(lineId);
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/zone/line/add")
                .content(objectMapper.writeValueAsString(zoneWithLineDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(new ErrorDTO(Messages.AlreadyContains(
                StringConstants.Zone, zone.getId(), StringConstants.Line, line.getId()))),
                response.getContentAsString());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void AddLineToZone_AddedSuccessfully_ReturnsTrue() throws Exception
    {
        // Arrange
        String zoneName = "zone33";
        zoneRepository.save(new Zone(zoneName, TransportType.BUS));
        Zone zone = zoneRepository.findByName("zone33");
        String lineName = "line33";
        lineRepository.save(new Line(lineName));
        Line line = lineRepository.findByName(lineName);
        ZoneWithLineDTO zoneWithLineDTO = new ZoneWithLineDTO()
        {{
            setZoneId(zone.getId());
            setLineId(line.getId());
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/zone/line/add")
                .content(objectMapper.writeValueAsString(zoneWithLineDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        Result<Boolean> res = new Result<>(true, Messages.SuccessfullySaved(StringConstants.Zone, zoneName));
        assertEquals(objectMapper.writeValueAsString(res), response.getContentAsString());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void Delete_ZoneDoesNotExists_ThrowsBadRequestException() throws Exception
    {
        // Arrange
        long zoneId = 1114L;

        // Act
        MockHttpServletResponse response = mockMvc.perform(delete("/zone/" + zoneId + "/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(new ErrorDTO(Messages.DoesNotExist(StringConstants.Zone, zoneId))),
                response.getContentAsString());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void Delete_SuccessfullyDeleted_ReturnsTrue() throws Exception
    {
        // Arrange
        // create zone
        String zoneName = "zone334";
        zoneRepository.save(new Zone(zoneName, TransportType.BUS));
        Zone zone = zoneRepository.findByName(zoneName);
        long zoneId = zone.getId();
        // create lines
        String lineName1 = "line33";
        lineRepository.save(new Line(lineName1));
        Line line1 = lineRepository.findByName(lineName1);
        String lineName2 = "line22";
        lineRepository.save(new Line(lineName2));
        Line line2 = lineRepository.findByName(lineName2);
        // add lines to zone
        List<Line> zoneLines = zone.getLines();
        zoneLines.add(line1);
        zoneLines.add(line2);
        zone.setLines(zoneLines);
        line1.setZone(zone);
        line2.setZone(zone);
        lineRepository.save(line1);
        lineRepository.save(line2);
        zoneRepository.save(zone);

        // Act
        MockHttpServletResponse response = mockMvc.perform(delete("/zone/" + zoneId + "/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        Result<Boolean> res = new Result<>(true, Messages.SuccessfullyDeleted(StringConstants.Zone, zoneId));
        assertEquals(objectMapper.writeValueAsString(res), response.getContentAsString());
        line1 = lineRepository.findByName(lineName1);
        line2 = lineRepository.findByName(lineName2);
        assertFalse(line1.isActive());
        assertFalse(line2.isActive());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void GetAll_Success_ReturnsData() throws Exception
    {
        // Arrange
        // create
        String zoneName1 = "zone11";
        Zone zone1 = new Zone(zoneName1, TransportType.BUS);
        String zoneName2 = "zone21";
        Zone zone2 = new Zone(zoneName2, TransportType.TRAM);
        String zoneName3 = "zone31";
        Zone zone3 = new Zone(zoneName3, TransportType.METRO);
        // save
        zoneService.save(zone1);
        zoneService.save(zone2);
        zoneService.save(zone3);
        // convert
        zone1 = zoneService.findByName(zoneName1);
        zone2 = zoneService.findByName(zoneName2);
        zone3 = zoneService.findByName(zoneName3);
        List<Zone> zones = new ArrayList<>();
        zones.add(zone1);
        zones.add(zone2);
        zones.add(zone3);
        List<BaseDTO> zoneDTOs = ZoneConverter.ConvertZonesToZoneDTOs(zones);

        // Act
        MockHttpServletResponse response = mockMvc.perform(get("/zone/all")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(zoneDTOs), response.getContentAsString());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void GetAllByTransportType_InvalidTransportType_ThrowsBadRequestException() throws Exception
    {
        // Arrange
        int invalidTransportType = 8;

        // Act
        MockHttpServletResponse response = mockMvc.perform(get("/zone/all/"+invalidTransportType)
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
    public void GetAllByTransportType_Success_ReturnsData() throws Exception
    {
        // Arrange
        // create
        String zoneName1 = "zone11";
        Zone zone1 = new Zone(zoneName1, TransportType.BUS);
        String zoneName2 = "zone21";
        Zone zone2 = new Zone(zoneName2, TransportType.BUS);
        String zoneName3 = "zone31";
        Zone zone3 = new Zone(zoneName3, TransportType.METRO);
        // save
        zoneService.save(zone1);
        zoneService.save(zone2);
        zoneService.save(zone3);
        // convert
        zone1 = zoneService.findByName(zoneName1);
        zone2 = zoneService.findByName(zoneName2);
        zone3 = zoneService.findByName(zoneName3);
        List<Zone> zones = new ArrayList<>();
        zones.add(zone1);
        zones.add(zone2);
        List<BaseDTO> zoneDTOs = ZoneConverter.ConvertZonesToZoneDTOs(zones);
        int validTransportType = 0;

        // Act
        MockHttpServletResponse response = mockMvc.perform(get("/zone/all/"+validTransportType)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        assertEquals(objectMapper.writeValueAsString(zoneDTOs), response.getContentAsString());
    }

    @Transactional
    @Rollback(true)
    @Test
    @WithMockUser(username = "nenad", authorities = { "TRANSPORT_ADMINISTRATOR" })
    public void Add_NameIsEmptyOrWhitespace_ThrowsBadRequestException() throws Exception
    {
        // Arrange
        BaseLiteDTO newZoneDTO = new BaseLiteDTO()
        {{
            setName("   ");
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/zone/add")
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
    public void Add_SuccessfullySaved_ReturnsTrue() throws Exception
    {
        // Arrange
        String name = "zone1";
        Zone zone = new Zone(name, TransportType.BUS);
        zone.setDeleted(true);
        zoneRepository.save(zone);
        NewZoneDTO newZoneDTO = new NewZoneDTO()
        {{
            setName(name);
        }};

        // Act
        MockHttpServletResponse response = mockMvc.perform(post("/zone/add")
                .content(objectMapper.writeValueAsString(newZoneDTO))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        // Assert
        Result<Boolean> res = new Result<>(true, Messages.SuccessfullySaved(StringConstants.Zone, name));
        assertEquals(objectMapper.writeValueAsString(res), response.getContentAsString());
    }
}
