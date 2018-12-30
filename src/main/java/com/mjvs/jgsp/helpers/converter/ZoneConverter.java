package com.mjvs.jgsp.helpers.converter;

import com.mjvs.jgsp.dto.BaseDTO;
import com.mjvs.jgsp.dto.ZoneDTO;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Zone;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ZoneConverter
{
    public static List<BaseDTO> ConvertZonesToZoneDTOs(List<Zone> zones)
    {
        return zones.stream()
            .map(x -> new BaseDTO(x.getId(), x.getName())).collect(Collectors.toList());
    }

    public static List<ZoneDTO> ConvertZonesToZoneDTOsWithLine(List<Zone> zones)
    {
        List<ZoneDTO> zoneDTOS = new ArrayList<ZoneDTO>();
        List<BaseDTO> lines;

        for (Zone zone : zones) {
            lines = new ArrayList<BaseDTO>();

            for (Line line: zone.getLines()) {
                lines.add(new BaseDTO(line.getId(), line.getName()));
            }
            zoneDTOS.add(new ZoneDTO(zone.getName(), lines));
        }

        return zoneDTOS;
    }
}
