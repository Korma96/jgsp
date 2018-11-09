package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.model.Stop;
import com.mjvs.jgsp.service.LineService;
import com.mjvs.jgsp.service.StopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/admin")
public class AdminController {

    @Autowired
    private LineService lineService;

    @Autowired
    private StopService stopService;

    @RequestMapping(value = "/add_line", method = RequestMethod.POST)
    public ResponseEntity addLine(@RequestBody Line line)
    {
        boolean result = lineService.add(line);

        if(result){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @RequestMapping(value = "/add_stop", method = RequestMethod.POST)
    public ResponseEntity addStop(@RequestBody Stop stop)
    {
        boolean result = stopService.add(stop);

        if(result){
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

}
