package com.mjvs.jgsp.helpers;

import java.time.LocalDate;

import com.mjvs.jgsp.dto.ReportDTO;
import com.mjvs.jgsp.model.Ticket;

public class UserAdminHelpers {
	
	public static ReportDTO calculateReport(ReportDTO report, Ticket ticket){
		
		switch (ticket.getTicketType())
		{
		case DAILY:
			report.setDaily(report.getDaily()+1);
			report.setDailyProfit(report.getDailyProfit() + ticket.getPrice());
			break;
		case MONTHLY:
			report.setMonthly(report.getMonthly()+1);
			report.setMonthlyProfit(report.getMonthlyProfit() + ticket.getPrice());
			break;
		case YEARLY:
			report.setYearly(report.getYearly()+1);
			report.setYearlyProfit(report.getYearlyProfit() + ticket.getPrice());
			break;
		case ONETIME:
			report.setOnetime(report.getOnetime()+1);
			report.setOnetimeProfit(report.getOnetimeProfit() + ticket.getPrice());
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
