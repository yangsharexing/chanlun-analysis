package com.chanlun.yx.data.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

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

	public static void test(String code, List<HistoryRecord> historyList, int index)
			throws IllegalAccessException, InvocationTargetException {

		List<HistoryRecord> list = new ArrayList<HistoryRecord>();

		if (historyList.size() < 500) {
			// k线不足500根不做处理
			return;
		}

		List<HistoryRecord> prelist = historyList.subList(0, index);
		List<HistoryRecord> afterlist = historyList.subList(index, historyList.size());
		boolean flag = true;
		double buyPrice = 0;
		for (HistoryRecord record : afterlist) {
			list.add(record);
			if (flag) {
				buyPrice = buy(list);

				if (buyPrice == 0) {
					continue;
				}
				flag = false;
			}
			
			//这里开始卖
			

		}

	}

	private static double buy(List<HistoryRecord> list) throws IllegalAccessException, InvocationTargetException {

		List<HistoryRecord> list2 = KLineUtils.handleKLine(list);

		List<Point> point = BiLineUtils.contructBiLines(list2);

		List<Point> point2 = LineUtils.bi2Line(point);

		List<TrendType> ttlist = ZhongShuUtils.findZhongShu(point2);

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
				double afterDiff = l.getStartPoint().getPrice() - l.getEndPoint().getPrice()
						- (l.getStartPoint().getPrice() - z.getDd());

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
				double afterDiff = currentPri - zs.getDd();

				if (preDiff / currentPri > 0.03 && afterDiff / currentPri > 0.03) {

					if (BeiChiUtils.isBeichi(sl, point, list2)) {
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
