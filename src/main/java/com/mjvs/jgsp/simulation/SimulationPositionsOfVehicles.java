package com.mjvs.jgsp.simulation;

import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.simulation.model.SubscribedLine;
import com.mjvs.jgsp.simulation.websockets.messaging.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

// @Controller
@Component
public class SimulationPositionsOfVehicles {

    @Autowired
    private Producer producer;


    @Autowired
    private List<SubscribedLine> linesShowPositions;


    /*
     * Logika pocinje da se izvrsava u fiksnim intervalima sa odlozenim
     * pocetkom izvrsavanja prilikom pokretanja aplikacije.
     * <code>fixedRate</code> se koristi kao indikacija u kojem intervalu
     * ce se pozivati metoda. <code>initialDelay</code> se koristi kao indikacija
     * koliko posle pokretanja aplikacije treba da se ceka do prvog pokretanja metode.
     */
    @Scheduled(
            initialDelayString = "2000",  // 2 sec
            fixedRateString = "10000")  // 10 sec
    //@Transactional // Dok nisam stavio @Transactional bacao mi je org.hibernate.LazyInitializationException
                   // i nisam mogao pristupiti kolekcijama bilo koje linije, jer su sve podesene na lazy.
                    // Problem je sto je iz nekog razloga session za dobavljanje lazy kolekcija bude zatvorena,
                    // a nakon sto sam stavio ovo @Transactional, uvek je otvorena
    public void fixedRateJobWithInitialDelay() {
        //List<Line> lines = lineService.getLines();


        //lines.parallelStream().forEach(line -> producer.sendMessageTo(line));
        linesShowPositions.parallelStream().forEach(line -> producer.sendMessageTo(line));

    }
}
