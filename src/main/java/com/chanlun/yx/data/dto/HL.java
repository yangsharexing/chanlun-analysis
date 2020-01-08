package com.chanlun.yx.data.dto;

public class HL {
	
	private double hl = 0;
	private int num = 0;
	private int dayNum = 0;
	

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getDayNum() {
		return dayNum;
	}

	public void setDayNum(int dayNum) {
		this.dayNum = dayNum;
	}

	public double getHl() {
		return hl;
	}

	public void setHl(double hl) {
		this.hl = hl;
	}

	@Override
	public String toString() {
		return "HL [hl=" + hl + ", num=" + num + ", dayNum=" + dayNum + "]";
	}
}
