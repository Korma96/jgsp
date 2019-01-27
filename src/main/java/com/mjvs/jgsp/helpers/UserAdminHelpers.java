package com.mjvs.jgsp.helpers;

import java.time.LocalDate;

import com.mjvs.jgsp.dto.ReportDTO;
import com.mjvs.jgsp.model.Ticket;

public class UserAdminHelpers {
	
	public static ReportDTO calculateReport(ReportDTO report, Ticket ticket){
		
		int students = 0;
    	int pensioners = 0;
    	int others = 0;
    	double otherProfit = 0;
    	double studentProfit = 0;
    	double pensionerProfit = 0;
		
		switch (ticket.getTicketType())
		{
		case DAILY:
			report.setDaily(report.getDaily()+1);
			break;
		case MONTHLY:
			report.setMonthly(report.getMonthly()+1);
			break;
		case YEARLY:
			report.setYearly(report.getYearly()+1);
			break;
		case ONETIME:
			report.setOnetime(report.getOnetime()+1);
			break;
		}
		
		switch (ticket.getPassengerType())
		{
			case STUDENT:
				students++;
				studentProfit+=ticket.getPrice();
				break;
			case PENSIONER:
				pensioners++;
				pensionerProfit+=ticket.getPrice();
				break;
			case OTHER:
				others++;
				otherProfit+=ticket.getPrice();
				break;
				
		}
		
		report.setProfit(report.getProfit() + ticket.getPrice());
		
		return report;
	}
	
	public static String toValidDateFormat(String dateStr){
		String[] tokens = dateStr.split("-");
		if (tokens[1].length() == 1 && tokens[2].length() == 1) return tokens[0] + "-0" + tokens[1] + "-0" + tokens[2];
		if (tokens[1].length() == 1) return tokens[0] + "-0" + tokens[1] + "-" + tokens[2];
		if (tokens[2].length() == 1) return tokens[0] + "-" + tokens[1] + "-0" + tokens[2];
		else return dateStr;
		
	}
	

}
