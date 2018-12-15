package com.mjvs.jgsp.dto;

import java.time.LocalDate;

public class ReportDTO {
	
	private int oneTime;
	private int daily;
	private int monthly;
	private int yearly;
	private double profit;
	
	
	public ReportDTO() 
	{
	}


	public ReportDTO(int oneTime, int daily, int monthly, int yearly, double profit) {
		super();
		this.oneTime = oneTime;
		this.daily = daily;
		this.monthly = monthly;
		this.yearly = yearly;
		this.profit = profit;
	}
	
	
	
	


}
