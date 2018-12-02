package com.mjvs.jgsp.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mjvs.jgsp.helpers.exception.LineNotFoundException;
import com.mjvs.jgsp.helpers.exception.TicketNotFoundException;
import com.mjvs.jgsp.helpers.exception.UserNotFoundException;
import com.mjvs.jgsp.model.*;
import com.mjvs.jgsp.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

@Service
public class TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private LineService lineService;

    @Autowired
    private PriceTicketService priceTicketService;

    @Autowired
    private UserService userService;


    @Override
    public boolean checkOnetimeTicket(Long ticketId, Long lineId) throws TicketNotFoundException, LineNotFoundException, UserNotFoundException {
        Ticket ticket = ticketRepository.findById(ticketId);
        if(ticket == null) throw new TicketNotFoundException(String.format("Ticket with id (%d) was not found in database.",
                                                                ticketId));

        if(ticket.getStartDateAndTime() != null) {
            // already activated
            return false;
        }

        Line line = lineService.findById(lineId).getData();
        if(line == null) throw new LineNotFoundException(String.format("Line with id (%d) was not found in database.", lineId));

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusMinutes(line.getMinutesRequiredForWholeRoute());

        ticket.setStartDateAndTime(start);
        ticket.setEndDateAndTime(end);

        User loggedUser = userService.getLoggedUser();
        Passenger loggedPassenger = (Passenger) loggedUser;

        PriceTicket priceTicket = priceTicketService.getPriceTicket(loggedPassenger.getPassengerType(), ticket.getTicketType(),
                                                                    line.getZone());
        ticket.lookAtPriceTicketAndSetPrice(priceTicket);

        ticketRepository.save(ticket);

        return true;
    }

    @Override
    public Ticket getTicket(Long id) {
        return ticketRepository.findById(id);
    }

    @Override
    public ByteArrayInputStream getPdfFileForTicket(Long id) throws TicketNotFoundException {
        Ticket ticket = getTicket(id);
        if(ticket == null) throw new TicketNotFoundException(String.format("Ticket with id (%d) was not found in database.", id));
        if(ticket.getStartDateAndTime() == null) throw new TicketNotFoundException(String.format("Ticket with id (%d) was not activated.", id));

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.COURIER, 32, BaseColor.GREEN);
        Chunk title = new Chunk("TICKET", titleFont);

        Paragraph paragraphTitle = new Paragraph();
        paragraphTitle.setAlignment(Element.ALIGN_CENTER);
        paragraphTitle.add(title);

        //LineSeparator lineSeparator = new LineSeparator(5, 100, BaseColor.DARK_GRAY, Element.ALIGN_CENTER, 60);

        /*Font font  = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 16, BaseColor.GREEN);
        Chunk ticketType = new Chunk(, font);
        Chunk passengerType = new Chunk(, font);
        //Chunk lineZone = new Chunk("Line/Zone:  " + ticket.getLineZone().toString(), font);
        Chunk price = new Chunk(, font);

        Paragraph paragraph = new Paragraph();
        paragraph.setAlignment(Element.ALIGN_CENTER);
        paragraph.add(ticketType);
        paragraph.breakUp();
        paragraph.add(passengerType);
        //paragraph.add(lineZone);
        paragraph.add(price);*/


        PdfPTable table = new PdfPTable(1);
        table.addCell("Type:  " + ticket.getTicketType().name());
        table.addCell("Passenger type:  " + ticket.getPassengerType());
        table.addCell("Price:  " + ticket.getPrice());

        try {
            document.addAuthor("Jgsp");
            document.add(paragraphTitle);
            document.add( Chunk.NEWLINE );
            document.add( Chunk.NEWLINE );
            document.add(table);
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
