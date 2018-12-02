package com.mjvs.jgsp.helpers.converter;

import com.mjvs.jgsp.dto.ZoneDTO;
import com.mjvs.jgsp.model.Zone;

import java.util.List;
import java.util.stream.Collectors;

public class ZoneConverter
{
    public static List<ZoneDTO> ConvertZonesToZoneDTOs(List<Zone> zones)
    {
        return zones.stream()
            .map(x -> new ZoneDTO(x.getName(), x.getId())).collect(Collectors.toList());
    }
}
