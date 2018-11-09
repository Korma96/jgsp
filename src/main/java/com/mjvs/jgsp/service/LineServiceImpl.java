package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.repository.LineRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LineServiceImpl implements LineService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private LineRepository lineRepository;

    @Override
    public boolean add(Line newLine) {
        Optional<Line> line = lineRepository.findAll().stream()
                .filter(l -> l.getName().equals(newLine.getName())).findFirst();
        if(line.isPresent())
        {
            logger.debug(String.format("Line %s already exists.", newLine.getName()));
            return false;
        }

        try
        {
            lineRepository.save(newLine);
            return true;
        }
        catch (Exception ex)
        {
            logger.error(String.format("Error adding new line with name %s! Message: %s"
                    , newLine.getName(), ex.getMessage()));
        }

        return false;
    }

    @Override
    public List<Line> getAll() {
        return lineRepository.findAll();
    }

    @Override
    public List<Stop> getLineStops(String lineName) {
        Line line = lineRepository.findByName(lineName);
        if(line == null) {
            return null;
        }

        return line.getStops();
    }

    @Override
    public boolean update(String oldLineName, String newLineName) {
        return false;
    }

    @Override
    public boolean delete(String lineName) {
        Optional<Line> line = lineRepository.findAll().stream()
                .filter(l -> l.getName().equals(lineName)).findFirst();
        if(!line.isPresent()){
            // TO DO: Need to be logged...
            return false;
        }

        try {
            lineRepository.delete(line.get());
            return true;
        }catch (Exception ex) {
            // TO DO: Need to be logged...
            return false;
        }
    }

}
