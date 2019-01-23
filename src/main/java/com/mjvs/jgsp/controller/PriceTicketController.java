package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.dto.PriceTicketDTO;
import com.mjvs.jgsp.dto.PriceTicketFrontendDTO;

import com.mjvs.jgsp.service.PriceTicketService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/priceticket")
public class PriceTicketController {

    @Autowired
    private PriceTicketService priceTicketService;


    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Boolean> addPriceTicket(@RequestBody PriceTicketDTO priceTicketDTO){
        System.out.println("kontroler");
        boolean added = this.priceTicketService.addTicket(priceTicketDTO);

        if(added){
            return new ResponseEntity<Boolean>(HttpStatus.CREATED);
        }
        return new ResponseEntity<Boolean>(HttpStatus.BAD_REQUEST);
    }



    @RequestMapping(value = "/pricetickets", method = RequestMethod.GET)
    public ResponseEntity<List<PriceTicketFrontendDTO>> getPriceTickets() {
        try {

            List<PriceTicketFrontendDTO> priceTicketFrontendDTOS = priceTicketService.getAllPriceTickets();
            return new ResponseEntity<List<PriceTicketFrontendDTO>>(priceTicketFrontendDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{id}/update", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody PriceTicketDTO priceTicketDTO, @PathVariable Long id) {

        System.out.println("kontroler");
        Pair<String,Boolean> changed = this.priceTicketService.update(priceTicketDTO,id);

//        System.out.println(changed.getValue());
       // System.out.println(changed.getKey());

        Map<String,String> response = new HashMap<>();

        response.put("message",changed.getKey());
        response.put("changed",changed.getValue().toString());

        if(changed.getValue()){
            return new ResponseEntity(response,HttpStatus.OK);
        }

        return new ResponseEntity(response,HttpStatus.OK);
    }


    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") Long id) throws Exception
    {

        System.out.println("kontrolor");
        Map<String,String> deleted = this.priceTicketService.delete(id);


        if(deleted.get("deleted").equalsIgnoreCase("true")){
            return new ResponseEntity(deleted, HttpStatus.OK);
        } else {
            return new ResponseEntity(deleted,HttpStatus.BAD_REQUEST);
        }

    }


}
