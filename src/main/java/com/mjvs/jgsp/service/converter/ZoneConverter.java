package com.mjvs.jgsp.service.converter;

import com.mjvs.jgsp.dto.ZoneLiteDTO;
import com.mjvs.jgsp.model.Zone;

import java.util.List;
import java.util.stream.Collectors;

public class ZoneConverter
{
    public static List<ZoneLiteDTO> ConvertZonesToZoneLiteDTOs(List<Zone> zones)
    {
        return zones.stream()
            .map(x -> new ZoneLiteDTO(x.getName())).collect(Collectors.toList());
    }
}
