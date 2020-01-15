package com.chanlun.yx.data.util;

import java.util.List;

import com.chanlun.yx.data.dto.BuyFeatrue;
import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.dto.Line;
import com.chanlun.yx.data.dto.LineFeature;
import com.chanlun.yx.data.dto.Point;
import com.chanlun.yx.data.dto.ZhongShu;

public class BeiChiUtils {

	// 根据回归模型判断配置
//	public static boolean isBeichiTrue() {
//
////    	首先 afterDiff 3次回归模型函数49.78 x(3) - 16.44 x(2) + 1.657 x(1) + 0.02503
//
//	}

	public static boolean isBeichi(Line line1, Line line2, List<HistoryRecord> list) {
		LineFeature f1 = computLine(line1, list);
		LineFeature f2 = computLine(line2, list);

		if (f1.getPriceStep() > f2.getPriceStep() * 2) {

			return true;
		}
		return false;
//		return true;
	}

	// 最后一个参数 表示 中枢+连接 走势的最低点
	public static boolean isBeichi(ZhongShu preZSLine, ZhongShu afterZS, Line line1, Line line2,
			List<HistoryRecord> list, double preDiff, double afterDiff, BuyFeatrue buyf, Point point) {

		LineFeature f1 = computLineV2(preZSLine, afterZS, line1, list, 1, null);
		LineFeature f2 = computLineV2(preZSLine, afterZS, line2, list, 2, point);

		buyf.setPreLineFeature(f1);
		buyf.setAfterLineFeature(f2);
		buyf.setPreDiff(preDiff);
		buyf.setAfterDiff(afterDiff);
		buyf.setPreMacd(f1.getMacd());
		buyf.setAfterMacd(f2.getMacd());

		// x3=volume （前成交/后成交）
		// x4=preDiff
		// x5=afterDiff
		// x6=preZSLineNum
		// x7=afterZSLineNum
//		
//		if(SingleX(afterDiff)>0.05 && SingleX5(f1.getVolume()/f2.getVolume(), preDiff, afterDiff, preZSLine.getNum(), afterZS.getNum())>0.05) {
//			
//			if(afterDiff>0.022 &&preDiff>0.015) {
//				
//				if(preZSLine.getNum()/afterZS.getNum()<0.8 ) {
//					
//					return true;
//				}
//			}
//		}

//		if (Math.abs(f1.getMacd()) > Math.abs(f2.getMacd() * 3) && afterDiff > 0.00) {
//			if(buyf.getPreZhongshu().getNum()>=9 && buyf.getAfterZhongshu().getNum()<9) {
//				
//				if(buyf.getPreLineFeature().getkNum()>=90) {
//					
//					if(f1.getMacd()>0 && afterDiff>0) {
//						
//						return true;
//					}
//					
//				}
//			}
//		}
		return true;

//		return false;
	}

	public static boolean isBeichi(Line line, Point start, Point end, List<HistoryRecord> list) {

		LineFeature f1 = computLine(line, list);
		LineFeature f2 = computPoint(start, end, list);

		if (f1.getPriceStep() > f2.getPriceStep() * 2) {

			return true;
		}
		return false;
//		return true;
	}

	public static boolean isBeichi(ZhongShu preZSLine, ZhongShu afterZS, Line line, Point start, Point end,
			List<HistoryRecord> list, double preDiff, double afterDiff) {

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
		double macd = 0;
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
			macd = macd + record.getBar();
			volume = volume + record.getVolume();
		}
		feature.setStartPoint(startPoint);
		feature.setEndPoint(endPoint);
		feature.setkNum(knum);
		feature.setVolume(volume);
		feature.setPriceStep(Math.abs((line.getStartPoint().getPrice() - line.getEndPoint().getPrice()) * volume));
		feature.setMacd(macd);
		return feature;
	}

	public static LineFeature computLineV2(ZhongShu preZSLine, ZhongShu afterZS, Line line, List<HistoryRecord> list,
			int type, Point point) {
		LineFeature feature = new LineFeature();
		Point startPoint = line.getStartPoint();
		Point endPoint = line.getEndPoint();
		int knum = 0;
		double volume = 0;
		double macd = 0;
		int startIndex = 0;
		int endIndex = 0;
		
		for(int i=0;i<list.size();i++) {
			if(list.get(i).getEndTime().equals(startPoint.getTime())) {
				startIndex = i;
				for(int j=i;j<list.size();j++) {
					if(list.get(j).getEndTime().equals(endPoint.getTime())) {
						endIndex = j;
					}
					if(endIndex==0) {
						endIndex = list.size()-1;
					}
				}
			}
		}
		for (int i = startIndex; i <= endIndex; i++) {
			HistoryRecord record = list.get(i);
			knum = knum + 1;
			macd = macd + record.getBar();
			volume = volume + record.getVolume();
		}
		
		// 往后面倒退数，发一个第一个大于前ZS dd的，的点为起始点
		feature.setStartPoint(startPoint);
		feature.setEndPoint(endPoint);
		feature.setkNum(knum);
		feature.setVolume(volume);
		feature.setPriceStep(Math.abs((line.getStartPoint().getPrice() - line.getEndPoint().getPrice()) * volume));
		feature.setMacd(macd);
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

	private static double SingleX(double value) {

//		return 49.78 * Math.pow(value, 3) - 16.44 * Math.pow(value, 2) + 1.657 * Math.pow(value, 1) + 0.02503;

		return 49.78 * Math.pow(value, 3) - 16.44 * Math.pow(value, 2) + 1.657 * Math.pow(value, 1) + 0.02503;
//		return  -903.7*Math.pow(value, 4)+ 431.1 * Math.pow(value, 3) - 61.33 * Math.pow(value, 2) + 3.103 * Math.pow(value, 1) + 0.01902;

	}

	private static double SingleX5(double volume, double preDiff, double afterDiff, double preZSLineNum,
			double afterZSLineNum) {

		// Parameters: const -0.003672
		// x3 0.000382
		// x4 0.095904
		// x5 0.649656
		// x6 0.000263
		// x7 0.000835
		//
		// x3=volume （前成交/后成交）
		// x4=preDiff
		// x5=afterDiff
		// x6=preZSLineNum
		// x7=afterZSLineNum

		return 0.000382 * volume + 0.095904 * preDiff + 0.649656 * afterDiff + 0.000263 * preZSLineNum
				+ 0.000835 * afterZSLineNum - 0.003672;
	}

	private static double SingleX2(double volume, double preDiff, double afterDiff, double preZSLineNum,
			double afterZSLineNum) {

		// Parameters: const -0.003672
		// x3 0.000382
		// x4 0.095904
		// x5 0.649656
		// x6 0.000263
		// x7 0.000835
		//
		// x3=volume （前成交/后成交）
		// x4=preDiff
		// x5=afterDiff
		// x6=preZSLineNum
		// x7=afterZSLineNum

		return 0.000382 * volume + 0.095904 * preDiff + 0.649656 * afterDiff + 0.000263 * preZSLineNum
				+ 0.000835 * afterZSLineNum - 0.003672;
	}

//	0.017664646916189998
	public static void main(String[] args) {
//		0.0076
//		0.1587
//		0.073
//		0.02
//		0.0725

		System.out.println(SingleX5(3.218955448, 0.52, 0, 6, 7));
		System.out.println(SingleX(0.1));
	}

}