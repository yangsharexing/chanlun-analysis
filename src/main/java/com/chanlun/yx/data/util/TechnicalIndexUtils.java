package com.chanlun.yx.data.util;

import java.util.List;

import com.chanlun.yx.data.dto.HistoryRecord;

public class TechnicalIndexUtils {
	
	public static void computeMacd(List<HistoryRecord> list) {
		
		computeMacd(list, 12, 16, 10);
	}
	

	private static void computeMacd(List<HistoryRecord> list, int emaSmall, int emaBig, int dea) {
		for (int i = 0; i < list.size(); i++) {

			if (i == 0) {
				computEma12(null, list.get(i), emaSmall);
				computEma26(null, list.get(i), emaBig);
				list.get(i).setDif(0.0);
				computDea(list.get(i), list.get(i), dea);
			} else {
				computEma12(list.get(i - 1), list.get(i), emaSmall);
				computEma26(list.get(i - 1), list.get(i), emaBig);
				list.get(i).setDif(Math.round((list.get(i).getEmaSamll() - list.get(i).getEmaBig()) * 1000) / 1000.0);
				computDea(list.get(i - 1), list.get(i), dea);
			}
			list.get(i).setBar(2 * (list.get(i).getDif() - list.get(i).getDea()));
		}
	}
	
	private static void computEma12(HistoryRecord beforeData, HistoryRecord currentDaydata, int ema12) {
		//
		ema12 = 12;

		if (beforeData == null) {
			currentDaydata.setEmaSamll(
					(Math.round(50 * (11.0 / 13.0) + currentDaydata.getClose() * (2.0 / 13)) * 1000) / 1000.0);
		} else {
			currentDaydata.setEmaSamll(Math
					.round((beforeData.getEmaSamll() * (11.0 / 13.0) + currentDaydata.getClose() * (2.0 / 13)) * 1000)
					/ 1000.0);
		}
	}

	private static void computEma26(HistoryRecord beforeData, HistoryRecord currentDaydata, int ema26) {
		//
		ema26 = 26;

		if (beforeData == null) {
			currentDaydata.setEmaBig(
					Math.round(((50 * (25.0 / 27.0) + currentDaydata.getClose() * (2.0 / 27.0))) * 1000) / 1000.0);
		} else {
			currentDaydata.setEmaBig(Math
					.round((beforeData.getEmaBig() * (25.0 / 27.0) + currentDaydata.getClose() * (2.0 / 27.0)) * 1000)
					/ 1000.0);
		}
	}
	
	private static void computDea(HistoryRecord beforeData, HistoryRecord currentDaydata, int dea) {

		dea = 9;

		if (beforeData == null) {
			currentDaydata
					.setDea(Math.round((50 * (8.0 / 10.0) + currentDaydata.getDif() * (2.0 / 10.0)) * 1000) / 1000.0);
		} else {

			currentDaydata.setDea(
					Math.round((beforeData.getDea() * (8.0 / 10.0) + currentDaydata.getDif() * (2.0 / 10.0)) * 1000)
							/ 1000.0);
		}
	}

}
