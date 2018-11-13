package com.mjvs.jgsp.service;

import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.repository.LineRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class LineServiceImpl implements LineService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private LineRepository lineRepository;

    @Override
    public boolean add(String lineName)
    {
        if(exists(lineName))
        {
            logger.debug(String.format("Line %s already exists.", lineName));
            return false;
        }

        try
        {
            lineRepository.save(new Line(lineName));
            logger.info(String.format("Line %s successfully added!", lineName));
        }
        catch (Exception ex)
        {
            logger.error(String.format("Error adding new line %s message %s",
                    lineName, ex.getMessage()));
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
            logger.warn(String.format("Line %s does not exist.", lineName));
            return new ArrayList<>();
        }

        return line.getStops();
    }

    @Override
    public boolean delete(String lineName)
    {
        Line line = lineRepository.findByName(lineName);
        if(line == null){
            logger.warn(String.format("Line %s does not exist.", lineName));
            return false;
        }

        try
        {
            lineRepository.delete(line);
            logger.info("Line %s successfully deleted!", lineName);
        }
        catch (Exception ex)
        {
            logger.error(String.format("Error deleting line %s message %s",
                    lineName, ex.getMessage()));
            return false;
        }

        return true;
    }

    @Override
    public Line getByName(String lineName)
    {
        return  lineRepository.findByName(lineName);
    }

    @Override
    public List<Schedule> getSchedules(String lineName)
    {
        Line line = lineRepository.findByName(lineName);
        if(line == null)
        {
            logger.warn(String.format("Line %s does not exists.", lineName));
            return new ArrayList<>();
        }

        return line.getSchedules();
    }

    @Override
    public boolean rename(HashMap<String, String> data)
    {
        String oldName;
        String newName;

        try
        {
            oldName = data.get("oldName");
            newName = data.get("newName");
        }
        catch (Exception ex)
        {
            logger.error(String.format("Wrong data format! %s ", ex.getMessage()));
            return false;
        }

        Line line = lineRepository.findByName(oldName);
        if(line == null)
        {
            logger.warn(String.format("Line %s does not exist.", oldName));
            return false;
        }

        line.setName(newName);

        try
        {
            lineRepository.save(line);
            logger.info(String.format("Line %s successfully renamed to %s",
                    oldName, newName));
        }
        catch (Exception ex)
        {
            logger.info(String.format("Error renaming line %s to %s",
                    oldName, newName));
            return false;
        }

        return true;
    }

    @Override
    public boolean exists(String lineName)
    {
        Line line = lineRepository.findByName(lineName);
        return line != null;
    }
}
