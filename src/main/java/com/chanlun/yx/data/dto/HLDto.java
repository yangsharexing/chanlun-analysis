package com.chanlun.yx.data.dto;

public class HLDto {

	private String code;
	private double buy = 0;
	private double sale = 0;
	private int dayNum = 0;
	private String startTm;
	private String endTm;
	private double yinliRate = 0;
	private double prePriceStep = 0;
	private int preKLine = 0;
	private double preVolume = 0;
	private double afterPriceStep = 0;
	private int afterKLine = 0;
	private double afterVolume = 0;
	
	private double preDiff = 0;
	private double afterDiff = 0;
	
	private double zhishui = 0;
	
	
	private int preZhongshuLineNum;
	
	private int afterZhongshuLineNum;
	
	private double preMacd;
	
	private double afterMacd;
	
	public double getPreMacd() {
		return preMacd;
	}

	public void setPreMacd(double preMacd) {
		this.preMacd = preMacd;
	}

	public double getAfterMacd() {
		return afterMacd;
	}

	public void setAfterMacd(double afterMacd) {
		this.afterMacd = afterMacd;
	}

	public double getZhishui() {
		return zhishui;
	}

	public void setZhishui(double zhishui) {
		this.zhishui = zhishui;
	}

	public int getPreZhongshuLineNum() {
		return preZhongshuLineNum;
	}

	public void setPreZhongshuLineNum(int preZhongshuLineNum) {
		this.preZhongshuLineNum = preZhongshuLineNum;
	}

	public int getAfterZhongshuLineNum() {
		return afterZhongshuLineNum;
	}

	public void setAfterZhongshuLineNum(int afterZhongshuLineNum) {
		this.afterZhongshuLineNum = afterZhongshuLineNum;
	}

	public double getPreDiff() {
		return preDiff;
	}

	public void setPreDiff(double preDiff) {
		this.preDiff = preDiff;
	}

	public double getAfterDiff() {
		return afterDiff;
	}

	public void setAfterDiff(double afterDiff) {
		this.afterDiff = afterDiff;
	}

	public int getPreKLine() {
		return preKLine;
	}

	public void setPreKLine(int preKLine) {
		this.preKLine = preKLine;
	}

	public double getPreVolume() {
		return preVolume;
	}

	public void setPreVolume(double preVolume) {
		this.preVolume = preVolume;
	}

	public int getAfterKLine() {
		return afterKLine;
	}

	public void setAfterKLine(int afterKLine) {
		this.afterKLine = afterKLine;
	}

	public double getAfterVolume() {
		return afterVolume;
	}

	public void setAfterVolume(double afterVolume) {
		this.afterVolume = afterVolume;
	}

	public double getPrePriceStep() {
		return prePriceStep;
	}

	public void setPrePriceStep(double prePriceStep) {
		this.prePriceStep = prePriceStep;
	}

	public double getAfterPriceStep() {
		return afterPriceStep;
	}

	public void setAfterPriceStep(double afterPriceStep) {
		this.afterPriceStep = afterPriceStep;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public double getBuy() {
		return buy;
	}

	public void setBuy(double buy) {
		this.buy = buy;
	}

	public double getSale() {
		return sale;
	}

	public void setSale(double sale) {
		this.sale = sale;
	}

	public int getDayNum() {
		return dayNum;
	}

	public void setDayNum(int dayNum) {
		this.dayNum = dayNum;
	}

	public String getStartTm() {
		return startTm;
	}

	public void setStartTm(String startTm) {
		this.startTm = startTm;
	}

	public String getEndTm() {
		return endTm;
	}

	public void setEndTm(String endTm) {
		this.endTm = endTm;
	}

	public double getYinliRate() {
		return yinliRate;
	}

	public void setYinliRate(double yinliRate) {
		this.yinliRate = yinliRate;
	}

	@Override
	public String toString() {
		return "HLDto [code=" + code + ", buy=" + buy + ", sale=" + sale + ", dayNum=" + dayNum + ", startTm=" + startTm
				+ ", endTm=" + endTm + ", yinliRate=" + yinliRate + ", prePriceStep=" + prePriceStep + ", preKLine="
				+ preKLine + ", preVolume=" + preVolume + ", afterPriceStep=" + afterPriceStep + ", afterKLine="
				+ afterKLine + ", afterVolume=" + afterVolume + ", preDiff=" + preDiff + ", afterDiff=" + afterDiff
				+ ", preZhongshuLineNum=" + preZhongshuLineNum + ", afterZhongshuLineNum=" + afterZhongshuLineNum + "]";
	}
	
	

}
