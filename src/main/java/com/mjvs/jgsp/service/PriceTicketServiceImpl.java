package com.mjvs.jgsp.service;

import com.mjvs.jgsp.dto.PriceTicketDTO;
import com.mjvs.jgsp.dto.PriceTicketFrontendDTO;
import com.mjvs.jgsp.helpers.exception.PriceTicketNotFoundException;
import com.mjvs.jgsp.repository.ZoneRepository;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mjvs.jgsp.model.PassengerType;
import com.mjvs.jgsp.model.PriceTicket;
import com.mjvs.jgsp.model.TicketType;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.repository.PriceTicketRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PriceTicketServiceImpl implements PriceTicketService {

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private PriceTicketRepository priceTicketRepository;

	@Autowired
	private ZoneRepository zoneRepository;

	@Override
	public PriceTicket getLatestPriceTicket(PassengerType passengerType, TicketType ticketType, Zone zone) throws PriceTicketNotFoundException {
		PriceTicket priceTicket = priceTicketRepository.findTopByPassengerTypeAndTicketTypeAndZoneOrderByDateFromDesc(passengerType, ticketType, zone);
		if (priceTicket == null) {
			String message = "PriceTicket(passengerType=" + passengerType.name()
					+ ", ticketType=" + ticketType + ", lineZoneId=" + zone.getId() + ") was not found in database.";
			logger.error(message);
			throw new PriceTicketNotFoundException(message);
		}

		return priceTicket;
	}

	@Override
	public void save(PriceTicket priceTicket) {
		priceTicketRepository.save(priceTicket);
	}

	@Override
	public boolean addTicket(PriceTicketDTO priceTicketDTO) {
		PriceTicket priceTicket;
		System.out.println("hej");

		List<PriceTicket> priceTicketList = this.priceTicketRepository.findAll();

		for (int i = 0; i < priceTicketList.size(); i++) {

			if(priceTicketList.get(i).isDeleted()){
				continue;
			}

			/*if (priceTicketDTO.getDateFrom().equals(priceTicketList.get(i).getDateFrom()) &&
					priceTicketDTO.getPassengerType().equals(priceTicketList.get(i).getPassengerType()) &&
					priceTicketDTO.getTicketType().equals(priceTicketList.get(i).getTicketType()) &&
					priceTicketDTO.getId_zone() == priceTicketList.get(i).getZone().getId()) {
				return false;
			}*/

			if (priceTicketDTO.getPassengerType().equals(priceTicketList.get(i).getPassengerType()) &&
					priceTicketDTO.getTicketType().equals(priceTicketList.get(i).getTicketType()) &&
					priceTicketDTO.getZone().equalsIgnoreCase(priceTicketList.get(i).getZone().getName())) {
				System.out.println("ovde");
				return false;
			}

		}


		Zone zone = this.zoneRepository.findByName(priceTicketDTO.getZone());
		if (zone == null) {
			return false;
		}

		System.out.println("Asfasfasf");


		try {
			priceTicket = new PriceTicket(priceTicketDTO.getDateFrom(), priceTicketDTO.getPassengerType(), priceTicketDTO.getTicketType(),
					priceTicketDTO.getPriceLine(), priceTicketDTO.getPriceZone(), zone);
			this.priceTicketRepository.save(priceTicket);
		} catch (Exception e) {
			e.printStackTrace();
		}
			return true;
		}

		@Override
		public List<PriceTicketFrontendDTO> getAllPriceTickets(){
			List<PriceTicket> priceTickets =  this.priceTicketRepository.findAll();
			List<PriceTicketFrontendDTO> priceTicketFrontendDTOS = new ArrayList<PriceTicketFrontendDTO>();

			for(int i = 0;i<priceTickets.size(); i++){
				if(priceTickets.get(i).isDeleted()){
					continue;
				}

				PriceTicketFrontendDTO priceTicketFrontendDTO = new PriceTicketFrontendDTO(priceTickets.get(i));
				priceTicketFrontendDTOS.add(priceTicketFrontendDTO);
			}
			return priceTicketFrontendDTOS;

		}

		@Override
		public List<PriceTicket> getAll(){
			return this.priceTicketRepository.findAll();
		}

		@Override
		public Map<String,String> delete(Long id){
			PriceTicket priceTicket = this.priceTicketRepository.findById(id);
			Map<String,String> retVal = new HashMap<String, String>();


			if(priceTicket == null){
				retVal.put("message", "Priceticket doensn't exist in base!!!");
				retVal.put("deleted","false");
				return retVal;
			}

			if(priceTicket.isDeleted()){
				retVal.put("message", "The priceticket has already been deleted!!!");
				retVal.put("deleted","false");
				return retVal;
			}

			priceTicket.setDeleted(true);
			this.priceTicketRepository.save(priceTicket);
			retVal.put("message", "The priceticket has been successfully deleted :)");
			retVal.put("deleted","true");
			return retVal;

		}

		@Override
		public Pair update(PriceTicketDTO priceTicketDTO, Long id){
			Zone zone = this.zoneRepository.findByName(priceTicketDTO.getZone());
			boolean changed = false;
			Pair<String,Boolean> retVal = null;

			System.out.println("servis");
			LocalTime localTime = LocalTime.MIDNIGHT;
			LocalDate localDateNow = LocalDate.now();
			LocalDateTime localDateTimeNow = LocalDateTime.of(localDateNow,localTime);
			LocalDateTime fromDTO = LocalDateTime.of(priceTicketDTO.getDateFrom(),localTime);

			System.out.println(localTime.toString());
			System.out.println(localDateNow);
			System.out.println(localDateTimeNow);
			System.out.println(fromDTO);

			if(zone == null) {
                retVal = new Pair<>("Zone doesn't exist in base!!!",false);
                return retVal;
			}

			PriceTicket priceTicket = this.priceTicketRepository.findById(id);

			if(priceTicket == null){
                retVal = new Pair<>("Priceticket doesn't exist in base!!!",false);
                return retVal;
			}

			if(priceTicket.getPriceLine() != priceTicketDTO.getPriceLine()){
				priceTicket.setPriceLine(priceTicketDTO.getPriceLine());
				changed = true;
			}

			if(priceTicket.getPriceZone() != priceTicketDTO.getPriceZone()){
				priceTicket.setPriceZone(priceTicketDTO.getPriceZone());
				changed = true;
			}

			if(!priceTicket.getDateFrom().equals(priceTicketDTO.getDateFrom())){


				long dif = computeSubtractTwoDate(localDateTimeNow,fromDTO);
				System.out.println("ovde");

				if(dif>=0){
				    priceTicket.setDateFrom(priceTicketDTO.getDateFrom());
				    changed=true;
				    System.out.println("datum");
                }else{
				    if(changed){
                        retVal = new Pair<>("Datefrom isn't updated, but the rest is.",true);
                        return retVal;
                    }
                }
			}


			if(changed){
			    this.priceTicketRepository.save(priceTicket);
                retVal = new Pair<>("Priceticket is successfully updated :)",true);
            }


            return retVal;
		}


		public long computeSubtractTwoDate(LocalDateTime ldt1, LocalDateTime ldt2) {
		long sub = ChronoUnit.SECONDS.between(ldt1, ldt2);
		return sub;
	}

}
