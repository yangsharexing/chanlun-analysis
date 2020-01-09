package com.chanlun.yx.data.util;

import java.util.List;

import com.chanlun.yx.data.dto.BuyFeatrue;
import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.dto.Line;
import com.chanlun.yx.data.dto.LineFeature;
import com.chanlun.yx.data.dto.Point;

public class BeiChiUtils {

	
	public static boolean isBeichi(Line line1, Line line2, List<HistoryRecord> list) {
		LineFeature f1 = computLine(line1, list);
		LineFeature f2 = computLine(line2, list);

		if (f1.getPriceStep() > f2.getPriceStep()*2) {

			return true;
		}
		return false;
//		return true;
	}
	
	public static boolean isBeichi(Line line1, Line line2, List<HistoryRecord> list,double preDiff,double afterDiff,BuyFeatrue buyf) {
		
		LineFeature f1 = computLine(line1, list);
		LineFeature f2 = computLine(line2, list);
		
		buyf.setPreLineFeature(f1);
		buyf.setAfterLineFeature(f2);
		buyf.setPreDiff(preDiff);
		buyf.setAfterDiff(afterDiff);
		
//		System.out.println(preDiff+"    "+afterDiff +"    "+f1.getPriceStep()+"    "+f2.getPriceStep());
//
//		if (f1.getPriceStep() > f2.getPriceStep()*2) {
//
//			return true;
//		}
//		return false;
		
		return true;
	}

	public static boolean isBeichi(Line line, Point start, Point end, List<HistoryRecord> list) {

		LineFeature f1 = computLine(line, list);
		LineFeature f2 = computPoint(start, end, list);

		if (f1.getPriceStep() > f2.getPriceStep()*2) {

			return true;
		}
		return false;
//		return true;
	}
	public static boolean isBeichi(Line line, Point start, Point end, List<HistoryRecord> list,double preDiff,double afterDiff) {

//		LineFeature f1 = computLine(line, list);
//		LineFeature f2 = computPoint(start, end, list);
//
//		if (f1.getPriceStep() > f2.getPriceStep()*2) {
//
//			return true;
//		}
//		return false;
		return true;
	}

	public static LineFeature computLine(Line line, List<HistoryRecord> list) {
		LineFeature feature = new LineFeature();
		// 向下
		Point startPoint = line.getStartPoint();
		Point endPoint = line.getEndPoint();
		int knum = 0;
		double volume = 0;
		boolean flag = false;
		for (HistoryRecord record : list) {

			if (!flag) {

				if (record.getEndTime().equals(startPoint.getTime())) {

					flag = true;

				}
				continue;
			}

			if (record.getEndTime().endsWith(endPoint.getTime())) {

				break;
			}
			knum = knum + 1;
			volume = volume + record.getVolume();
		}
		feature.setStartPoint(startPoint);
		feature.setEndPoint(endPoint);
		feature.setkNum(knum);
		feature.setVolume(volume);
		feature.setPriceStep(Math.abs((line.getStartPoint().getPrice() - line.getEndPoint().getPrice()) * volume));
		return feature;
	}

	public static LineFeature computPoint(Point start, Point end, List<HistoryRecord> list) {

		LineFeature feature = new LineFeature();
		// 向下
		int knum = 0;
		double volume = 0;
		boolean flag = false;
		for (HistoryRecord record : list) {

			if (!flag) {

				if (record.getEndTime().equals(start.getTime())) {

					flag = true;

				}
				continue;
			}

			if (record.getEndTime().endsWith(end.getTime())) {

				break;
			}
			knum = knum + 1;
			volume = volume + record.getVolume();
		}
		feature.setkNum(knum);
		feature.setVolume(volume);
		feature.setPriceStep(Math.abs((start.getPrice() - end.getPrice()) * volume));
		return feature;
	}
}
