package com.mjvs.jgsp.unit_tests.helpers;

import static org.junit.Assert.*;

import java.time.LocalDateTime;

import org.junit.Before;
import org.junit.Test;

import com.mjvs.jgsp.dto.ReportDTO;
import com.mjvs.jgsp.helpers.UserAdminHelpers;
import com.mjvs.jgsp.model.PassengerType;
import com.mjvs.jgsp.model.Ticket;
import com.mjvs.jgsp.model.TicketType;
import com.mjvs.jgsp.model.Zone;

public class UserAdminHelpersTests {
	
	private ReportDTO report;
	private Ticket ticket;

	@Before
	public void setUp() throws Exception {
		ticket = new Ticket(1L, LocalDateTime.now(), LocalDateTime.now(), TicketType.DAILY, PassengerType.OTHER ,65, new Zone("1", null));
		report = new ReportDTO(0,0,0,0,0);
	}

	@Test
	public void calculateReportTest() {
		
		ReportDTO reportTest = UserAdminHelpers.calculateReport(report, ticket);
		
		assertEquals(1, reportTest.getDaily());
		double myPi = 22.0d / 7.0d;
		assertEquals(65, reportTest.getProfit(), myPi);
		assertEquals(0, reportTest.getMonthly());
		assertEquals(0, reportTest.getOneTime());
		assertEquals(0, reportTest.getYearly());


		
	}

}
