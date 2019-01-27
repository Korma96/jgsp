package com.mjvs.jgsp.dto;

public class ReportDTO {
	
	private int onetime;
	private int daily;
	private int monthly;
	private int yearly;
	private double profit;
	
	
	public ReportDTO() 
	{
	}


	public ReportDTO(int onetime, int daily, int monthly, int yearly, double profit) {
		super();
		this.onetime = onetime;
		this.daily = daily;
		this.monthly = monthly;
		this.yearly = yearly;
		this.profit = profit;
	}


	public int getOneTime() {
		return onetime;
	}


	public void setOneTime(int oneTime) {
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
