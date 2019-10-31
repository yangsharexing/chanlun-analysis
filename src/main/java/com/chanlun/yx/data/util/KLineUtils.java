package com.chanlun.yx.data.util;

import java.util.ArrayList;
import java.util.List;

import com.chanlun.yx.data.dto.HistoryRecord;

public class KLineUtils {

	// 比较两个k先是否包含关系
	public static int isContains(HistoryRecord recordA, HistoryRecord recordB) {
		if (recordA.getLow() <= recordB.getLow() && recordA.getHigh() >= recordB.getHigh()) {
			return 1;
		}
		if (recordB.getLow() <= recordA.getLow() && recordB.getHigh() >= recordA.getHigh()) {
			return -1;
		}
		return 0;
	}

	// 合并K线
	public static HistoryRecord merge(HistoryRecord before, HistoryRecord recordA, HistoryRecord recordB) {
		if(before !=null && before.getTime().equals("2013/3/8") && before.getOpen() ==2314.59){
			System.out.println();
		}
			
		HistoryRecord record = new HistoryRecord();
		if (isContains(recordA, recordB) == 0) {
			return null;// 不是孕线不能合并
		}
		double hight = 0.0;
		if (recordA.getHigh() >= recordB.getHigh()) {

			hight = recordA.getHigh();
		} else {
			hight = recordB.getHigh();
		}
		double low = 0.0;
		if (recordA.getLow() <= recordB.getLow()) {

			low = recordA.getLow();
		} else {
			low = recordB.getLow();
		}

		if (before != null) {

			if (recordA.getHigh() > before.getHigh()) {//上升k
				record.setHigh(hight);
				if (recordA.getLow() >= recordB.getLow()) {
					record.setLow(recordA.getLow());
				} else {
					record.setLow(recordB.getLow());
				}
			} else {//下跌k
				record.setLow(low);
				if (recordA.getHigh() >= recordB.getHigh()) {
					record.setHigh(recordB.getHigh());
				} else {
					record.setHigh(recordA.getHigh());
				}
			}
			record.setPriceChange(recordB.getClose() - before.getClose());
			record.setpChange((recordB.getClose() - before.getClose()) / before.getClose());
		} else {
			record.setHigh(hight);
			record.setLow(low);
			record.setPriceChange(0.0);
			record.setpChange(0.0);
		}
		
		if(record.getHigh()==0){
			System.out.println("111");
		}
		record.setOpen(recordA.getOpen());
		record.setClose(recordB.getClose());
		record.setStartTime(recordA.getStartTime());
		record.setEndTime(recordB.getEndTime());
		record.setTime(recordA.getTime());
		record.setVolume(recordA.getVolume() + recordB.getVolume());

		return record;
	}

	// 相邻个是否有包含关系
	public static boolean checkContains(List<HistoryRecord> list) {

		if (list.size() < 2) {

			return true;

		} else {

			for (int i = 0; i < list.size() - 1; i++) {

				if (isContains(list.get(i), list.get(i + 1)) != 0) {

					return false;
				}
			}
			return true;
		}

	}

	public static List<HistoryRecord> simpleKLine(List<HistoryRecord> list) {
		if (KLineUtils.checkContains(list)) {
			return list;
		}
		List<HistoryRecord> simpleList = new ArrayList<HistoryRecord>();
		simpleList.add(list.get(0));
		int i = 1;
		while (true) {
			if (i > list.size() - 1) {
				break;
			}
			if (KLineUtils.isContains(simpleList.get(simpleList.size() - 1), list.get(i)) == 0) {
				simpleList.add(list.get(i));
				i++;
			} else {
				HistoryRecord newRecord = null;
				if (simpleList.size() > 1) {
					
					newRecord = KLineUtils.merge(simpleList.get(simpleList.size() - 2),
							simpleList.get(simpleList.size() - 1), list.get(i));
					simpleList.remove(simpleList.size()-1);
					simpleList.add(newRecord);
					i++;
				} else {
					newRecord = KLineUtils.merge(null, simpleList.get(simpleList.size() - 1), list.get(i));
					simpleList.remove(simpleList.size()-1);
					simpleList.add(newRecord);
					i++;
				}
			}
		}
		//
		return simpleKLine(simpleList);
//		return simpleList;
	}
}
