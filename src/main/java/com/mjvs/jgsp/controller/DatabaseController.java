package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.model.*;
import com.mjvs.jgsp.repository.*;
import com.mjvs.jgsp.service.LineService;
import com.mjvs.jgsp.service.ScheduleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/vuk_popunjava_bazu")
public class DatabaseController {

	@Autowired
	private StopRepository stopRepository;
	
	@Autowired
	private MyLocalTImeRepository myLocalTImeRepository;

	@Autowired
	private LineService lineService;

	@Autowired
	private ZoneRepository zoneRepository;

	@Autowired
	private ScheduleService scheduleService;



	@Autowired
	private PriceTicketRepository priceTicketRepository;

	@Autowired
	private PointRepository pointRepository;


	@RequestMapping(value = "/stanice" , method = RequestMethod.GET)
	public void databaseFill() throws Exception
    {	
    	String staniceDirectoryPath = "/sortirane_stanice";
		File dirStanice = null;

		String tackeDirectoryPath = "/tacke";
		File dirTacke = null;

		try {
			dirStanice = new File(URLDecoder.decode(getClass().getResource(staniceDirectoryPath).getFile(),"UTF-8"));
			dirTacke = new File(URLDecoder.decode(getClass().getResource(tackeDirectoryPath).getFile(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			System.out.println("errorrrrrrrrrrrrrrrr");
			e.printStackTrace();
		}
		File[] staniceDirectoryListing = dirStanice.listFiles();
		File[] tackeDirectoryListing = dirTacke.listFiles();
    	System.out.println("sssssssssssssssssssssssssssssssssssssssssss");
    	
    	if (staniceDirectoryListing != null)
    	{
    		Line line;
    		Zone zone;
    		Stop stop;
    		Point point;
			ArrayList<Stop> stops;
			ArrayList<Point> points;
			BufferedReader br = null;
			File subDirectoryStanice, subDirectoryTacke, childStanice, childTacke;

    		for (int i = 0; i < staniceDirectoryListing.length; i++) {
    			subDirectoryStanice = staniceDirectoryListing[i];
    			subDirectoryTacke = tackeDirectoryListing[i];
    			zone = new Zone(subDirectoryStanice.getName(), TransportType.BUS);
    			
    			for (int j = 0; j < subDirectoryStanice.listFiles().length; j++) {
    				try {
						childStanice = subDirectoryStanice.listFiles()[j];
						childTacke = subDirectoryTacke.listFiles()[j];

    					String readline = "";
    					br = new BufferedReader(new FileReader(childStanice));
    					line = new Line(childStanice.getName().substring(25).split("\\.")[0], zone, 45);
    					System.out.println(childStanice.getName().substring(25).split("\\.")[0]);
    					stops = new ArrayList<Stop>();
    					String name;
    					double lat, lng;
    					String[] tokens;
    					while ((readline = br.readLine())!=null){
    						tokens = readline.split("\\|");
    						lat = Double.parseDouble(tokens[0]);
    						lng = Double.parseDouble(tokens[1]);
    						name = tokens[2];
    						stop = stopRepository.findByLatitudeAndLongitude(lat, lng);
    						if (stop == null) stop = stopRepository.save(new Stop(lat, lng, name));
    						if(!stops.contains(stop)) stops.add(stop);
    					}
    					line.setStops(stops);


						br = new BufferedReader(new FileReader(childTacke));
						points = new ArrayList<Point>();
						while ((readline = br.readLine())!=null){
							tokens = readline.split("\\|");
							lat = Double.parseDouble(tokens[0]);
							lng = Double.parseDouble(tokens[1]);
							point = pointRepository.findByLatitudeAndLongitude(lat, lng);
							if (point == null) point = pointRepository.save(new Point(lat, lng));
							if(!points.contains(point)) points.add(point);
						}
						line.setPoints(points);
    					
    					//lineService.save(line);
    					
    					//if (zone.getLines()== null) zone.setLines(new ArrayList<>());
    					
    					zone.getLines().add(line);

    				} catch (NumberFormatException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (FileNotFoundException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				} catch (IOException e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
					finally {
						if(br != null) br.close();
					}
				}
    			
    			zoneRepository.save(zone);
    		}
    	}
    	else
    	{
    	   System.out.println("*****************************************************************************Error directory");
    	}
    	System.out.println("Zavrsio");
    }
	
	@RequestMapping(value= "/schedules", method = RequestMethod.GET)
	public void databaseFill2() throws Exception
    {
		String myDirectoryPath = "/redovi_voznje";
		File dir = null;
		try {
			dir = new File(URLDecoder.decode(getClass().getResource(myDirectoryPath).getFile(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		File[] directoryListing = dir.listFiles();
    	if (directoryListing != null) 
    	{
    		MyLocalTime mlt;
			BufferedReader br = null;
			LocalTime time;
			String dateFromStr = "01.12.2018.";
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");
			LocalDate dateFrom = LocalDate.parse(dateFromStr, formatter);
			List<MyLocalTime> departureList;
			Line line;
			DayType dayType;
			Schedule schedule;
    		
    		for (File subDir: directoryListing)
    		{
    			dayType = DayType.WORKDAY;
				if (subDir.getName().equals("red_voznje_subota")) dayType = DayType.SATURDAY;
				else if (subDir.getName().equals("red_voznje_nedelja")) dayType = DayType.SUNDAY;

	    		for (File child : subDir.listFiles())
	    		{
	    			
	    			departureList =  new ArrayList<MyLocalTime>();

	    			try
	    			{
						String readline = "";
						br = new BufferedReader(new FileReader(child));
						System.out.println(child.getName().split("\\.")[0]);
						line = lineService.findByName(child.getName().split("\\.")[0]);

						String[] tokens;

						while ((readline = br.readLine())!=null)
						{
							tokens = readline.split(" ");
							for (String token: tokens)
							{
								time = LocalTime.parse(token);
								time = time.plusMinutes(15);
							    mlt = myLocalTImeRepository.findByTime(time);
								if (mlt == null) mlt = myLocalTImeRepository.save(new MyLocalTime(time));
								departureList.add(mlt);
							}
						}
						schedule = new Schedule(dayType,dateFrom,departureList, line);
						//line.getSchedules().add(schedule);

						//lineService.save(line);
						scheduleService.save(schedule);

					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					finally {
	    				if(br != null) br.close();
					}
	    		}
    		}
    	}
    	else
    	{
    	   System.out.println("*****************************************************************************Error directory");
    	}
    	System.out.println("Zavrsio");
    }

    @RequestMapping(value = "aaa", method = RequestMethod.GET)
	public void aaa() {
		PriceTicket priceTicket1 = new PriceTicket(LocalDate.now(), PassengerType.OTHER, TicketType.MONTHLY,
				1500, 3500, new Zone("sedma zona", TransportType.BUS));
		priceTicketRepository.save(priceTicket1);
	}

}
	
    

