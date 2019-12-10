package com.chanlun.yx.data.dto;

public class BiDesc {
	public BiDesc(Point startBi, Point endBi, int type) {
		super();
		this.startBi = startBi;
		
		this.endBi = endBi;
		this.type = type;
	}

	public BiDesc(int startIndex, int endIndex, Point startBi, Point endBi, int type) {
		super();
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.startBi = startBi;
		this.endBi = endBi;
		this.type = type;
	}


	public BiDesc() {
		super();
	}
	
	private int startIndex = 0;
	private int endIndex = 0;
	private Point startBi = null;

	private Point endBi = null;

	private int type;//// 1 向上 0向下

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}

	public void setEndIndex(int endIndex) {
		this.endIndex = endIndex;
	}

	public Point getStartBi() {
		return startBi;
	}

	public void setStartBi(Point startBi) {
		this.startBi = startBi;
	}

	public Point getEndBi() {
		return endBi;
	}

	public void setEndBi(Point endBi) {
		this.endBi = endBi;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}