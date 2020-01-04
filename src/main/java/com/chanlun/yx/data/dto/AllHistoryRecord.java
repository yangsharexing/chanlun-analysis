package com.chanlun.yx.data.dto;

public class AllHistoryRecord extends HistoryRecord {

	private double pctChg;

	private double turn;

	
	public double getPctChg() {
		return pctChg;
	}

	public void setPctChg(double pctChg) {
		this.pctChg = pctChg;
	}

	public double getTurn() {
		return turn;
	}

	public void setTurn(double turn) {
		this.turn = turn;
	}

	@Override
	public String toString() {
		return "AllHistoryRecord [pctChg=" + pctChg + ", turn=" + turn + "]";
	}
}
