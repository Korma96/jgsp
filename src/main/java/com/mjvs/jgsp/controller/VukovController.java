package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.model.DayType;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.MyLocalTime;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.repository.MyLocalTImeRepository;
import com.mjvs.jgsp.repository.StopRepository;
import com.mjvs.jgsp.repository.ZoneRepository;
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

@RestController
@RequestMapping(value = "/vuk_popunjava_bazu")
public class VukovController {

	@Autowired
	private StopRepository stopRepository;
	
	@Autowired
	private MyLocalTImeRepository MyLocalTImeRepository;

	@Autowired
	private LineService lineService;

	@Autowired
	private ZoneRepository zoneRepository;
	
	@Autowired
	private ScheduleService scheduleService;

	@RequestMapping(value = "1" , method = RequestMethod.POST)
	public void databaseFill() throws Exception
    {	
    	String myDirectoryPath = "/stops";
		File dir = null;
		try {
			dir = new File(URLDecoder.decode(getClass().getResource(myDirectoryPath).getFile(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		File[] directoryListing = dir.listFiles();
    	if (directoryListing != null) 
    	{
    		Zone zone = zoneRepository.findByName("prva");
    		if(zone == null) zone = zoneRepository.save(zone);

    		for (File child : directoryListing)
    		{
    			try {
					String readline = "";
					BufferedReader br = new BufferedReader(new FileReader(child));
					Line line = new Line(child.getName().substring(25).split("\\.")[0], zone);
					ArrayList<Stop> stops = new ArrayList<Stop>();
					String name;
					double lat, lng;
					String[] tokens;
					while ((readline = br.readLine())!=null){
						tokens = readline.split("\\|");
						lat = Double.parseDouble(tokens[0]);
						lng = Double.parseDouble(tokens[1]);
						name = tokens[2];
						Stop stop = stopRepository.findByLatitudeAndLongitude(lat, lng);
						if (stop == null) stop = stopRepository.save(new Stop(lat, lng, name));
						if(!stops.contains(stop)) stops.add(stop);
					}
					line.setStops(stops);
					lineService.save(line);

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
    		}
    	}
    	else
    	{
    	   System.out.println("*****************************************************************************Error directory");
    	}
    	System.out.println("Zavrsio");
    }
	
	@RequestMapping(value= "2", method = RequestMethod.POST)
	public void databaseFill2() throws Exception
    {
		String myDirectoryPath = "/schedules";
		File dir = null;
		try {
			dir = new File(URLDecoder.decode(getClass().getResource(myDirectoryPath).getFile(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		File[] directoryListing = dir.listFiles();
    	if (directoryListing != null) 
    	{
    		for (File child : directoryListing)
    		{
    			try {
					String readline = "";
					BufferedReader br = new BufferedReader(new FileReader(child));
					Line line = lineService.findByName(child.getName().split("\\.")[0]);
					String[] tokens;
					Schedule schedule = new Schedule();
					while ((readline = br.readLine())!=null){
						tokens = readline.split(" ");
						
						for (String token: tokens){
							LocalTime time = LocalTime.parse(token);
						    MyLocalTime mlt = MyLocalTImeRepository.findByTime(time);
						if (mlt == null) mlt = MyLocalTImeRepository.save(new MyLocalTime(time));
						if(!schedule.getDepartureList().contains(mlt)) schedule.getDepartureList().add(mlt);
						}
					}
					schedule.setDayType(DayType.WORKDAY);
					String str = "2018-09-01";
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate dateTime = LocalDate.parse(str, formatter);
					schedule.setDateFrom(dateTime);
					scheduleService.save(schedule);
					line.getSchedules().add(schedule);
					lineService.save(line);

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
    		}
    	}
    	else
    	{
    	   System.out.println("*****************************************************************************Error directory");
    	}
    	System.out.println("Zavrsio");
    }
}
	
    

