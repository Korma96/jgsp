package com.mjvs.jgsp.helpers.converter;

import com.mjvs.jgsp.dto.StopDTO;
import com.mjvs.jgsp.model.Stop;

import java.util.List;
import java.util.stream.Collectors;

public class StopConverter {

    public static List<StopDTO> convertStopsToStopDTOs(List<Stop> stops) {
        return stops.stream()
                .map(stop -> new StopDTO(stop))
                .collect(Collectors.toList());
    }
}
