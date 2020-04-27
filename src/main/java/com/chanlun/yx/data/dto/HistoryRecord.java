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
	
	private double abs_length;
	
	private double turn;
	
	private double upLineRate;
	
	private double downLineRate;
	
	private double priceCh;
	
	private double Ma5;
	
	private double Ma10;
	
	private double Ma20;
	
	private double Ma30;
	
	private double Ma60;
	
	private double liDu;
	
	public double getLiDu() {
		return liDu;
	}

	public void setLiDu(double liDu) {
		this.liDu = liDu;
	}

	public double getMa5() {
		return Ma5;
	}

	public void setMa5(double ma5) {
		Ma5 = ma5;
	}

	public double getMa10() {
		return Ma10;
	}

	public void setMa10(double ma10) {
		Ma10 = ma10;
	}

	public double getMa20() {
		return Ma20;
	}

	public void setMa20(double ma20) {
		Ma20 = ma20;
	}

	public double getMa30() {
		return Ma30;
	}

	public void setMa30(double ma30) {
		Ma30 = ma30;
	}

	public double getMa60() {
		return Ma60;
	}

	public void setMa60(double ma60) {
		Ma60 = ma60;
	}

	public double getPriceCh() {
		return priceCh;
	}

	public void setPriceCh(double priceCh) {
		this.priceCh = priceCh;
	}

	public double getUpLineRate() {
		return upLineRate;
	}

	public void setUpLineRate(double upLineRate) {
		this.upLineRate = upLineRate;
	}

	public double getDownLineRate() {
		return downLineRate;
	}

	public void setDownLineRate(double downLineRate) {
		this.downLineRate = downLineRate;
	}

	public double getTurn() {
		return turn;
	}

	public void setTurn(double turn) {
		this.turn = turn;
	}

	public double getAbs_length() {
		return abs_length;
	}

	public void setAbs_length(double abs_length) {
		this.abs_length = abs_length;
	}

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
