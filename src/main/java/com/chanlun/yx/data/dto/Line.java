package com.chanlun.yx.data.dto;

public class Line extends TrendType {

	private Point startPoint;

	private Point endPoint;

	private int num;

	public Line() {
		super();
	}

	public Line(Point startPoint, Point endPoint) {
		super();
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
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

	@Override
	public String toString() {
		return "Line [startPoint=" + startPoint + ", endPoint=" + endPoint + ", num=" + num + "]";
	}

}
