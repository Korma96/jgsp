package com.mjvs.jgsp.unit_tests.helpers.converter;

import com.mjvs.jgsp.dto.BaseDTO;
import com.mjvs.jgsp.dto.BaseLiteDTO;
import com.mjvs.jgsp.helpers.converter.LineConverter;
import com.mjvs.jgsp.model.Line;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class LineConverterTests
{
    @Test
    public void ConvertLinesToLineDTOs_ListWithValidLines_ReturnsConvertedLines()
    {
        // Arrange
        List<Line> lines = new ArrayList<Line>()
        {{
            add(new Line("line1"));
            add(new Line("line2"));
            add(new Line("line3"));
        }};

        // Act
        List<BaseDTO> baseDTOS = LineConverter.ConvertLinesToBaseDTOs(lines);

        // Assert
        assertEquals(3, baseDTOS.size());
        assertThat(baseDTOS.get(0), isA(BaseDTO.class));
        assertEquals(lines.get(0).getName(), baseDTOS.get(0).getName());
        assertEquals(lines.get(1).getName(), baseDTOS.get(1).getName());
        assertEquals(lines.get(2).getName(), baseDTOS.get(2).getName());
    }

    @Test
    public void ConvertLinesToLineDTOs_ListWithInvalidLines_ReturnsConvertedLines()
    {
        // Arrange
        List<Line> lines = new ArrayList<Line>()
        {{
            add(new Line(null));
            add(new Line(null));
            add(new Line(null));
        }};

        // Act
        List<BaseDTO> baseDTOS = LineConverter.ConvertLinesToBaseDTOs(lines);

        // Assert
        assertEquals(3, baseDTOS.size());
        assertThat(baseDTOS.get(0), isA(BaseDTO.class));
        assertEquals(lines.get(0).getName(), baseDTOS.get(0).getName());
        assertEquals(lines.get(1).getName(), baseDTOS.get(1).getName());
        assertEquals(lines.get(2).getName(), baseDTOS.get(2).getName());
    }

    @Test
    public void ConvertLinesToLineDTOs_EmptyList_ReturnsEmptyList()
    {
        // Arrange
        List<Line> lines = new ArrayList<>();

        // Act
        List<BaseDTO> baseDTOS = LineConverter.ConvertLinesToBaseDTOs(lines);

        // Assert
        assertEquals(0, baseDTOS.size());
    }

    @Test
    public void ConvertLinesToLineLiteDTOs_ListWithValidLines_ReturnsConvertedLines()
    {
        // Arrange
        List<Line> lines = new ArrayList<Line>()
        {{
            add(new Line("line1"));
            add(new Line("line2"));
            add(new Line("line3"));
        }};

        // Act
        List<BaseLiteDTO> baseLiteDTOs = LineConverter.ConvertLinesToBaseLiteDTOs(lines);

        // Assert
        assertEquals(3, baseLiteDTOs.size());
        assertThat(baseLiteDTOs.get(0), isA(BaseLiteDTO.class));
        assertEquals(lines.get(0).getName(), baseLiteDTOs.get(0).getName());
        assertEquals(lines.get(1).getName(), baseLiteDTOs.get(1).getName());
        assertEquals(lines.get(2).getName(), baseLiteDTOs.get(2).getName());
    }

    @Test
    public void ConvertLinesToLineLiteDTOs_EmptyList_ReturnsEmptyList()
    {
        // Arrange
        List<Line> lines = new ArrayList<>();

        // Act
        List<BaseLiteDTO> baseLiteDTOs = LineConverter.ConvertLinesToBaseLiteDTOs(lines);

        // Assert
        assertEquals(0, baseLiteDTOs.size());
    }
}
