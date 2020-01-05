package com.chanlun.yx.data.dto.resp;

import java.util.List;

import com.chanlun.yx.data.model.Monitor;

public class MinotorResponse extends BaseResponse {

	private List<Monitor> data;

	public List<Monitor> getData() {
		return data;
	}

	public void setData(List<Monitor> data) {
		this.data = data;
	}
}
