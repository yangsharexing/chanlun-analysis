package com.chanlun.yx.data.util;

import java.util.ArrayList;
import java.util.List;

import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.dto.Point;
/**
 * 初始化：
 * 默认第一个为确认的底分型，第一笔不唯一，今后划分线段的时候第一段尽量不要作为买入依据
 *                                    	       （后高于前） 更新分析高点信息
 *                                         |
 * 						  (顶)---顶与顶比高 ----|
 * 						  |                |
 *  					  |               （后低于前） 不处理	 
 *   					  |
 * 			（顶）--(下一个)--				  （底高于顶） 不处理
 * 			|             |               |
 *			| 		      |				  |
 *			|             (底)---底是否低于顶 --|
 *			|							  |								（相隔少于一个K） 不处理
 *			|							  |								|
 *			|							  （底低于顶）---两个是否相隔至少一个K线-----|
 *			|															|
 *			| 															（相隔知道包含一个K） 确认顶分型，当前分型变成该地点的底分型
 *			|
 * 当前分型--- |
 * 			|			  (顶)	同理
 * 			|			  |
 * 			|			  |
 * 		    |			  |
 * 			（底）--(下一个)-->
 * 						  |
 * 						  |
 * 						  |
 * 						  （底）      同理
 * 
 * 
 * 分型后去掉第一笔
 *
 */
public class BiLineUtils {

	public static List<Point> contructBiLines(List<HistoryRecord> records) {

		List<Point> fenXinRecords = new ArrayList<Point>();
		// 默认第一个为确认的底分型，第一笔不唯一，今后划分线段的时候第一段尽量不要作为买入依据
		// 第二笔开始到倒数第二笔 都是唯一确认的，因为第一笔是从0开始，最后一笔未完成
		Point tempPoint = new Point();
		tempPoint.setType(0);
		tempPoint.setPrice(0);
		tempPoint.setTime("0");
		int preBeforeIndex = -2;// 前一个分型结束的位置，由于一个分型是默认的0 当出现第一个顶分型，且满足
								// 高于0分型，且顶分型后面出现底分型

		for (int i = 0; i < records.size() - 2; i++) {
			if (tempPoint.getType() == 1) {
				Point isFenXin = isFenXin(records.get(i), records.get(i + 1), records.get(i + 2));
				if (isFenXin == null) {
					continue;
				}
				if (isFenXin.getType() == 1) {
					if (isFenXin.getPrice() > tempPoint.getPrice()) {
						tempPoint = copyProperties(isFenXin);
					}
				} else {
					if (isFenXin.getPrice() < tempPoint.getPrice()) {// 确认

						// 确认分型之前，要保证两个分型之前至少有一个K线
						if (i - preBeforeIndex > 1) {
							fenXinRecords.add(copyProperties(tempPoint));

							// 加入一个节点后记录分型最后一k的位置
							tempPoint = copyProperties(isFenXin);
							preBeforeIndex = i + 2;
						}

					}
				}
			} else {
				Point isFenXin = isFenXin(records.get(i), records.get(i + 1), records.get(i + 2));
				if (isFenXin == null) {
					continue;
				}
				if (isFenXin.getType() == 1) {

					if (isFenXin.getPrice() > tempPoint.getPrice()) {// 确认
						// 确认分型之前，要保证两个分型之前至少有一个K线
						if (i - preBeforeIndex > 1) {
							fenXinRecords.add(copyProperties(tempPoint));

							// 加入一个节点后记录分型最后一k的位置
							preBeforeIndex = i + 2;
							tempPoint = copyProperties(isFenXin);
						}
					}

				} else {
					if (isFenXin.getPrice() < tempPoint.getPrice()) {
						tempPoint = copyProperties(isFenXin);
					}
				}
			}
		}

		// 最后一笔不是完成笔，一个相反的笔才能确认前一笔（这里不考虑价位）
		// 当个倒数第一个确认的点为 底时，则需要找到最后3个柱子，最高点是否大于底分型，如果满足则将这个高点设置为结束点。
		// 倒数第一个确认点是顶分析，同理

		Point lastFenXin = fenXinRecords.get(fenXinRecords.size() - 1);
		Point lastPoint = null;
		if (lastFenXin.getType() == 1) {
			if (records.get(records.size() - 1).getLow() < lastFenXin.getPrice()) {
				lastPoint = new Point();
				lastPoint.setPrice(records.get(records.size() - 1).getLow());
				lastPoint.setType(0);
				lastPoint.setTime(records.get(records.size() - 1).getEndTime());
			}
		} else {
			if (records.get(records.size() - 1).getHigh() > lastFenXin.getPrice()) {
				lastPoint = new Point();
				lastPoint.setPrice(records.get(records.size() - 1).getHigh());
				lastPoint.setType(0);
				lastPoint.setTime(records.get(records.size() - 1).getEndTime());
			}
		}
		// 重点，如果倒数第一个确认的分型为顶点，那么最后一个柱子最低点小于该确认的顶，那么最后一笔为该确认顶 到最后一点【向下】
		// 重点，如果倒数第一个确认的分型为底点，那么最后一个柱子最高点大于该确认的底，那么最后一笔为该确认的底 到最后一点 【向上】
		//// （由于确认一个分型，一定需要下一个相反分型出现，所以在最后一个确认的分型到最后一个K中间肯定还有一个临时的分型，如果不满足上面两个条件，我们就在最后一个临时分型处结束）
		if (lastPoint != null) {
			fenXinRecords.add(copyProperties(lastPoint));
		} else {
			fenXinRecords.add(copyProperties(tempPoint));
		}
		
		fenXinRecords.remove(0);//移除第一点 默认第一笔向下【因为第一笔在构成线段之后，极有可能成为第一个中枢的DD，这个就很严重了】
		return fenXinRecords;
	}

	private static Point copyProperties(Point isFenXin) {

		Point point = new Point();
		point.setPrice(isFenXin.getPrice());
		point.setTime(isFenXin.getTime());
		point.setType(isFenXin.getType());
		return point;
	}

	public static Point isFenXin(HistoryRecord recordA, HistoryRecord recordB, HistoryRecord recordC) {
		Point point = new Point();
		if (recordB.getHigh() > recordA.getHigh() && recordB.getHigh() > recordC.getHigh()) {
			// 顶
			point.setPrice(recordB.getHigh());
			point.setTime(recordB.getEndTime());
			point.setType(1);
			return point;

		}

		if (recordB.getLow() < recordA.getLow() && recordB.getLow() < recordC.getLow()) {
			// 底
			point.setPrice(recordB.getLow());
			point.setTime(recordB.getEndTime());
			point.setType(0);
			return point;
		}

		return null;

	}

	public static void main(String[] args) {

		List<HistoryRecord> records = new ArrayList<HistoryRecord>();
		HistoryRecord record1 = new HistoryRecord();
		HistoryRecord record2 = new HistoryRecord();
		HistoryRecord record3 = new HistoryRecord();
		HistoryRecord record4 = new HistoryRecord();
		HistoryRecord record5 = new HistoryRecord();
		HistoryRecord record6 = new HistoryRecord();
		HistoryRecord record7 = new HistoryRecord();
		HistoryRecord record8 = new HistoryRecord();

		HistoryRecord record11 = new HistoryRecord();
		HistoryRecord record12 = new HistoryRecord();
		HistoryRecord record13 = new HistoryRecord();
		HistoryRecord record14 = new HistoryRecord();
		HistoryRecord record15 = new HistoryRecord();

		record1.setHigh(3);
		record1.setLow(1);
		record2.setHigh(4);
		record2.setLow(2);
		record3.setHigh(5);
		record3.setLow(3);
		record4.setHigh(4);
		record4.setLow(2);
		record5.setHigh(3);
		record5.setLow(1);
		record6.setHigh(2);
		record6.setLow(0.5);
		record7.setHigh(1);
		record7.setLow(0.1);
		record8.setHigh(2);
		record8.setLow(0.5);

		record11.setHigh(3);
		record11.setLow(1);
		record12.setHigh(4);
		record12.setLow(2);
		record13.setHigh(5);
		record13.setLow(3);
		record14.setHigh(4);
		record14.setLow(2);
		record15.setHigh(3);
		record15.setLow(1);

		records.add(record1);
		records.add(record2);
		records.add(record3);
		records.add(record4);
		records.add(record5);
		records.add(record6);
		records.add(record7);
		records.add(record8);

		records.add(record11);
		records.add(record12);
		records.add(record13);
		records.add(record14);
		records.add(record15);

		System.out.println(contructBiLines(records));

	}
}
