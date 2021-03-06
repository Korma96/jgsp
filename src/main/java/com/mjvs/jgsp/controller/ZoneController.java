package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.dto.BaseDTO;
import com.mjvs.jgsp.dto.NewZoneDTO;
import com.mjvs.jgsp.dto.ZoneDTO;
import com.mjvs.jgsp.dto.ZoneWithLineDTO;
import com.mjvs.jgsp.helpers.*;
import com.mjvs.jgsp.helpers.converter.LineConverter;
import com.mjvs.jgsp.helpers.converter.ZoneConverter;
import com.mjvs.jgsp.helpers.exception.BadRequestException;
import com.mjvs.jgsp.helpers.exception.DatabaseException;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.TransportType;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.service.LineService;
import com.mjvs.jgsp.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    @PreAuthorize("hasAuthority('TRANSPORT_ADMINISTRATOR')")
    @RequestMapping(value = "/addZone", method = RequestMethod.POST)
    public ResponseEntity addZone(@RequestBody NewZoneDTO newZoneDTO) throws Exception
    {
        int transportType = newZoneDTO.getTransportType();
        TransportType transportTypeEnum = null;
        switch (transportType) {
            case 0:
                transportTypeEnum = TransportType.BUS;
                break;
            case 1:
                transportTypeEnum = TransportType.TRAM;
                break;
            case 2:
                transportTypeEnum = TransportType.METRO;
                break;
        }
        if (transportTypeEnum == null) {
            throw new BadRequestException("Transport type must be 0, 1 or 2 !");
        }

        if(StringExtensions.isEmptyOrWhitespace(newZoneDTO.getName())) {
            throw new BadRequestException(Messages.CantBeEmptyOrWhitespace(StringConstants.Zone));
        }

        Result<Boolean> existsResult = zoneService.exists(newZoneDTO.getName());
        if(existsResult.getData()) {
            throw new BadRequestException(existsResult.getMessage());
        }

        Zone newZone = new Zone(newZoneDTO.getName(), transportTypeEnum);
        Result addResult = zoneService.save(newZone);
        if(addResult.isFailure()) {
            throw new DatabaseException(addResult.getMessage());
        }

        return ResponseHelpers.getResponseData(addResult);
    }

    @PreAuthorize("hasAuthority('TRANSPORT_ADMINISTRATOR')")
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
        List<Line> zoneLines = zone.getLines();
        if(zoneLines.stream().anyMatch(x -> x.getId().equals(line.getId()))) {
            throw new BadRequestException(Messages.AlreadyContains(
                    StringConstants.Zone, zone.getId(), StringConstants.Line, line.getId()));
        }

        zoneLines.add(line);
        zone.setLines(zoneLines);
        line.setZone(zone);

        lineService.checkIfLineCanBeActive(line);

        Result<Boolean> saveLineResult = lineService.save(line);
        if(saveLineResult.isFailure()) {
            throw new DatabaseException(saveLineResult.getMessage());
        }

        Result<Boolean> saveResult = zoneService.save(zone);
        if(saveResult.isFailure()) {
            throw new DatabaseException(saveResult.getMessage());
        }

        return ResponseHelpers.getResponseData(saveResult);
    }

    @PreAuthorize("hasAuthority('TRANSPORT_ADMINISTRATOR')")
    @Override
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") Long id) throws Exception
    {
        Result<Zone> findResult = zoneService.findById(id);
        if(!findResult.hasData()){
            throw new BadRequestException(findResult.getMessage());
        }

        Zone zone = findResult.getData();
        // if zone is logically deleted, zone lines are still referencing delete zone,
        // so zone from that lines must be removed
        int zoneLinesSize = zone.getLines().size();
        for(int i=0; i<zoneLinesSize; i++) {
            zone.getLines().get(0).setActive(false);
            Result<Boolean> saveResult = lineService.save(zone.getLines().get(0));
            if(saveResult.isFailure()) {
                throw new DatabaseException(saveResult.getMessage());
            }
            zone.getLines().remove(0);
        }
        zone.setLines(new ArrayList<>());
        Result<Boolean> deleteResult = zoneService.delete(zone);
        if(deleteResult.isFailure()) {
            throw new DatabaseException(deleteResult.getMessage());
        }

        return ResponseHelpers.getResponseData(deleteResult);
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

    @RequestMapping(value = "/all/{transport_type}", method = RequestMethod.GET)
    public ResponseEntity getAllByTransportType(@PathVariable("transport_type") int transportType) throws Exception
    {
        TransportType transportTypeEnum = null;
        switch (transportType) {
            case 0:
                transportTypeEnum = TransportType.BUS;
                break;
            case 1:
                transportTypeEnum = TransportType.TRAM;
                break;
            case 2:
                transportTypeEnum = TransportType.METRO;
                break;
        }
        if (transportTypeEnum == null) {
            throw new BadRequestException("Transport type must be 0, 1 or 2 !");
        }

        Result<List<Zone>> result = zoneService.getAll();
        if(result.isFailure()) {
            throw new DatabaseException(result.getMessage());
        }

        List<Zone> filteredZones = new ArrayList<>();
        for(Zone zone : result.getData()) {
            if (zone.getTransportType() == transportTypeEnum) {
                filteredZones.add(zone);
            }
        }

        List<BaseDTO> zoneLiteDTOs = ZoneConverter.ConvertZonesToZoneDTOs(filteredZones);
        return ResponseHelpers.getResponseData(zoneLiteDTOs);
    }

    @RequestMapping(value = "/all-with-line", method = RequestMethod.GET)
    public ResponseEntity<List<ZoneDTO>> getAllWithLine()
    {
        Result<List<Zone>> result = zoneService.getAll();
        if(result.isFailure()) {
            throw new DatabaseException(result.getMessage());
        }

        result.getData().stream()
                .forEach(zone -> {
                    List<Line> filteredLines = zone.getLines().stream()
                        .filter(x -> !x.isDeleted()).collect(Collectors.toList());
                    filteredLines.sort(new Comparator<Line>() {
                        @Override
                        public int compare(Line o1, Line o2) {
                            int num1 = extractInt(o1.getName());
                            int num2 = extractInt(o2.getName());

                            if(num1 == 0 && num2 == 0) return o1.getName().compareTo(o2.getName());

                            return num1 - num2;
                        }

                        int extractInt(String s) {
                            String sCopy = new String(s);
                            String num = sCopy.replaceAll("\\D", "");
                            // return 0 if no digits found
                            if(num.isEmpty()) return 0;
                            else {
                                if(s.startsWith(num)) return Integer.parseInt(num);

                                return 0;
                            }

                        }
                    });
                    zone.setLines(filteredLines);
                });
        List<ZoneDTO> zoneLiteDTOs = ZoneConverter.ConvertZonesToZoneDTOsWithLine(result.getData());
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
        List<BaseDTO> lines = LineConverter.ConvertLinesToBaseDTOs(zone.getLines());

        return ResponseHelpers.getResponseData(lines);
    }

    @PreAuthorize("hasAuthority('TRANSPORT_ADMINISTRATOR')")
    @RequestMapping(value = "/line/remove", method = RequestMethod.POST)
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
        List<Line> zoneLines = zone.getLines();
        if(zoneLines.stream().noneMatch(x -> x.getId().equals(line.getId()))) {
            throw new BadRequestException(Messages.DoesNotContain(
                    StringConstants.Zone, zone.getId(), StringConstants.Line, line.getId()));
        }

        // update line
        line.setZone(null);
        lineService.checkIfLineCanBeActive(line);

        Result<Boolean> saveLineResult = lineService.save(line);
        if(saveLineResult.isFailure()) {
            throw new DatabaseException(saveLineResult.getMessage());
        }

        // zoneLines.remove(line) not working -> throws StackOverflowException :/
        int index = -1;
        for(int i=0; i<zoneLines.size(); i++){
            if(zoneLines.get(i).getId().equals(line.getId())){
                index = i;
                break;
            }
        }
        // update zone
        zoneLines.remove(index);
        zone.setLines(zoneLines);

        Result<Boolean> saveZoneResult = zoneService.save(zone);
        if(saveZoneResult.isFailure()) {
            throw new DatabaseException(saveZoneResult.getMessage());
        }

        return ResponseHelpers.getResponseData(saveZoneResult);
    }

}