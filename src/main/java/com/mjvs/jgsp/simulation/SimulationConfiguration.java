package com.mjvs.jgsp.simulation;

import com.mjvs.jgsp.simulation.model.SubscribedLine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class SimulationConfiguration {

    @Bean
    public List<SubscribedLine> linesShowPositions() {
        final List<SubscribedLine> lines = new ArrayList<>();
        return lines;
    }
}
