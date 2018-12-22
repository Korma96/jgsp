package com.mjvs.jgsp.unit_tests.helpers.converter;

import com.mjvs.jgsp.dto.BaseDTO;
import com.mjvs.jgsp.helpers.converter.ZoneConverter;
import com.mjvs.jgsp.model.Zone;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.isA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ZoneConverterTests
{
    @Test
    public void ConvertZonesToZoneDTOs_ListWithValidZones_ReturnsConvertedZones()
    {
        // Arrange
        List<Zone> zones = new ArrayList<Zone>()
        {{
          add(new Zone("zone1"));
          add(new Zone("zone2"));
          add(new Zone("zone3"));
        }};

        // Act
        List<BaseDTO> baseDTOS = ZoneConverter.ConvertZonesToZoneDTOs(zones);

        // Assert
        assertEquals(3, baseDTOS.size());
        assertThat(baseDTOS.get(0), isA(BaseDTO.class));
        assertEquals(zones.get(0).getName(), baseDTOS.get(0).getName());
        assertEquals(zones.get(1).getName(), baseDTOS.get(1).getName());
        assertEquals(zones.get(2).getName(), baseDTOS.get(2).getName());
    }

    @Test
    public void ConvertZonesToZoneDTOs_EmptyList_ReturnsEmptyList()
    {
        // Arrange
        List<Zone> zones = new ArrayList<>();

        // Act
        List<BaseDTO> baseDTOS = ZoneConverter.ConvertZonesToZoneDTOs(zones);

        // Assert
        assertEquals(0, baseDTOS.size());
    }
}
