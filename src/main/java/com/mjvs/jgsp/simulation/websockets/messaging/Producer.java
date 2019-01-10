package com.mjvs.jgsp.simulation.websockets.messaging;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;


import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.simulation.model.SubscribedLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/*
 * Izdvojena komponenta koja sadrzi atribut SimpMessagingTemplate
 * cije metode vrse slanje poruka sa servera na pretplacene klijente.
 */
@Component
public class Producer {

	private static final SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
	
	/*
	 * Implementira SimpMessagesSendingOperations klasu koja sadrzi metode za slanje poruka korisnicima.
	 */
	@Autowired
	private SimpMessagingTemplate template;


	//@Async
	@Transactional // Dok nisam stavio @Transactional bacao mi je org.hibernate.LazyInitializationException
	// i nisam mogao pristupiti kolekcijama bilo koje linije, jer su sve podesene na lazy.
	// Problem je sto je iz nekog razloga session za dobavljanje lazy kolekcija bude zatvorena,
	// a nakon sto sam stavio ovo @Transactional, uvek je otvorena
	public void sendMessageTo(SubscribedLine subscribedLine) {
		Line line = subscribedLine.getLine();
		System.out.println("pocelo slanje poruke za line: " + line.getName());

		Stop stop;
		List<Stop> stops = line.getStops();
		int stopsSize = stops.size();

		StringBuilder sb = new StringBuilder("");
		int currentIndex = subscribedLine.getIndexOfStopForFirstVehicle();
		for(int i = 0; i < subscribedLine.getNumOfVehicles(); i++) {
			stop = stops.get(currentIndex % stopsSize);
			sb.append(";");
			sb.append(stop.getLatitude());
			sb.append(",");
			sb.append(stop.getLongitude());
			currentIndex += 5;
		}

		subscribedLine.setIndexOfStopForFirstVehicle(subscribedLine.getIndexOfStopForFirstVehicle() + 1);

		/*sb.append("[");
		sb.append(dateFormatter.format(new Date()));
		sb.append("] ");
		sb.append(message);*/

		// metoda convertAndSend() vrsi slanje poruke sa servera na klijente koji
		// su se pretplatili na odredjeni topic.
		System.out.println("Line name: " + line.getName() +" | " + sb.substring(1));
		System.out.println("zavrseno slanje poruke za line: " + line.getName());
		this.template.convertAndSend("/topic/" + line.getId(), sb.substring(1));
	}

}
