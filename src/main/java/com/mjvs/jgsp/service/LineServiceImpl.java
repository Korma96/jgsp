package com.mjvs.jgsp.service;

import com.mjvs.jgsp.dto.BaseDTO; 
import com.mjvs.jgsp.dto.StopDTO;
import com.mjvs.jgsp.dto.TimesDTO;
import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.converter.LineConverter;
import com.mjvs.jgsp.model.DayType;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.repository.LineRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LineServiceImpl extends ExtendedBaseServiceImpl<Line> implements LineService
{
    private final Logger logger = LogManager.getLogger(this.getClass());
    private LineRepository lineRepository;

    private DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy.");

    @Autowired
    public LineServiceImpl(LineRepository lineRepository)
    {
        super(lineRepository);
        this.lineRepository = lineRepository;
    }

    @Override
    public Result<List<BaseDTO>> getActiveLines()
    {
        try {
            List<BaseDTO> data = LineConverter
                    .ConvertLinesToBaseDTOs(lineRepository.findByActive(true));
            return new Result<>(data);
        }
        catch (Exception ex)
        {
            logger.error(ex.getMessage());
            return new Result<>(null, false, Messages.DatabaseError());
        }
    }

    @Override
    public List<Line> getLines() {
        return lineRepository.findAll();
    }

    @Override
    public List<StopDTO> getSortedStopsById(List<Stop> stops)
    {

        stops.sort(new Comparator<Stop>() { // sortiramo jer u dokumentaciji nije garantovano
            // da cemo dobiti sortirane objekte po id
            @Override
            public int compare(Stop o1, Stop o2) {
                return o1.getId().compareTo(o2.getId());
            }
        });

        return stops.stream()
                .map(stop -> new StopDTO(stop))
                .collect(Collectors.toList());
    }


    @Override
    public List<Schedule> getLatestSchedules(List<Schedule> schedules)
    {
        if(schedules.size() == 0) {
            return new ArrayList<>();
        }

        List<Schedule> latestSchedules = new ArrayList<>();
        Optional<Schedule> optional = schedules.stream().findFirst();
        if(!optional.isPresent()) {
            return latestSchedules;
        }
        LocalDate latestDate = optional.get().getDateFrom();

        for(Schedule schedule : schedules)
        {
            if(schedule.getDateFrom().isBefore(latestDate))
            {
                continue;
            }
            else if(schedule.getDateFrom().isAfter(latestDate))
            {
                latestDate = schedule.getDateFrom();
                latestSchedules = new ArrayList<>();
                latestSchedules.add(schedule);
            }
            else
            {
                latestSchedules.add(schedule);
            }
        }

        return latestSchedules;
    }

	@Override
	public Line findByName(String name) {
		return lineRepository.findByName(name);
	}
	
	public List<String> getLineNames() {
		List<Line> lines = lineRepository.findAll();
		List<String> retVal = new ArrayList<String>();
		String name;
		String lineNum = "";
		for (Line line : lines){
			name = line.getName();
			for (int i = 0; i < name.length(); i++){
				try{
					Integer.parseInt(name.substring(0, i));
				}
				catch (Exception e) {
					lineNum = name.substring(0,i-1);
				}
				
			}
			retVal.add(lineNum);
		}
		
		return retVal;
	}


	@Override
    public ArrayList<TimesDTO> getDepartureLists(String dateStr, String dayStr, String[] lines) {
        LocalDate date = LocalDate.parse(dateStr, dateFormatter);
        DayType day = DayType.valueOf(dayStr.toUpperCase());

        ArrayList<TimesDTO> timesDTOs = new ArrayList<TimesDTO>();

        Line lineA, lineB;
        TimesDTO timesDTO = null;
        String[] sufixes = {"A", "B"};
        List<String> timesA, timesB;

        for (String lineStr : lines) {
            try {
                timesDTO = new TimesDTO();
                lineA = lineRepository.findByNameAndDeleted(lineStr+sufixes[0], false);
                lineB = lineRepository.findByNameAndDeleted(lineStr+sufixes[1], false);
                //if(lineA == null && lineB == null) return null;

                if(lineA == null) timesDTO.setTimesA(new ArrayList<>());
                else {
                    timesA = lineA.getSchedules().stream()
                            .filter(schedule -> schedule.getDateFrom().equals(date) && schedule.getDayType() == day)
                            .map(schedule -> schedule.getDepartureList()).findFirst().get()
                            .stream()
                            .map(myLocalTime -> myLocalTime.getTime().format(timeFormatter))
                            .collect(Collectors.toList());
                    timesDTO.setTimesA(timesA);
                }

                if(lineB == null) timesDTO.setTimesB(new ArrayList<>());
                else {
                    timesB = lineB.getSchedules().stream()
                            .filter(schedule -> schedule.getDateFrom().equals(date))
                            .map(schedule -> schedule.getDepartureList()).findFirst().get()
                            .stream()
                            .map(myLocalTime -> myLocalTime.getTime().format(timeFormatter))
                            .collect(Collectors.toList());
                    timesDTO.setTimesB(timesB);
                }
            } catch (Exception e) {
                if(timesDTO == null) timesDTO = new TimesDTO();

                timesDTO.setTimesA(new ArrayList<>());
                timesDTO.setTimesB(new ArrayList<>());
            }

            timesDTOs.add(timesDTO);
        }

        return timesDTOs;
    }

    public void checkIfLineCanBeActive(Line line)
    {
        // must have zone
        if(line.getZone() == null){
            if(line.isActive()){
                line.setActive(false);
            }
            return;
        }

        // minutes required for whole route must be greater than 0
        if(line.getMinutesRequiredForWholeRoute() <= 0){
            if(line.isActive()){
                line.setActive(false);
            }
            return;
        }

        // must have at least two stops
        if(line.getStops().size() < 2){
            if(line.isActive()){
                line.setActive(false);
            }
            return;
        }
        // must have at least one schedule
        if(line.getSchedules().size() == 0){
            if(line.isActive()){
                line.setActive(false);
            }
            return;
        }

        List<Schedule> latestSchedules = getLatestSchedules(line.getSchedules());
        /*
        // must have latest schedules for all DayTypes
        if(latestSchedules.stream().noneMatch(x -> x.getDayType() == DayType.WORKDAY) ||
           latestSchedules.stream().noneMatch(x -> x.getDayType() == DayType.SATURDAY) ||
           latestSchedules.stream().noneMatch(x -> x.getDayType() == DayType.SUNDAY)){
            if(line.isActive()){
                line.setActive(false);
            }
            return;
        }
        */
        // must have at least one departure time in latest schedules
        for(Schedule s : latestSchedules){
            if(s.getDepartureList().size() == 0){
                if(line.isActive()){
                    line.setActive(false);
                }
                return;
            }
        }

        if(!line.isActive()){
            line.setActive(true);
        }
    }
}