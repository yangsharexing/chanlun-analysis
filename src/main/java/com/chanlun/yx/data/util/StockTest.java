package com.chanlun.yx.data.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.chanlun.yx.data.dto.HL;
import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.dto.Line;
import com.chanlun.yx.data.dto.Point;
import com.chanlun.yx.data.dto.TrendType;
import com.chanlun.yx.data.dto.ZhongShu;

/**
 * 股票走势测试（线段级别）
 * 
 * @author Administrator
 *
 */
public class StockTest {

	public static int test(String code, List<HistoryRecord> historyList, int index, HL hl, int maxDay)
			throws IllegalAccessException, InvocationTargetException {

		List<HistoryRecord> list = new ArrayList<HistoryRecord>();
		List<HistoryRecord> afterlist = new ArrayList<HistoryRecord>();
		if (historyList.size() < 500) {
			// k线不足500根不做处理
			return 0;
		}
		if (index >= historyList.size() - 10) {
			return 0;
		}
		list = historyList.subList(0, index);
		for (HistoryRecord his : historyList.subList(index, historyList.size())) {
			afterlist.add(his);
		}
		boolean flag = true;
		double buyPrice = 0;
		int pointNum = 0;
		int zoushiNum = 0;
		String buyTm = "";
		ZhongShu lastZhongshu = null;
		for (int j = 0; j < afterlist.size(); j++) {
			HistoryRecord record = afterlist.get(j);
			list.add(record);
			if (flag) {
				List<HistoryRecord> list2 = KLineUtils.handleKLine(list);
				List<Point> point = BiLineUtils.contructBiLines(list2);
				List<Point> point2 = LineUtils.bi2Line(point);
				List<TrendType> ttlist = ZhongShuUtils.findZhongShu(point2);
				buyPrice = buy(list, list2, point, point2, ttlist);

				if (buyPrice == 0) {
					continue;
				}
				zoushiNum = ttlist.size();
				pointNum = point2.size();

				if (ttlist.get(ttlist.size() - 1) instanceof ZhongShu) {
					lastZhongshu = (ZhongShu) ttlist.get(ttlist.size() - 1);
				} else {
					lastZhongshu = (ZhongShu) ttlist.get(ttlist.size() - 2);
				}
				buyTm = record.getEndTime();
				flag = false;
			} else {
				// 这里开始卖
				List<HistoryRecord> list2 = KLineUtils.handleKLine(list);
				List<Point> point = BiLineUtils.contructBiLines(list2);
				List<Point> point2 = LineUtils.bi2Line(point);
				if (point2.size() <= pointNum) {
					continue;
				}

				List<TrendType> ttlist = ZhongShuUtils.findZhongShu(point2);

				// 最后两点
				Point a1 = point2.get(point2.size() - 1);
				Point a2 = point2.get(point2.size() - 2);

				if (a1.getPrice() < a2.getPrice() && a2.getPrice() > lastZhongshu.getZd())

					if (a1.getPrice() < lastZhongshu.getGg()) {

						// 最后一个中枢升级（说明走势已经在原来中枢波动区间 回调了）此时就是买点 ，因为已经进入一个更大级别的中枢
						System.out.println(code + "  " + buyTm + "买入" + buyPrice + "  " + record.getEndTime() + "卖出"
								+ record.getLow() + "获利" + (record.getLow() - buyPrice) / buyPrice + "   index"
								+ index);
						System.out.println("持仓天数  --- " + CommonUtils.caculateTotalTime(record.getEndTime(), buyTm));
						hl.setHl(hl.getHl() + ((record.getLow() - buyPrice) / buyPrice) * 10000);
						hl.setNum(hl.getNum() + 1);
						hl.setDayNum(hl.getDayNum() + CommonUtils.caculateTotalTime(record.getEndTime(), buyTm));
						if((record.getLow() - buyPrice) / buyPrice>0.002){
							hl.setHlNum(hl.getHlNum()+1);
						}
						
						System.out.println("总获利" + hl.getHl());
						return list.size() + j;
					}

				if (ttlist.size() > zoushiNum) {

					if (ttlist.get(ttlist.size() - 1) instanceof ZhongShu) {
						ZhongShu salelastZhongshu = (ZhongShu) ttlist.get(ttlist.size() - 1);

						if (salelastZhongshu.getDd() > lastZhongshu.getGg()) {

							System.out.println(code + "  " + buyTm + "买入" + buyPrice + "  " + record.getEndTime() + "卖出"
									+ record.getLow() + "获利" + (record.getLow() - buyPrice) / buyPrice + "   index"
									+ index);
							System.out
									.println("持仓天数  --- " + CommonUtils.caculateTotalTime(record.getEndTime(), buyTm));
							hl.setHl(hl.getHl() + ((record.getLow() - buyPrice) / buyPrice) * 10000);
							hl.setNum(hl.getNum() + 1);
							hl.setDayNum(hl.getDayNum() + CommonUtils.caculateTotalTime(record.getEndTime(), buyTm));
							if((record.getLow() - buyPrice) / buyPrice>0.002){
								hl.setHlNum(hl.getHlNum()+1);
							}
							System.out.println("总获利" + hl.getHl());
							return list.size() + j;
						}
					}
				}

				if (CommonUtils.caculateTotalTime(record.getEndTime(), buyTm) > maxDay) {

					if ((record.getLow() - buyPrice) / buyPrice < 0.01) {

						System.out.println(code + "  " + buyTm + "买入" + buyPrice + "  " + record.getEndTime() + "卖出"
								+ record.getLow() + "获利" + (record.getLow() - buyPrice) / buyPrice + "   index"
								+ index);
						System.out.println("持仓天数（超过" + maxDay + "）  --- "
								+ CommonUtils.caculateTotalTime(record.getEndTime(), buyTm));
						hl.setHl(hl.getHl() + ((record.getLow() - buyPrice) / buyPrice) * 10000);
						hl.setNum(hl.getNum() + 1);
						hl.setDayNum(hl.getDayNum() + CommonUtils.caculateTotalTime(record.getEndTime(), buyTm));
						if((record.getLow() - buyPrice) / buyPrice>0.002){
							hl.setHlNum(hl.getHlNum()+1);
						}
						System.out.println("总获利" + hl.getHl());
						return list.size() + j;
					}

				}

				if (j == afterlist.size() - 1) {

					System.out.println(code + "  " + buyTm + "买入" + buyPrice + "  " + record.getEndTime() + "卖出"
							+ record.getLow() + "获利" + (record.getLow() - buyPrice) / buyPrice + "   index" + index);
					System.out.println("持仓天数 (到期) --- " + CommonUtils.caculateTotalTime(record.getEndTime(), buyTm));
					hl.setHl(hl.getHl() + ((record.getLow() - buyPrice) / buyPrice) * 10000);
					hl.setNum(hl.getNum() + 1);
					hl.setDayNum(hl.getDayNum() + CommonUtils.caculateTotalTime(record.getEndTime(), buyTm));
					if((record.getLow() - buyPrice) / buyPrice>0.002){
						hl.setHlNum(hl.getHlNum()+1);
					}
					System.out.println("总获利" + hl.getHl());
					return 0;
				}
			}

		}
		return 0;
	}

	private static double buy(List<HistoryRecord> list, List<HistoryRecord> list2, List<Point> point,
			List<Point> point2, List<TrendType> ttlist) throws IllegalAccessException, InvocationTargetException {

		if (ttlist != null && ttlist.size() > 3) {

			TrendType last = ttlist.get(ttlist.size() - 1);

			if (last instanceof Line) {

				Line l = (Line) last;
				if (l.getDirect() == 1) {
					// 向上的忽略
					return 0;
				}

				TrendType selast = ttlist.get(ttlist.size() - 3);
				Line sl = (Line) selast;
				if (sl.getDirect() == 1) {
					// 向上
					return 0;

				}
				// 比较背驰
				// 计算两者真实的价格区间
				ZhongShu sez = (ZhongShu) ttlist.get(ttlist.size() - 4);
				ZhongShu z = (ZhongShu) ttlist.get(ttlist.size() - 2);
				double preDiff = sl.getStartPoint().getPrice() - sl.getEndPoint().getPrice()
						- (sl.getStartPoint().getPrice() - sez.getDd());
				double afterDiff =Math.abs(l.getStartPoint().getPrice() - l.getEndPoint().getPrice()
						- (l.getStartPoint().getPrice() - z.getDd())) ;
				// 计算几个锚点
				double price = l.getStartPoint().getPrice();

				if (preDiff / price > 0.03 && afterDiff / price > 0.03) {
						if (BeiChiUtils.isBeichi(sl, l, list2)) {
							// 买入价格
							double buyPrice = list.get(list.size() - 1).getHigh();

							return buyPrice;
						}
				}

			} else {
				ZhongShu zs = (ZhongShu) last;
				double currentPri = point2.get(point2.size() - 1).getPrice();
				if (currentPri >= zs.getDd()) {
					// 中枢最后一笔比dd高
					return 0;
				}

				TrendType selast = ttlist.get(ttlist.size() - 2);
				Line sl = (Line) selast;
				if (sl.getDirect() == 1) {
					// 向上
					return 0;
				}
				ZhongShu sez = (ZhongShu) ttlist.get(ttlist.size() - 3);
				// 计算两者真实的价格区间
				double preDiff = sl.getStartPoint().getPrice() - sl.getEndPoint().getPrice()
						- (sl.getStartPoint().getPrice() - sez.getDd());
				double afterDiff = Math.abs(currentPri - zs.getDd());

				if (preDiff / currentPri > 0.03 && afterDiff / currentPri > 0.03) {
						if (BeiChiUtils.isBeichi(sl, point2.get(point2.size() - 2), point2.get(point2.size() - 1),
								list2)) {
							// 买入价格
							double buyPrice = list.get(list.size() - 1).getHigh();

							return buyPrice;
						}

				}

			}
		}
		return 0;

	}

}
