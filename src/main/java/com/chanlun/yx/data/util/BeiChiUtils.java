package com.chanlun.yx.data.util;

import java.util.List;

import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.dto.Line;
import com.chanlun.yx.data.dto.LineFeature;
import com.chanlun.yx.data.dto.Point;

public class BeiChiUtils {

	public static boolean isBeichi(Line lastLine, Line lastLine2, List<HistoryRecord> lists) {
		// TODO Auto-generated method stub
		
		
		
		return false;
	}
	
	public static LineFeature computV1(Line line, double lowPrice, double hightPrice, List<HistoryRecord> list) {
		
		
		
		return null;
	}
	
	public static LineFeature computV(Line line, double lowPrice, double hightPrice, List<HistoryRecord> list) {
		LineFeature feature = new LineFeature();
		boolean flag = false;
		int sumKnum = 0;
		double sumVol = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getTime().equals(line.getStartPoint().getTime())) {

				flag = true;
			}
			if (flag) {
				if (list.get(i).getHigh() < lowPrice) {

					sumKnum = 0;
					sumVol = 0;
				}
				if (list.get(i).getHigh() >= hightPrice) {
					// 第一次达到目的
					break;
				}
				sumKnum = sumKnum + 1;
				sumVol = sumVol + list.get(i).getVolume();
			}
		}
		feature.setkNum(sumKnum);
		feature.setVolume(sumVol);
		return feature;
	}

	public static boolean isBeichi(Line sl, List<Point> point, List<HistoryRecord> list2) {
		// TODO Auto-generated method stub
		return false;
	}
}
