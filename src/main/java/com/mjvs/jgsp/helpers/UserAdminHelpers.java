package com.mjvs.jgsp.helpers;

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
			report.setOneTime(report.getOneTime()+1);
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
	

}
