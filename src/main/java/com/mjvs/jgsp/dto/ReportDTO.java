package com.mjvs.jgsp.dto;

public class ReportDTO {
	
	private int onetime;
	private int daily;
	private int monthly;
	private int yearly;
	private double profit;
	private double dailyProfit = 0;
	private double monthlyProfit = 0;
	private double yearlyProfit = 0;
	private double onetimeProfit = 0;
	
	
	public ReportDTO() 
	{
	}


	public ReportDTO(int onetime, int daily, int monthly, int yearly, double profit, double onetimeProfit, double dailyProfit,
			double monthlyProfit, double yearlyProfit) {
		super();
		this.onetime = onetime;
		this.daily = daily;
		this.monthly = monthly;
		this.yearly = yearly;
		this.profit = profit;
		this.dailyProfit = dailyProfit;
		this.monthlyProfit = monthlyProfit;
		this.yearlyProfit = yearlyProfit;
		this.onetimeProfit = onetimeProfit;
	}




	public int getOnetime() {
		return onetime;
	}


	public void setOnetime(int oneTime) {
		this.onetime = oneTime;
	}


	public int getDaily() {
		return daily;
	}


	public void setDaily(int daily) {
		this.daily = daily;
	}


	public int getMonthly() {
		return monthly;
	}


	public void setMonthly(int monthly) {
		this.monthly = monthly;
	}


	public int getYearly() {
		return yearly;
	}


	public void setYearly(int yearly) {
		this.yearly = yearly;
	}


	public double getProfit() {
		return profit;
	}


	public void setProfit(double profit) {
		this.profit = profit;
	}

	public double getDailyProfit() {
		return dailyProfit;
	}


	public void setDailyProfit(double dailyProfit) {
		this.dailyProfit = dailyProfit;
	}


	public double getMonthlyProfit() {
		return monthlyProfit;
	}


	public void setMonthlyProfit(double monthlyProfit) {
		this.monthlyProfit = monthlyProfit;
	}


	public double getYearlyProfit() {
		return yearlyProfit;
	}


	public void setYearlyProfit(double yearlyProfit) {
		this.yearlyProfit = yearlyProfit;
	}


	public double getOnetimeProfit() {
		return onetimeProfit;
	}


	public void setOnetimeProfit(double onetimeProfit) {
		this.onetimeProfit = onetimeProfit;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + daily;
		result = prime * result + monthly;
		result = prime * result + onetime;
		long temp;
		temp = Double.doubleToLongBits(profit);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + yearly;
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportDTO other = (ReportDTO) obj;
		if (daily != other.daily)
			return false;
		if (monthly != other.monthly)
			return false;
		if (onetime != other.onetime)
			return false;
		if (Double.doubleToLongBits(profit) != Double.doubleToLongBits(other.profit))
			return false;
		if (yearly != other.yearly)
			return false;
		return true;
	}


}
