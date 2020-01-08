package com.chanlun.yx.data.dto;

public class HL {
	
	private double hl = 0;
	private int num = 0;
	private int dayNum = 0;
	private int hlNum = 0;
	private double hlv = 0;
	
	public int getHlNum() {
		return hlNum;
	}

	public void setHlNum(int hlNum) {
		this.hlNum = hlNum;
	}

	public double getHlv() {
		
		return this.hlNum/Double.parseDouble(this.num+"");
	}

	public void setHlv(double hlv) {
		this.hlv = hlv;
	}

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
		return "HL [hl=" + hl + ", num=" + num + ", dayNum=" + dayNum + ", hlNum=" + hlNum + ", hlv=" + hlv + "]";
	}
}
