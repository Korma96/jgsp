package com.mjvs.jgsp.helpers.converter;

import com.mjvs.jgsp.dto.BaseDTO;
import com.mjvs.jgsp.dto.BaseLiteDTO;
import com.mjvs.jgsp.model.Line;

import java.util.List;
import java.util.stream.Collectors;

public class LineConverter
{
    public static List<BaseLiteDTO> ConvertLinesToBaseLiteDTOs(List<Line> lines)
    {
        return lines.stream()
                .map(x -> new BaseLiteDTO(x.getName())).collect(Collectors.toList());
    }

    public static List<BaseDTO> ConvertLinesToBaseDTOs(List<Line> lines)
    {
        return lines.stream()
                .map(x -> new BaseDTO(x.getId(), x.getName())).collect(Collectors.toList());
    }
}
