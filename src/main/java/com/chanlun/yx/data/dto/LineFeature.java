package com.chanlun.yx.data.dto;

public class LineFeature {

	private int kNum;

	private double volume;

	private Point startPoint;

	private Point endPoint;

	private double low;

	private double hight;
	
	private double priceStep;
	
	public double getPriceStep() {
		return priceStep;
	}

	public void setPriceStep(double priceStep) {
		this.priceStep = priceStep;
	}

	public int getkNum() {
		return kNum;
	}

	public void setkNum(int kNum) {
		this.kNum = kNum;
	}

	public double getVolume() {
		return volume;
	}

	public void setVolume(double volume) {
		this.volume = volume;
	}

	public Point getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Point startPoint) {
		this.startPoint = startPoint;
	}

	public Point getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Point endPoint) {
		this.endPoint = endPoint;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getHight() {
		return hight;
	}

	public void setHight(double hight) {
		this.hight = hight;
	}

	@Override
	public String toString() {
		return "LineFeature [kNum=" + kNum + ", volume=" + volume + ", startPoint=" + startPoint + ", endPoint="
				+ endPoint + ", low=" + low + ", hight=" + hight + "]";
	}

}
