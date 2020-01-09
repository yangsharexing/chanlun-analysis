package com.chanlun.yx.data.dto;

public class BuyFeatrue {

	private double price;
	
	private double preDiff = 0;
	private double afterDiff = 0;
	
	private LineFeature preLineFeature;
	
	private LineFeature afterLineFeature;
	
	private double prePriceStep;
	
	private double afterPriceStep;
	
	private int preZhongshuLineNum;
	
	private int afterZhongshuLineNum;

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

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public LineFeature getPreLineFeature() {
		return preLineFeature;
	}

	public void setPreLineFeature(LineFeature preLineFeature) {
		this.preLineFeature = preLineFeature;
	}

	public LineFeature getAfterLineFeature() {
		return afterLineFeature;
	}

	public void setAfterLineFeature(LineFeature afterLineFeature) {
		this.afterLineFeature = afterLineFeature;
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

	@Override
	public String toString() {
		return "BuyFeatrue [price=" + price + ", preLineFeature=" + preLineFeature + ", afterLineFeature="
				+ afterLineFeature + ", prePriceStep=" + prePriceStep + ", afterPriceStep=" + afterPriceStep + "]";
	}
}
