package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.dto.LineDTO;
import com.mjvs.jgsp.model.Line;
import com.mjvs.jgsp.service.LineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/lines")
public class LineController {

    @Autowired
    private LineService lineService;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LineDTO>> getLines() {
        List<Line> lines = lineService.getLines();

        List<LineDTO> lineDTOS = lines.stream()
                .map(LineDTO::new)
                .collect(Collectors.toList());

        return new ResponseEntity<>(lineDTOS, HttpStatus.OK)
;    }
}
