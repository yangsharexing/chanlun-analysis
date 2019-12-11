package com.chanlun.yx.data.dto;

public class Point {

	private String time;

	private double price;

	private int type;// 1 髙点 0 低点
	
	public Point() {
		super();
	}

	public Point(String time, double price, int type) {
		super();
		this.time = time;
		this.price = price;
		this.type = type;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Point [time=" + time + ", price=" + price + ", type=" + type + "]";
	}
	
}
