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
import com.chanlun.yx.redis.RedisUtils;

/**
 * 股票走势测试（线段级别）
 * 
 * @author Administrator
 *
 */
public class StockTest4 {

	public static List<String> tradeList = new ArrayList<String>();// 20200101
																	// 格式
	public static List<String> tradeList2 = new ArrayList<String>();// 2020-01-01
																	// 格式

	static {
		List<HistoryRecord> list = RedisUtils.fetchData("java.sh.600000");
		for (HistoryRecord record : list) {
			if (!tradeList.contains(record.getStartTime().substring(0, 8))) {
				tradeList.add(record.getStartTime().substring(0, 8));
			}
		}

		tradeList.add("20200110");
		// 排序
		for (int i = 0; i < tradeList.size(); i++) {

			for (int j = i + 1; j < tradeList.size(); j++) {

				Long jv = Long.parseLong(tradeList.get(j));
				Long iv = Long.parseLong(tradeList.get(i));

				if (iv > jv) {
					tradeList.set(i, jv + "");
					tradeList.set(j, iv + "");
				}
			}

		}

		for (int i = 0; i < tradeList.size(); i++) {

			String timeStr = tradeList.get(i);

			String time = timeStr.substring(0, 4) + "-" + timeStr.substring(4, 6) + "-" + timeStr.substring(6, 8);

			tradeList2.add(time);
		}
	}

	
	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {
		
		List<String> codes = RedisUtils.getAllKeys();
		for(String code:codes) {
			List<HistoryRecord> list = RedisUtils.fetchData(code);
			test(list);
		}
		
		
	}
	
	public static void test(List<HistoryRecord> historyList)
			throws IllegalAccessException, InvocationTargetException {

		TechnicalIndexUtils.computeMacd(historyList);

		List<HistoryRecord> list = new ArrayList<HistoryRecord>();

		// 寻找买点
		List<HistoryRecord> simpleKLines = KLineUtils.handleKLine(list);
		List<Point> lines = BiLineUtils.contructBiLines(simpleKLines);
		// List<Point> lines = LineUtils.bi2Line(biLines);
		List<TrendType> trendList = ZhongShuUtils.findZhongShu(lines);

		// 买入
		buy(list, simpleKLines, lines, trendList);
	}

	// buyPrice = buy(list, simpleKLines, biLines, lines, trendList);
	private static void buy(List<HistoryRecord> list, List<HistoryRecord> list2, List<Point> lines,
			List<TrendType> trendList) throws IllegalAccessException, InvocationTargetException {

		BuyFeatrue buyf = new BuyFeatrue();
		if(trendList != null) {
			
			System.out.println(trendList.size());
		}
		// 中枢或走势连接必须大于4个
		if (trendList != null && trendList.size() > 4) {

			// 最后一个中枢或者连接
			TrendType lastTrend = trendList.get(trendList.size() - 1);

			// 判断最后一个是中枢 还是 连接
			if (lastTrend instanceof Line) {

				Line lastTrendLine = (Line) lastTrend;

				if (lastTrendLine.getDirect() == 1) {
					// 下线段离开中枢，直接pass
					buyf.setPrice(0);
					return ;
				}

				TrendType beforeTrend = trendList.get(trendList.size() - 3);
				Line beforeTrendLine = (Line) beforeTrend;
				if (beforeTrendLine.getDirect() == 1) {
					// 倒数第二个中枢连接是向上的，直接pass
					buyf.setPrice(0);
					return ;
				}

				// 此时的走势必然是 中枢 ---向上--->中枢---向上--->
				// 计算连个中枢连接的信息，判断是否背驰

				// 获取第一个中枢（中枢 ---向上--->中枢---向上---> 走势）
				ZhongShu beforeTrendZS = (ZhongShu) trendList.get(trendList.size() - 4);

				// 获取第二个中枢（中枢 ---向上--->中枢---向上---> 走势）
				ZhongShu lastTrendSz = (ZhongShu) trendList.get(trendList.size() - 2);

				double preDiff = beforeTrendLine.getStartPoint().getPrice() - beforeTrendLine.getEndPoint().getPrice()
						- -(beforeTrendLine.getStartPoint().getPrice() - beforeTrendZS.getDd());

				double afterDiff = Math
						.abs(lastTrendLine.getStartPoint().getPrice() - lastTrendLine.getEndPoint().getPrice()
								- (lastTrendLine.getStartPoint().getPrice() - lastTrendSz.getDd()));

				// 计算几个锚点
				double price = beforeTrendLine.getStartPoint().getPrice();
				buyf.setPreZhongshu(beforeTrendZS);
				buyf.setAfterZhongshu(lastTrendSz);

				// 两次离开中枢的幅度要超过一个固定的数目
				if (BeiChiUtils2.isBeichi(beforeTrendZS, lastTrendSz, beforeTrendLine, lastTrendLine, list,
						preDiff / price, afterDiff / price, buyf, lines)) {
					// 买入价格
					double buyPrice = list.get(list.size() - 1).getHigh();

					// System.out.println("过滤掉跌的买入---");
					if (buyPrice < lastTrendSz.getGg()) {

						buyf.setPrice(buyPrice);

						buyf.setPreZhongshuLineNum(beforeTrendZS.getNum());
						buyf.setAfterZhongshuLineNum(lastTrendSz.getNum());
						buyf.setPressPrice(lastTrendLine.getEndPoint().getPrice());
						System.out.println("AAAAAAAAAAAAAAAAAA");

					}
				}
			}
		}
	}

	public static int dayStep(String endTime, String startTime) {

		if (startTime.length() > 10) {

			startTime = startTime.substring(0, 8);
			endTime = endTime.substring(0, 8);
			int start = 0;
			;
			int end = 0;
			for (int i = 0; i < tradeList.size(); i++) {
				if (startTime.equals(tradeList.get(i))) {
					// 开始计数
					start = i;
				}
				if (endTime.equals(tradeList.get(i))) {
					// 开始计数
					end = i;
					break;
				}
			}
			return end - start;
		} else {

			int start = 0;
			int end = 0;
			for (int i = 0; i < tradeList2.size(); i++) {
				if (startTime.equals(tradeList2.get(i))) {
					// 开始计数
					start = i;
				}
				if (endTime.equals(tradeList2.get(i))) {
					// 开始计数
					end = i;
					break;
				}
			}
			return end - start;
		}
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
