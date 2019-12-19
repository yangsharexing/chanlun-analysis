package com.chanlun.yx.data.dto;

import java.util.List;

public class BuyDto {

	private String code;
	private ZouShi zouShi;
	private List<HistoryRecord> historys;
	private List<HistoryRecord> tailRecord;
	private double buyPrice;
	private String buyTime;

	public List<HistoryRecord> getTailRecord() {
		return tailRecord;
	}

	public void setTailRecord(List<HistoryRecord> tailRecord) {
		this.tailRecord = tailRecord;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public ZouShi getZouShi() {
		return zouShi;
	}

	public void setZouShi(ZouShi zouShi) {
		this.zouShi = zouShi;
	}

	public List<HistoryRecord> getHistorys() {
		return historys;
	}

	public void setHistorys(List<HistoryRecord> historys) {
		this.historys = historys;
	}

	public double getBuyPrice() {
		return buyPrice;
	}

	public void setBuyPrice(double buyPrice) {
		this.buyPrice = buyPrice;
	}

	public String getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(String buyTime) {
		this.buyTime = buyTime;
	}

	@Override
	public String toString() {
		return "BuyDto [code=" + code + ", zouShi=" + zouShi + ", historys=" + historys + ", tailRecord=" + tailRecord
				+ ", buyPrice=" + buyPrice + ", buyTime=" + buyTime + "]";
	}

}
