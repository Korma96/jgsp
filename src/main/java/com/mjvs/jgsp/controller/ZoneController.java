package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.controller.exception.BadRequestException;
import com.mjvs.jgsp.controller.exception.DatabaseException;
import com.mjvs.jgsp.dto.LineLiteDTO;
import com.mjvs.jgsp.dto.ZoneDTO;
import com.mjvs.jgsp.dto.ZoneLiteDTO;
import com.mjvs.jgsp.helpers.*;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.service.LineService;
import com.mjvs.jgsp.service.ZoneService;
import com.mjvs.jgsp.service.converter.LineConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping(value = "/zone")
public class ZoneController
{
    private ZoneService zoneService;
    private LineService lineService;

    @Autowired
    public ZoneController(ZoneService zoneService, LineService lineService)
    {
        this.zoneService = zoneService;
        this.lineService = lineService;
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
    public ResponseEntity addLineToZone(@RequestBody ZoneDTO zoneDTO) throws Exception
    {
        Result<Zone> zoneResult = zoneService.findById(zoneDTO.getZoneId());
        if(!zoneResult.hasData()) {
            throw new BadRequestException(zoneResult.getMessage());
        }

        Result<Line> lineResult = lineService.findById(zoneDTO.getLineId());
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
        Result<List<ZoneLiteDTO>> result = zoneService.getAll();
        if(result.isFailure()) {
            throw new DatabaseException(result.getMessage());
        }

        return ResponseHelpers.getResponseData(result.getData());
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

    @RequestMapping(value = "/line/remove", method = RequestMethod.POST)
    public ResponseEntity removeLineFromZone(@RequestBody ZoneDTO zone)
    {
        //boolean result = zoneService.removeLineFromZone(zone.getName(), zone.getLineName());
        return ResponseHelpers.getResponseData(true);
    }

    @RequestMapping(value = "/rename", method = RequestMethod.POST)
    public ResponseEntity rename(@RequestBody HashMap<String, String> data)
    {
        //boolean result = zoneService.rename(data);
        return ResponseHelpers.getResponseData(true);
    }
}
