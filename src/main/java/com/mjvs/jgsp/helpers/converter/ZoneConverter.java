package com.mjvs.jgsp.helpers.converter;

import com.mjvs.jgsp.dto.BaseDTO;
import com.mjvs.jgsp.model.Zone;

import java.util.List;
import java.util.stream.Collectors;

public class ZoneConverter
{
    public static List<BaseDTO> ConvertZonesToZoneDTOs(List<Zone> zones)
    {
        return zones.stream()
            .map(x -> new BaseDTO(x.getId(), x.getName())).collect(Collectors.toList());
    }
}
