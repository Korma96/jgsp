package com.mjvs.jgsp.controller;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;

import com.mjvs.jgsp.model.Zone;
import com.mjvs.jgsp.repository.ZoneRepository;
import com.mjvs.jgsp.service.LineService;
import com.mjvs.jgsp.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.repository.LineRepository;
import com.mjvs.jgsp.repository.StopRepository;

@RestController
@RequestMapping(value = "/vuk_popunjava_bazu")
public class VukovController {

	@Autowired
	private StopRepository stopRepository;

	@Autowired
	private LineService lineService;

	@Autowired
	private ZoneRepository zoneRepository;

	@RequestMapping(method = RequestMethod.GET)
	public void databaseFill()
    {	
    	String myDirectoryPath = "/stops";
		File dir = null;
		try {
			dir = new File(URLDecoder.decode(getClass().getResource(myDirectoryPath).getFile(),"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		File[] directoryListing = dir.listFiles();
    	System.out.println("sssssssssssssssssssssssssssssssssssssssssss");
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
}
