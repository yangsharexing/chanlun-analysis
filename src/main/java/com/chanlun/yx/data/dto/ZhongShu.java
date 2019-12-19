package com.chanlun.yx.data.dto;

import java.util.List;

/**
 * 中枢
 */
public class ZhongShu extends TrendType{

	// 中枢高点
	private double zg;

	// 中枢低点
	private double zd;

	// 中枢振幅高点
	private double gg;

	// 中枢振幅低点
	private double dd;
	
	//中枢级别  总是级别  1表示又线段构成的中枢  2表示线段构成中枢的上一个级别  以此类推
	private int level;
	
	//中枢的段数
	private int num;
	
	List<Line> lines;
	
	//中枢开始时间
	private String startTime;
	
	//中枢结束时间
	private String endTime;
	
	//中枢离开段
	private Line leaveLine;
	
	public Line getLeaveLine() {
		return leaveLine;
	}

	public void setLeaveLine(Line leaveLine) {
		this.leaveLine = leaveLine;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public double getZg() {
		return zg;
	}

	public void setZg(double zg) {
		this.zg = zg;
	}

	public double getZd() {
		return zd;
	}

	public void setZd(double zd) {
		this.zd = zd;
	}

	public double getGg() {
		return gg;
	}

	public void setGg(double gg) {
		this.gg = gg;
	}

	public double getDd() {
		return dd;
	}

	public void setDd(double dd) {
		this.dd = dd;
	}
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
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
	

	public List<Line> getLines() {
		return lines;
	}

	public void setLines(List<Line> lines) {
		this.lines = lines;
	}

	@Override
	public String toString() {
		return "ZhongShu [zg=" + zg + ", zd=" + zd + ", gg=" + gg + ", dd=" + dd + ", level=" + level + ", num=" + num
				+ ", lines=" + lines + ", startTime=" + startTime + ", endTime=" + endTime + ", leaveLine=" + leaveLine
				+ "]";
	}

}
