package com.mjvs.jgsp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mjvs.jgsp.dto.LineLiteDTO;
import com.mjvs.jgsp.dto.ZoneDTO;
import com.mjvs.jgsp.dto.ZoneLiteDTO;
import com.mjvs.jgsp.dto.ZoneWithLineDTO;
import com.mjvs.jgsp.helpers.*;
import com.mjvs.jgsp.helpers.converter.LineConverter;
import com.mjvs.jgsp.helpers.converter.ZoneConverter;
import com.mjvs.jgsp.helpers.exception.BadRequestException;
import com.mjvs.jgsp.helpers.exception.DatabaseException;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.service.LineService;
import com.mjvs.jgsp.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/zone")
public class ZoneController
{
    private ZoneService zoneService;
    private LineService lineService;

    @Autowired
    public ZoneController(LineService lineService, ZoneService zoneService)
    {
        this.lineService = lineService;
        this.zoneService = zoneService;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity add(@RequestBody ZoneLiteDTO zone) throws Exception
    {
        if(StringExtensions.isEmptyOrWhitespace(zone.getName())) {
            throw new BadRequestException(Messages.CantBeEmptyOrWhitespace(StringConstants.Zone));
        }

        Result<Boolean> existsResult = zoneService.exists(zone.getName());
        if(existsResult.getData()) {
            throw new BadRequestException(existsResult.getMessage());
        }

        Result addResult = zoneService.save(new Zone(zone.getName()));
        if(addResult.isFailure()) {
            throw new DatabaseException(addResult.getMessage());
        }

        return ResponseHelpers.getResponseData(addResult.getData());
    }

    @RequestMapping(value = "/line/add", method = RequestMethod.POST)
    public ResponseEntity addLineToZone(@RequestBody ZoneWithLineDTO zoneWithLineDTO) throws Exception
    {
        Result<Zone> zoneResult = zoneService.findById(zoneWithLineDTO.getZoneId());
        if(!zoneResult.hasData()) {
            throw new BadRequestException(zoneResult.getMessage());
        }

        Result<Line> lineResult = lineService.findById(zoneWithLineDTO.getLineId());
        if(!lineResult.hasData()) {
            throw new BadRequestException(lineResult.getMessage());
        }

        Zone zone = zoneResult.getData();
        Line line = lineResult.getData();
        if(zone.getLines().contains(line)) {
            throw new BadRequestException(Messages.AlreadyContains(
                    StringConstants.Zone, zone.getId(), StringConstants.Line, line.getId()));
        }

        zone.getLines().add(line);
        Result<Boolean> saveResult = zoneService.save(zone);
        if(saveResult.isFailure()) {
            throw new DatabaseException(saveResult.getMessage());
        }

        return ResponseHelpers.getResponseData(saveResult.getData());
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity getAll()
    {
        Result<List<Zone>> result = zoneService.getAll();
        if(result.isFailure()) {
            throw new DatabaseException(result.getMessage());
        }

        List<ZoneDTO> zoneLiteDTOs = ZoneConverter.ConvertZonesToZoneDTOs(result.getData());
        return ResponseHelpers.getResponseData(zoneLiteDTOs);
    }

    @RequestMapping(value = "/line/{id}", method = RequestMethod.GET)
    public ResponseEntity getLines(@PathVariable Long id) throws Exception
    {
        Result<Zone> zoneResult = zoneService.findById(id);
        if(!zoneResult.hasData()){
            throw new BadRequestException(zoneResult.getMessage());
        }

        Zone zone = zoneResult.getData();
        List<LineLiteDTO> lines = LineConverter.ConvertLinesToLineLiteDTOs(zone.getLines());

        return ResponseHelpers.getResponseData(lines);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable Long id) throws Exception
    {
        Result<Zone> zoneResult = zoneService.findById(id);
        if(!zoneResult.hasData()) {
            throw new BadRequestException(zoneResult.getMessage());
        }

        Result<Boolean> deleteResult = zoneService.delete(zoneResult.getData());
        if(deleteResult.isFailure()) {
            throw new DatabaseException(deleteResult.getMessage());
        }

        return ResponseHelpers.getResponseData(deleteResult);
    }

    @RequestMapping(value = "/line/remove", method = RequestMethod.DELETE)
    public ResponseEntity removeLineFromZone(@RequestBody ZoneWithLineDTO zoneWithLineDTO) throws Exception
    {
        Result<Zone> zoneResult = zoneService.findById(zoneWithLineDTO.getZoneId());
        if(!zoneResult.hasData()) {
            throw new BadRequestException(zoneResult.getMessage());
        }

        Result<Line> lineResult = lineService.findById(zoneWithLineDTO.getLineId());
        if(!lineResult.hasData()) {
            throw new BadRequestException(lineResult.getMessage());
        }

        Zone zone = zoneResult.getData();
        Line line = lineResult.getData();
        if(!zone.getLines().contains(line)) {
            throw new BadRequestException(Messages.DoesNotContains(
                    StringConstants.Zone, zone.getId(), StringConstants.Line, line.getId()));
        }

        zone.getLines().remove(line);
        Result<Boolean> saveResult = zoneService.save(zone);
        if(saveResult.isFailure()) {
            throw new DatabaseException(saveResult.getMessage());
        }

        return ResponseHelpers.getResponseData(saveResult.getData());
    }

    @RequestMapping(value = "/rename", method = RequestMethod.POST)
    public ResponseEntity rename(@RequestBody ZoneDTO zoneDTO) throws Exception
    {
        if(StringExtensions.isEmptyOrWhitespace(zoneDTO.getName())) {
            throw new BadRequestException(Messages.CantBeEmptyOrWhitespace(StringConstants.Zone));
        }

        Result<Boolean> existsResult = zoneService.exists(zoneDTO.getName());
        if(existsResult.getData()) {
            throw new BadRequestException(existsResult.getMessage());
        }

        Result<Zone> zoneResult = zoneService.findById(zoneDTO.getId());
        if(!zoneResult.hasData()) {
            throw new BadRequestException(zoneResult.getMessage());
        }

        Zone zone = zoneResult.getData();
        zone.setName(zoneDTO.getName());

        Result<Boolean> saveResult = zoneService.save(zone);
        if(saveResult.isFailure()) {
            throw new DatabaseException(saveResult.getMessage());
        }

        return ResponseHelpers.getResponseData(saveResult.getData());
    }
}