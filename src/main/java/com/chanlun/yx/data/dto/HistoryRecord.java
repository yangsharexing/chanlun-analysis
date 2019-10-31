package com.chanlun.yx.data.dto;

public class HistoryRecord {

	private double open;

	private double high;

	private double close;

	private double low;

	private double volume;

	private double priceChange;

	private double pChange;

	private String time;

	private String startTime;

	private String endTime;

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public double getPriceChange() {
		return priceChange;
	}

	public void setPriceChange(double priceChange) {
		this.priceChange = priceChange;
	}

	public double getpChange() {
		return pChange;
	}

	public void setpChange(double pChange) {
		this.pChange = pChange;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "HistoryRecord [open=" + open + ", high=" + high + ", close=" + close + ", low=" + low + ", volume="
				+ volume + ", priceChange=" + priceChange + ", pChange=" + pChange + ", startTime=" + startTime
				+ ", endTime=" + endTime + "]";
	}
}
