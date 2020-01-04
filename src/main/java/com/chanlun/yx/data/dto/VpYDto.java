package com.chanlun.yx.data.dto;

public class VpYDto {

	private double y;

	private String time;

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "VpYDto [y=" + y + ", time=" + time + "]";
	}
}
