package com.mjvs.jgsp.simulation.service;

import com.mjvs.jgsp.helpers.exception.BadRequestException;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.service.LineService;
import com.mjvs.jgsp.simulation.model.SubscribedLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SimulationServiceImpl implements SimulationService {

    @Autowired
    private LineService lineService;

    @Autowired
    private List<SubscribedLine> linesShowPositions;


    @Transactional
    @Override
    public void addFavoriteLine(Long id) throws Exception {
        for (SubscribedLine subscribedLine : linesShowPositions) {
            if(subscribedLine.getLine().getId() == id) {
                subscribedLine.setNumOfSubscribers(subscribedLine.getNumOfSubscribers() + 1);
                return;
            }
        }

        Line line = lineService.findById(id).getData();
        linesShowPositions.add(new SubscribedLine(line, 3));
    }

    @Transactional
    @Override
    public void removeFavoriteLine(Long id) throws Exception {
        int indexForRemove = -1;
        SubscribedLine subscribedLine;

        for (int i = 0; i < linesShowPositions.size(); i++) {
            subscribedLine = linesShowPositions.get(i);

            if(subscribedLine.getLine().getId() == id) {
                if(subscribedLine.getNumOfSubscribers() == 1) {
                    indexForRemove = i;
                    break;
                }
                else {
                    subscribedLine.setNumOfSubscribers(subscribedLine.getNumOfSubscribers() - 1);
                    return;
                }

            }
        }

        if(indexForRemove > -1) {
            linesShowPositions.remove(indexForRemove);
        }
        else throw new BadRequestException();

    }
}
