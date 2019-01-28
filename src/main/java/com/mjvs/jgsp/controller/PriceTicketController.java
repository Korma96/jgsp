package com.mjvs.jgsp.controller;

import com.mjvs.jgsp.dto.PriceTicketDTO;
import com.mjvs.jgsp.dto.PriceTicketFrontendDTO;

import com.mjvs.jgsp.service.PriceTicketService;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/priceticket")
public class PriceTicketController {

    @Autowired
    private PriceTicketService priceTicketService;


    @PreAuthorize("hasAuthority('TRANSPORT_ADMINISTRATOR')")
    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity addPriceTicket(@RequestBody PriceTicketDTO priceTicketDTO){
        System.out.println("kontroler");
        //boolean added = this.priceTicketService.addTicket(priceTicketDTO);
        Map<String, String> added;

        try {
            added = this.priceTicketService.addTicket(priceTicketDTO);
        }catch (Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<Map>(added, HttpStatus.OK);
    }



    @PreAuthorize("hasAuthority('TRANSPORT_ADMINISTRATOR')")
    @RequestMapping(value = "/pricetickets", method = RequestMethod.GET)
    public ResponseEntity<List<PriceTicketFrontendDTO>> getPriceTickets() {
        try {

            List<PriceTicketFrontendDTO> priceTicketFrontendDTOS = priceTicketService.getAllPriceTickets();
            return new ResponseEntity<List<PriceTicketFrontendDTO>>(priceTicketFrontendDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("hasAuthority('TRANSPORT_ADMINISTRATOR')")
    @RequestMapping(value = "/{id}/update", method = RequestMethod.PUT)
    public ResponseEntity update(@RequestBody PriceTicketDTO priceTicketDTO, @PathVariable Long id) {

        Map<String,String> response = new HashMap<>();
        Pair<String,Boolean> changed;

        try {
            changed = this.priceTicketService.update(priceTicketDTO,id);
        } catch (Exception e){
            return new ResponseEntity(response,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        response.put("message",changed.getKey());
        response.put("changed",changed.getValue().toString());

        return new ResponseEntity(response,HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('TRANSPORT_ADMINISTRATOR')")
    @RequestMapping(value = "/{id}/delete", method = RequestMethod.DELETE)
    public ResponseEntity delete(@PathVariable("id") Long id) throws Exception
    {

        System.out.println("kontrolor");
        Map<String,String> deleted;

        try {
            deleted = this.priceTicketService.delete(id);
        } catch (Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(deleted, HttpStatus.OK);


    }


}
