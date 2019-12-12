package com.chanlun.yx.data.util;

import java.util.ArrayList;
import java.util.List;

import com.chanlun.yx.data.dto.HistoryRecord;

public class KLineUtils {

	/**
	 * @param recordA
	 * @param recordB
	 * @return 若recordA包含或等于recordB则返回 1，若recordB包含recordA则返回-1 互不包含则返回0
	 */
	public static int isContains(HistoryRecord recordA, HistoryRecord recordB) {
		if (recordA.getLow() <= recordB.getLow() && recordA.getHigh() >= recordB.getHigh()) {
			return 1;
		}
		if (recordB.getLow() <= recordA.getLow() && recordB.getHigh() >= recordA.getHigh()) {
			return -1;
		}
		return 0;
	}

	/**
	 * 合并需要遵从【从左往右原则】合并【方向右之前两根合并处理过的柱子的方向决定】，保证前后两者不具有包含关系
	 * 第一个柱子与第二个如果有包含关系，则直接合并为最长的那根为第一根。
	 * 
	 * @param before
	 *            recordA 之前那根
	 * @param recordA
	 *            可能需要合并的第一根
	 * @param recordB
	 *            可能需要合并的第二根
	 * @return 如果 recordA recordB 需要合并，则返回合并后的 值，如果不需要合并则返回null
	 */
	public static HistoryRecord merge(HistoryRecord before, HistoryRecord recordA, HistoryRecord recordB) {

		HistoryRecord record = new HistoryRecord();

		// 不存在包含关系，无需合并
		if (isContains(recordA, recordB) == 0) {
			return null;
		}

		// before ==null 说明recordA recordB 为前两根柱子，在这里还有包含关系
		if (before == null) {
			// 直接取最长的
			record.setStartTime(recordA.getStartTime());
			record.setEndTime(recordB.getEndTime());
			record.setHigh(maxHight(recordA, recordB));
			record.setLow(minLow(recordA, recordB));
			record.setVolume(recordA.getVolume() + recordB.getVolume());
			return record;
		}

		// before 不为 null 说明recordA recordB 不是起始两根 且有包含关系
		// [recordA 与 before必然没包含关系，高点或则低点也绝不会对齐]
		if (recordA.getHigh() > before.getHigh()) {
			// 方向向上【取最高的高点为合并高点，取最高的低点为合并低点】
			record.setStartTime(recordA.getStartTime());
			record.setEndTime(recordB.getEndTime());
			record.setHigh(maxHight(recordA, recordB));
			record.setLow(maxLow(recordA, recordB));
			record.setVolume(recordA.getVolume() + recordB.getVolume());
		} else {
			// 方向向下【取最低的低点为合并低点，取最底的高点为合并高点】
			record.setStartTime(recordA.getStartTime());
			record.setEndTime(recordB.getEndTime());
			record.setHigh(minHight(recordA, recordB));
			record.setLow(minLow(recordA, recordB));
			record.setVolume(recordA.getVolume() + recordB.getVolume());
		}

		return record;
	}

	/**
	 * 检查是否还具有包含关系
	 * 
	 * @param list
	 *            是否还具有包含关系
	 * @return 返回true 不具有包含关系【处理完成后的K】 返回false 仍然需要处理合并
	 */
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

	/**
	 * 柱子简化处理
	 * 
	 * @param list
	 * @return
	 */
	public static List<HistoryRecord> handleKLine(List<HistoryRecord> list) {

		// 检查是否处理完成，处理完成就返回
		if (KLineUtils.checkContains(list)) {
			return list;
		}
		List<HistoryRecord> simpleList = new ArrayList<HistoryRecord>();
		HistoryRecord tempRecord = list.get(0);
		HistoryRecord before = null;
		for (int i = 0; i < list.size() - 1; i++) {
			
			HistoryRecord mergeRecord = merge(before, tempRecord, list.get(i + 1));
			if (mergeRecord == null) {// 无需合并，第k个柱子可以确认
				simpleList.add(copyProperties(tempRecord));
				
				before = copyProperties(tempRecord);
				tempRecord = list.get(i + 1);
				if (i == list.size() - 2) {
					// 倒数两根无包含关系
					simpleList.add(copyProperties(list.get(i + 1)));
					break;// finish
				}
			} else {
				if (i == list.size() - 2) {
					// 倒数两根无包含关系
					simpleList.add(copyProperties(mergeRecord));
					break;// finish
				}
				tempRecord = copyProperties(mergeRecord);
			}
		}
		return simpleList;
	}

	// 获取最高点
	private static double maxHight(HistoryRecord recordA, HistoryRecord recordB) {

		if (recordA.getHigh() > recordB.getHigh()) {
			return recordA.getHigh();
		} else {

			return recordB.getHigh();
		}

	}

	// 获取最大的低点高点
	private static double maxLow(HistoryRecord recordA, HistoryRecord recordB) {

		if (recordA.getLow() > recordB.getLow()) {
			return recordA.getLow();
		} else {

			return recordB.getLow();
		}

	}

	// 获取最小的高点
	private static double minHight(HistoryRecord recordA, HistoryRecord recordB) {

		if (recordA.getHigh() < recordB.getHigh()) {
			return recordA.getHigh();
		} else {

			return recordB.getHigh();
		}
	}

	// 获取最低点
	private static double minLow(HistoryRecord recordA, HistoryRecord recordB) {
		if (recordA.getLow() < recordB.getLow()) {
			return recordA.getLow();
		} else {
			return recordB.getLow();
		}
	}


	private static HistoryRecord copyProperties(HistoryRecord orgRecord) {
		HistoryRecord decRecord = new HistoryRecord();
		decRecord.setLow(orgRecord.getLow());
		decRecord.setHigh(orgRecord.getHigh());
		decRecord.setVolume(orgRecord.getVolume());
		decRecord.setStartTime(orgRecord.getStartTime());
		decRecord.setEndTime(orgRecord.getEndTime());
		return decRecord;
	}
	
	public static void main(String[] args) {
		List<HistoryRecord> records = new ArrayList<HistoryRecord>();
		HistoryRecord recordA = new HistoryRecord();
		HistoryRecord recordB = new HistoryRecord();
		HistoryRecord recordC = new HistoryRecord();
		HistoryRecord recordD = new HistoryRecord();
		HistoryRecord recordE = new HistoryRecord();
		
		recordA.setHigh(2);
		recordA.setLow(1);
		recordB.setHigh(3);
		recordB.setLow(0.5);
		recordC.setHigh(4);
		recordC.setLow(0.3);
		recordD.setHigh(1.5);
		recordD.setLow(0.1);
		recordE.setHigh(4);
		recordE.setLow(0.01);
		
		records.add(recordC);
		records.add(recordB);
		records.add(recordA);
		records.add(recordD);
		records.add(recordE);
//		records.add(recordA);
//		records.add(recordB);
//		records.add(recordC);
//		records.add(recordD);
		
		System.out.println(handleKLine(records));
	}
}
