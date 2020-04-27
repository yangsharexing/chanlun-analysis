package com.chanlun.yx.data.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.chanlun.yx.data.dto.BuyFeatrue;
import com.chanlun.yx.data.dto.HLDto;
import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.dto.Line;
import com.chanlun.yx.data.dto.Point;
import com.chanlun.yx.data.dto.TrendType;
import com.chanlun.yx.data.dto.ZhongShu;

public class FindStockTest {
	
	static boolean flag = true;

	// 寻找三类买点
	public static void test(String code, List<HistoryRecord> historyList, int index, List<HLDto> hlList)
			throws IllegalAccessException, InvocationTargetException {
		if(flag) {
			String date = historyList.get(historyList.size()-1).getTime();
			System.out.println(date);
			flag = false;
		}

		// 计算macd
		TechnicalIndexUtils.computeMacd(historyList);

		// 寻找买点
		List<HistoryRecord> simpleKLines = KLineUtils.handleKLine(historyList);

		List<Point> biLines = BiLineUtils.contructBiLines(simpleKLines);

		List<Point> lines = LineUtils.bi2Line(biLines);
		List<TrendType> trendList = ZhongShuUtils.findZhongShu(lines);

		// 买入
		buy(historyList, simpleKLines, biLines, trendList);

	}

	// buyPrice = buy(list, simpleKLines, biLines, lines, trendList);
	private static BuyFeatrue buy(List<HistoryRecord> list, List<HistoryRecord> list2, List<Point> lines,
			List<TrendType> trendList) throws IllegalAccessException, InvocationTargetException {

		BuyFeatrue buyf = new BuyFeatrue();
		// 中枢或走势连接必须大于4个
		if (trendList != null && trendList.size() > 3) {

			// 最后一个中枢或者连接
			TrendType lastTrend = trendList.get(trendList.size() - 1);

			// 判断最后一个是中枢 还是 连接
			if (lastTrend instanceof Line) {

				Line lastTrendLine = (Line) lastTrend;

				if (lastTrendLine.getDirect() == 0) {
					// 向上线段离开中枢，直接pass
					buyf.setPrice(0);
					return buyf;
				}

				TrendType beforeTrend = trendList.get(trendList.size() - 3);
				Line beforeTrendLine = (Line) beforeTrend;
				if (beforeTrendLine.getDirect() == 0) {
					// 倒数第二个中枢连接是向上的，直接pass
					buyf.setPrice(0);
					return buyf;
				}
				
				//判断是否背驰
				double laststep  = lastTrendLine.getEndPoint().getPrice()-lastTrendLine.getStartPoint().getPrice();
				double beforestep = beforeTrendLine.getEndPoint().getPrice()-beforeTrendLine.getStartPoint().getPrice();
				
				int lastnum = lastTrendLine.getNum();
				int beforenum = beforeTrendLine.getNum();
				
				//比较斜率
				if(laststep/lastnum >beforestep/beforenum) {
					System.out.println("形态出现  [中枢 ---向下--->中枢]  " + list.get(0).getCode());
					System.out.println(beforeTrendLine.getStartPoint().getTime()+" bef--> "+beforeTrendLine.getEndPoint().getTime());
					System.out.println(lastTrendLine.getStartPoint().getTime()+" aft--> "+lastTrendLine.getEndPoint().getTime());
				}
				
				
				
			} else {

//				// 获取最后一个中枢（中枢 ---向下--->中枢---向下 走势）
//				ZhongShu lastTrendZs = (ZhongShu) lastTrend;
//				double currentPrice = lines.get(lines.size() - 1).getPrice();
//				if (currentPrice >= lastTrendZs.getDd()) {
//					return null;
//				}
//				Line lastTrendTypeLine = (Line) trendList.get(trendList.size() - 2);
//				if (lastTrendTypeLine.getDirect() == 1) {
//					return null;
//				}
//				System.out.println("形态出现  [中枢 ---向下--->中枢---向下]  " + list.get(0).getCode());
			}
		}
		return null;

	}

	public static double min(double a, double b) {

		if (a >= b) {
			return b;
		} else {
			return a;
		}
	}

	public static double max(double a, double b) {

		if (a >= b) {
			return a;
		} else {
			return b;
		}
	}

}
