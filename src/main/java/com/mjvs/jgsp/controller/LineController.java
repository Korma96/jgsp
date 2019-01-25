package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.dto.*;
import com.mjvs.jgsp.helpers.Messages;
import com.mjvs.jgsp.helpers.ResponseHelpers;
import com.mjvs.jgsp.helpers.Result;
import com.mjvs.jgsp.helpers.StringConstants;
import com.mjvs.jgsp.helpers.converter.LineConverter;
import com.mjvs.jgsp.helpers.converter.StopConverter;
import com.mjvs.jgsp.helpers.exception.BadRequestException;
import com.mjvs.jgsp.helpers.exception.DatabaseException;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Point;
import com.mjvs.jgsp.model.Schedule;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.repository.PointRepository;
import com.mjvs.jgsp.service.LineService;
import com.mjvs.jgsp.service.ScheduleService;
import com.mjvs.jgsp.service.StopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/line")
public class LineController extends ExtendedBaseController<Line>
{
    private LineService lineService;
    private StopService stopService;
    private ScheduleService scheduleService;
    private PointRepository pointRepository;

    @Autowired
    public LineController(LineService lineService, StopService stopService,
                          ScheduleService scheduleService, PointRepository pointRepository)
    {
        super(lineService);
        this.lineService = lineService;
        this.stopService = stopService;
        this.scheduleService = scheduleService;
        this.pointRepository = pointRepository;
    }

    @PreAuthorize("hasAuthority('TRANSPORT_ADMINISTRATOR')")
    @RequestMapping(value = "/stop/add", method = RequestMethod.POST)
    public ResponseEntity addStopToLine(@RequestBody LineWithStopLiteDTO lineWithStopDTO) throws Exception
    {
        Result<Line> lineResult = lineService.findById(lineWithStopDTO.getLineId());
        if(!lineResult.hasData()) {
            throw new BadRequestException(lineResult.getMessage());
        }

        Result<Stop> stopResult = stopService.findById(lineWithStopDTO.getStopId());
        if(!stopResult.hasData()) {
            throw new BadRequestException(stopResult.getMessage());
        }

        Line line = lineResult.getData();
        Stop stop = stopResult.getData();
        if(line.getStops().contains(stop)) {
            throw new BadRequestException(Messages.AlreadyContains(
                    StringConstants.Line, line.getId(), StringConstants.Stop, stop.getId()));
        }

        int index = lineWithStopDTO.getPosition();
        if(index < 1 || index > (line.getStops().size() + 1)){
            throw new BadRequestException("Position is out of bounds!");
        }

        List<Stop> lineStops = line.getStops();
        lineStops.add(index - 1, stop);
        line.setStops(lineStops);

        lineService.checkIfLineCanBeActive(line);

        Result<Boolean> saveResult = lineService.save(line);
        if(saveResult.isFailure()) {
            throw new DatabaseException(saveResult.getMessage());
        }

        return ResponseHelpers.getResponseData(saveResult);
    }

    @PreAuthorize("hasAuthority('TRANSPORT_ADMINISTRATOR')")
    @RequestMapping(value = "/stop/remove", method = RequestMethod.POST)
    public ResponseEntity removeStopFromLine(@RequestBody LineWithStopLiteDTO lineWithStopLiteDTO) throws Exception
    {
        Result<Line> lineResult = lineService.findById(lineWithStopLiteDTO.getLineId());
        if(!lineResult.hasData()) {
            throw new BadRequestException(lineResult.getMessage());
        }

        Result<Stop> stopResult = stopService.findById(lineWithStopLiteDTO.getStopId());
        if(!stopResult.hasData()) {
            throw new BadRequestException(stopResult.getMessage());
        }

        Line line = lineResult.getData();
        Stop stop = stopResult.getData();
        if(!line.getStops().contains(stop)) {
            throw new BadRequestException(Messages.DoesNotContain(
                    StringConstants.Line, line.getId(), StringConstants.Stop, stop.getId()));
        }

        List<Stop> lineStops = line.getStops();
        lineStops.remove(stop);
        line.setStops(lineStops);

        lineService.checkIfLineCanBeActive(line);

        Result<Boolean> saveResult = lineService.save(line);
        if(saveResult.isFailure()) {
            throw new DatabaseException(saveResult.getMessage());
        }

        return ResponseHelpers.getResponseData(saveResult);
    }

    @PreAuthorize("hasAuthority('TRANSPORT_ADMINISTRATOR')")
    @RequestMapping(value = "/schedule/add", method = RequestMethod.POST)
    public ResponseEntity addScheduleToLine(LineWithScheduleDTO lineWithScheduleDTO) throws Exception
    {

        Result<Line> lineResult = lineService.findById(lineWithScheduleDTO.getLineId());
        if(!lineResult.hasData()) {
            throw new BadRequestException(lineResult.getMessage());
        }

        Result<Schedule> scheduleResult = scheduleService.findById(lineWithScheduleDTO.getScheduleId());
        if(!scheduleResult.hasData()) {
            throw new BadRequestException(scheduleResult.getMessage());
        }

        Line line = lineResult.getData();
        Schedule schedule = scheduleResult.getData();
        if(line.getSchedules().contains(schedule)) {
            throw new BadRequestException(Messages.AlreadyContains(
                    StringConstants.Line, line.getId(), StringConstants.Schedule, schedule.getId()));
        }

        List<Schedule> lineSchedules = line.getSchedules();
        lineSchedules.add(schedule);
        line.setSchedules(lineSchedules);

        lineService.checkIfLineCanBeActive(line);

        Result<Boolean> saveResult = lineService.save(line);
        if(saveResult.isFailure()) {
            throw new DatabaseException(saveResult.getMessage());
        }

        return ResponseHelpers.getResponseData(saveResult);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseEntity<List<BaseDTO>> getAll()
    {
        Result<List<Line>> getResult = lineService.getAll();
        if(getResult.isFailure()) {
            throw new DatabaseException(getResult.getMessage());
        }

        getResult.getData().sort(new Comparator<Line>() {
            @Override
            public int compare(Line o1, Line o2) {
                int num1 = extractInt(o1.getName());
                int num2 = extractInt(o2.getName());

                if(num1 == 0 && num2 == 0) return o1.getName().compareTo(o2.getName());

                return num1 - num2;
            }

            int extractInt(String s) {
                String sCopy = new String(s);
                String num = sCopy.replaceAll("\\D", "");
                // return 0 if no digits found
                if(num.isEmpty()) return 0;
                else {
                    if(s.startsWith(num)) return Integer.parseInt(num);

                    return 0;
                }

            }
        });
        List<BaseDTO> lineDTOs = LineConverter.ConvertLinesToBaseDTOs(getResult.getData());
        return ResponseHelpers.getResponseData(lineDTOs);
    }

    @RequestMapping(value = "/active", method = RequestMethod.GET)
    public ResponseEntity<List<BaseDTO>> getActiveLines()
    {
        Result<List<BaseDTO>> getResult = lineService.getActiveLines();
        if(getResult.isFailure()) {
            throw new DatabaseException(getResult.getMessage());
        }

        return ResponseHelpers.getResponseData(getResult.getData());
    }

    @RequestMapping(value = "/{id}/schedule", method = RequestMethod.GET)
    public ResponseEntity<List<Schedule>> getLineSchedules(@PathVariable("id") Long id) throws Exception
    {
        Result<Line> lineResult = lineService.findById(id);
        if(!lineResult.hasData()){
            throw new BadRequestException(lineResult.getMessage());
        }

        Line line = lineResult.getData();
        List<Schedule> schedules = line.getSchedules();
        List<Schedule> latestSchedules = lineService.getLatestSchedules(schedules);

        return ResponseHelpers.getResponseData(latestSchedules);
    }

    @RequestMapping(value = "/{id}/minutes", method = RequestMethod.GET)
    public ResponseEntity<MinutesRequiredForWholeRouteDTO> getLineMinutesRequiredForWholeRoute(@PathVariable("id") Long id) throws Exception
    {
        Result<Line> lineResult = lineService.findById(id);
        if(!lineResult.hasData()){
            throw new BadRequestException(lineResult.getMessage());
        }

        Line line = lineResult.getData();
        MinutesRequiredForWholeRouteDTO minutesDTO = new MinutesRequiredForWholeRouteDTO(
                line.getMinutesRequiredForWholeRoute());
        return ResponseHelpers.getResponseData(minutesDTO);
    }

    @PreAuthorize("hasAuthority('TRANSPORT_ADMINISTRATOR')")
    @RequestMapping(value = "/{id}/minutes", method = RequestMethod.POST)
    public ResponseEntity updateLineMinutesRequiredForWholeRoute(
            @PathVariable("id") Long id, @RequestBody MinutesRequiredForWholeRouteDTO minutesDTO) throws Exception
    {
        int minutes = minutesDTO.getMinutes();
        if(minutes <= 0){
            throw new BadRequestException("Minutes required for whole route must be positive number!");
        }

        Result<Line> lineResult = lineService.findById(id);
        if(!lineResult.hasData()){
            throw new BadRequestException(lineResult.getMessage());
        }

        Line line = lineResult.getData();
        line.setMinutesRequiredForWholeRoute(minutes);
        lineService.checkIfLineCanBeActive(line);

        Result<Boolean> saveResult = lineService.save(line);
        if(saveResult.isFailure()) {
            throw new DatabaseException(saveResult.getMessage());
        }

        return ResponseHelpers.getResponseData(saveResult);
    }

    @RequestMapping(value = "/{id}/stop", method = RequestMethod.GET)
    public ResponseEntity<List<StopDTO>> getLineStops(@PathVariable("id") Long id) throws Exception
    {
        Result<Line> lineResult = lineService.findById(id);
        if(!lineResult.hasData()){
            throw new BadRequestException(lineResult.getMessage());
        }

        Line line = lineResult.getData();
        List<StopDTO> sortedStopDTOs = StopConverter.convertStopsToStopDTOs(line.getStops());

        return ResponseHelpers.getResponseData(sortedStopDTOs);
    }

    @RequestMapping(value = "/{id}/points-and-stops", method = RequestMethod.GET)
    public ResponseEntity<PointsAndStopsDTO> getLinePointsAndStops(@PathVariable("id") Long id) throws Exception
    {
        Result<Line> lineResult = lineService.findById(id);
        if(!lineResult.hasData()){
            throw new BadRequestException(lineResult.getMessage());
        }

        Line line = lineResult.getData();
        List<StopDTO> stopDTOs = StopConverter.convertStopsToStopDTOs(line.getStops());

        List<PointDTO> points = line.getPoints().stream()
                                                .map(point -> new PointDTO(point))
                                                .collect(Collectors.toList());
        PointsAndStopsDTO pointsAndStopsDTO = new PointsAndStopsDTO(points, stopDTOs);

        return ResponseHelpers.getResponseData(pointsAndStopsDTO);
    }

    @PreAuthorize("hasAuthority('TRANSPORT_ADMINISTRATOR')")
    @RequestMapping(value = "/point/add", method = RequestMethod.POST)
    public ResponseEntity addPointToLine(@RequestBody LineWithPointDTO lineWithPointDTO) throws Exception
    {
        Result<Line> lineResult = lineService.findById(lineWithPointDTO.getLineId());
        if(!lineResult.hasData()){
            throw new BadRequestException(lineResult.getMessage());
        }

        Point point = pointRepository.findByLatitudeAndLongitude(lineWithPointDTO.getLat(), lineWithPointDTO.getLng());
        Line line = lineResult.getData();
        if(line.getPoints().contains(point)) {
            throw new BadRequestException(Messages.AlreadyContains(
                    StringConstants.Line, line.getId(), StringConstants.Point));
        }

        int index = lineWithPointDTO.getPosition();
        if(index < 1 || index > (line.getPoints().size() + 1)){
            throw new BadRequestException("Position is out of bounds!");
        }

        Point newPoint = new Point(lineWithPointDTO.getLat(), lineWithPointDTO.getLng());
        List<Point> linePoints = line.getPoints();
        linePoints.add(index - 1, newPoint);
        line.setPoints(linePoints);

        Result<Boolean> saveResult = lineService.save(line);
        if(saveResult.isFailure()) {
            throw new DatabaseException(saveResult.getMessage());
        }

        return ResponseHelpers.getResponseData(saveResult);
    }

    @PreAuthorize("hasAuthority('TRANSPORT_ADMINISTRATOR')")
    @RequestMapping(value = "/point/remove", method = RequestMethod.POST)
    public ResponseEntity removePointFromLine(@RequestBody LineWithPointDTO lineWithPointDTO) throws Exception
    {
        Result<Line> lineResult = lineService.findById(lineWithPointDTO.getLineId());
        if(!lineResult.hasData()){
            throw new BadRequestException(lineResult.getMessage());
        }

        Point point = pointRepository.findByLatitudeAndLongitude(lineWithPointDTO.getLat(), lineWithPointDTO.getLng());
        Line line = lineResult.getData();
        if(!line.getPoints().contains(point)) {
            throw new BadRequestException(Messages.DoesNotContain(
                    StringConstants.Line, line.getId(), StringConstants.Point));
        }

        List<Point> linePoints = line.getPoints();
        linePoints.remove(point);
        line.setPoints(linePoints);

        Result<Boolean> saveResult = lineService.save(line);
        if(saveResult.isFailure()) {
            throw new DatabaseException(saveResult.getMessage());
        }

        return ResponseHelpers.getResponseData(saveResult);
    }

    @RequestMapping(value = "/times", method = RequestMethod.GET)
    public ResponseEntity<ArrayList<TimesDTO>> getDepartureLists(@RequestParam("date") String date, @RequestParam("day") String day, @RequestParam("lines") String linesStr) {
        try {
            String[] lines = linesStr.split(",");

            for(int i = 0; i < lines.length; i++) {
                if(lines[i].trim().equals("")) return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }

            ArrayList<TimesDTO> timesDTOs = lineService.getDepartureLists(date, day, lines);
            if(timesDTOs == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);

            return new ResponseEntity<ArrayList<TimesDTO>>(timesDTOs, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }


}