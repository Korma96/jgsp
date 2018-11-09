package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.repository.LineRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LineServiceImpl implements LineService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private LineRepository lineRepository;

    @Override
    public boolean add(Line newLine)
    {
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
            logger.info(String.format("Line %s successfully added!", newLine.getName()));
        }
        catch (Exception ex)
        {
            logger.error(String.format("Error adding new line %s message %s",
                    newLine.getName(), ex.getMessage()));
            return false;
        }

        return true;
    }

    @Override
    public List<Line> getAll()
    {
        return lineRepository.findAll();
    }

    @Override
    public List<Stop> getLineStops(String lineName)
    {
        Line line = lineRepository.findByName(lineName);
        if(line == null)
        {
            logger.warn(String.format("Line %s does not exists.", line.getName()));
            return new ArrayList<>();
        }

        return line.getStops();
    }

    @Override
    public boolean update(String oldLineName, String newLineName) {
        return false;
    }

    @Override
    public boolean delete(String lineName)
    {
        Optional<Line> line = lineRepository.findAll().stream()
                .filter(l -> l.getName().equals(lineName)).findFirst();
        if(!line.isPresent()){
            logger.warn(String.format("Line %s does not exists.", lineName));
            return false;
        }

        try
        {
            lineRepository.delete(line.get());
            logger.info("Stop %s successfully deleted!", line.get().getName());
        }
        catch (Exception ex)
        {
            logger.error(String.format("Error deleting line %s message %s",
                    line.get().getName(), ex.getMessage()));
            return false;
        }

        return true;
    }

}
