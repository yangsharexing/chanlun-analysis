package com.chanlun.yx.data.dto;

public class TrendType {
	
	private int type;//1 表示中枢 2表示线段
	
	private int direct;//1表示向上 0 表示向下

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	public int getDirect() {
		return direct;
	}

	public void setDirect(int direct) {
		this.direct = direct;
	}

	@Override
	public String toString() {
		return "TrendType [type=" + type + ", direct=" + direct + "]";
	}

}
