package com.mjvs.jgsp.service;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mjvs.jgsp.helpers.exception.LineNotFoundException;
import com.mjvs.jgsp.helpers.exception.PriceTicketNotFoundException;
import com.mjvs.jgsp.helpers.exception.TicketNotFoundException;
import com.mjvs.jgsp.model.*;
import com.mjvs.jgsp.repository.TicketRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TicketServiceImpl implements TicketService {

    private final Logger logger = LogManager.getLogger(this.getClass());

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private LineService lineService;

    @Autowired
    private PriceTicketService priceTicketService;

    @Autowired
    private UserService userService;


    @Override
    public boolean checkOnetimeTicket(Long ticketId, Long lineId) throws Exception {
        Ticket ticket = ticketRepository.findById(ticketId);
        String message;
        if(ticket == null) {
            message = String.format("Ticket with id (%d) was not found in database.", ticketId);
            logger.error(message);
            throw new TicketNotFoundException(message);
        }

        if(ticket.getStartDateAndTime() != null) {
            // already activated
            return false;
        }

        Line line = lineService.findById(lineId).getData();
        if(line == null) {
            message = String.format("Line with id (%d) was not found in database.", lineId);
            logger.error(message);
            throw new LineNotFoundException(message);
        }
        ticket.setLineZone(line);

        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusMinutes(line.getMinutesRequiredForWholeRoute());

        ticket.setStartDateAndTime(start);
        ticket.setEndDateAndTime(end);

        User loggedUser = userService.getLoggedUser();
        Passenger loggedPassenger = (Passenger) loggedUser;

        PriceTicket priceTicket = priceTicketService.getLatestPriceTicket(loggedPassenger.getPassengerType(), ticket.getTicketType(),
                                                                    line.getZone());
        if(priceTicket == null) {
            message = String.format("PriceTicket (passengerType = %s, ticketType = %s, zone = %s) was not found in database.",
                    loggedPassenger.getPassengerType().name(), ticket.getTicketType().name(), line.getZone().getName());
            logger.error(message);
            throw new PriceTicketNotFoundException(message);
        }

        ticket.lookAtPriceTicketAndSetPrice(priceTicket);

        ticketRepository.save(ticket);

        return true;
    }

    @Override
    public Ticket getTicket(Long id) {
        return ticketRepository.findById(id);
    }

    @Override
    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
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

	@Override
	public List<Ticket> getAll() {
		return ticketRepository.findAll();
	}
}
