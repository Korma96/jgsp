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
	private String testDate1;
	private String testDate2;
	private String testDate3;
	private String testDate4;
	private String testDate1Exp;
	private String testDate2Exp;
	private String testDate3Exp;
	private String testDate4Exp;

	

	@Before
	public void setUp() throws Exception {
		ticket = new Ticket(1L, LocalDateTime.now(), LocalDateTime.now(), TicketType.DAILY, PassengerType.OTHER ,65, new Zone("1", null));
		report = new ReportDTO(0,0,0,0,0,0,0,0,0);
		testDate1 = "2019-1-1";
		testDate2 = "2019-11-1";
		testDate3 = "2019-1-11";
		testDate4 = "2018-01-02";
		testDate1Exp = "2019-01-01";
		testDate2Exp = "2019-11-01";
		testDate3Exp = "2019-01-11";
		testDate4Exp = "2018-01-02";

		

		
	}

	@Test
	public void calculateReportTest() {
		
		ReportDTO reportTest = UserAdminHelpers.calculateReport(report, ticket);
		
		assertEquals(1, reportTest.getDaily());
		double myPi = 22.0d / 7.0d;
		assertEquals(65, reportTest.getProfit(), myPi);
		assertEquals(0, reportTest.getMonthly());
		assertEquals(0, reportTest.getOnetime());
		assertEquals(0, reportTest.getYearly());
		assertEquals(0, reportTest.getOnetimeProfit(),myPi);
		assertEquals(65, reportTest.getDailyProfit(),myPi);


	}
	
	@Test
	public void toValidDateFormatTest() {
		assertEquals(testDate1Exp,UserAdminHelpers.toValidDateFormat(testDate1));
		assertEquals(testDate2Exp,UserAdminHelpers.toValidDateFormat(testDate2));
		assertEquals(testDate3Exp,UserAdminHelpers.toValidDateFormat(testDate3));
		assertEquals(testDate4Exp,UserAdminHelpers.toValidDateFormat(testDate4));


		
	}
	

}
