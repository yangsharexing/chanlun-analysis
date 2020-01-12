package com.chanlun.yx.data.dto;

public class HistoryRecord {
	
	private String code;

	private double open;

	private double high;

	private double close;

	private double low;

	private double volume;

	private String time;
	
	private String startTime;
	
	private String endTime;
	
	private double emaSamll;

	private double emaBig;

	private double dif;

	private double macd;

	private double bar;

	private double dea;
	
	public double getEmaSamll() {
		return emaSamll;
	}

	public void setEmaSamll(double emaSamll) {
		this.emaSamll = emaSamll;
	}

	public double getEmaBig() {
		return emaBig;
	}

	public void setEmaBig(double emaBig) {
		this.emaBig = emaBig;
	}

	public double getDif() {
		return dif;
	}

	public void setDif(double dif) {
		this.dif = dif;
	}

	public double getMacd() {
		return macd;
	}

	public void setMacd(double macd) {
		this.macd = macd;
	}

	public double getBar() {
		return bar;
	}

	public void setBar(double bar) {
		this.bar = bar;
	}

	public double getDea() {
		return dea;
	}

	public void setDea(double dea) {
		this.dea = dea;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
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

	@Override
	public String toString() {
		return "HistoryRecord [code=" + code + ", open=" + open + ", high=" + high + ", close=" + close + ", low=" + low
				+ ", volume=" + volume + ", time=" + time + "]";
	}

}
