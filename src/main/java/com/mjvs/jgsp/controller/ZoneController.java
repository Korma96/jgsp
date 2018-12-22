package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.dto.BaseDTO;
import com.mjvs.jgsp.dto.BaseLiteDTO;
import com.mjvs.jgsp.dto.ZoneWithLineDTO;
import com.mjvs.jgsp.service.LineService;
import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.ResponseHelpers;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.StringConstants;
import com.mjvs.jgsp.helpers.converter.LineConverter;
import com.mjvs.jgsp.helpers.converter.ZoneConverter;
import com.mjvs.jgsp.helpers.exception.BadRequestException;
import com.mjvs.jgsp.helpers.exception.DatabaseException;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/zone")
public class ZoneController extends ExtendedBaseController<Zone>
{
    private ZoneService zoneService;
    private LineService lineService;

    @Autowired
    public ZoneController(LineService lineService, ZoneService zoneService)
    {
        super(zoneService);
        this.lineService = lineService;
        this.zoneService = zoneService;
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

        return ResponseHelpers.getResponseData(saveResult);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity getAll()
    {
        Result<List<Zone>> result = zoneService.getAll();
        if(result.isFailure()) {
            throw new DatabaseException(result.getMessage());
        }

        List<BaseDTO> zoneLiteDTOs = ZoneConverter.ConvertZonesToZoneDTOs(result.getData());
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
        List<BaseLiteDTO> lines = LineConverter.ConvertLinesToBaseLiteDTOs(zone.getLines());

        return ResponseHelpers.getResponseData(lines);
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
            throw new BadRequestException(Messages.DoesNotContain(
                    StringConstants.Zone, zone.getId(), StringConstants.Line, line.getId()));
        }

        zone.getLines().remove(line);
        Result<Boolean> saveResult = zoneService.save(zone);
        if(saveResult.isFailure()) {
            throw new DatabaseException(saveResult.getMessage());
        }

        return ResponseHelpers.getResponseData(saveResult);
    }

}