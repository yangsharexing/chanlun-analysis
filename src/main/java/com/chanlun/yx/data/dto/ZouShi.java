package com.chanlun.yx.data.dto;

import java.util.List;

public class ZouShi {

	private List<TrendType> list;

	private int num;//线段总数
	
	private int zhsNum;//中枢数量

	public List<TrendType> getList() {
		return list;
	}

	public void setList(List<TrendType> list) {
		this.list = list;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getZhsNum() {
		return zhsNum;
	}

	public void setZhsNum(int zhsNum) {
		this.zhsNum = zhsNum;
	}
	
}
