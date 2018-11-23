package com.mjvs.jgsp.helpers.converter;

import com.mjvs.jgsp.dto.LineDTO;
import com.mjvs.jgsp.dto.LineLiteDTO;
import com.mjvs.jgsp.model.Line;

import java.util.List;
import java.util.stream.Collectors;

public class LineConverter
{
    public static List<LineLiteDTO> ConvertLinesToLineLiteDTOs(List<Line> lines)
    {
        return lines.stream()
                .map(x -> new LineLiteDTO(x.getName())).collect(Collectors.toList());
    }

    public static List<LineDTO> ConvertLinesToLineDTOs(List<Line> lines)
    {
        return lines.stream()
                .map(x -> new LineDTO(x.getId(), x.getName())).collect(Collectors.toList());
    }
}
