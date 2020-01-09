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

/**
 * 股票走势测试（线段级别）
 * 
 * @author Administrator
 *
 */
public class StockTest2 {

	public static int test(String code, List<HistoryRecord> historyList, int index, List<HLDto> hlList)
			throws IllegalAccessException, InvocationTargetException {

		HLDto hlDto = null;
		List<HistoryRecord> list = new ArrayList<HistoryRecord>();
		List<HistoryRecord> afterlist = new ArrayList<HistoryRecord>();
		if (historyList.size() < 500) {
			// k线不足500根不做处理
			return 0;
		}
		if (index >= historyList.size() - 10) {

			// 未来数据不足10个k 也不做处理
			return 0;
		}

		// 历史存量数据
		list = historyList.subList(0, index);

		// 未来数据
		for (HistoryRecord his : historyList.subList(index, historyList.size())) {
			afterlist.add(his);
		}

		boolean flag = true;
		double buyPrice = 0;
		int pointNum = 0;
		int zoushiNum = 0;
		String buyTm = "";// 买入时间
		ZhongShu lastZhongshu = null;
		BuyFeatrue buyfeature = null;
		for (int j = 0; j < afterlist.size(); j++) {
			HistoryRecord record = afterlist.get(j);
			list.add(record);

			// 寻找买点
			if (flag) {
				List<HistoryRecord> simpleKLines = KLineUtils.handleKLine(list);
				List<Point> biLines = BiLineUtils.contructBiLines(simpleKLines);
				List<Point> lines = LineUtils.bi2Line(biLines);
				List<TrendType> trendList = ZhongShuUtils.findZhongShu(lines);

				// 买入
				buyfeature = buy(list, simpleKLines, biLines, lines, trendList);
				buyPrice = buyfeature.getPrice();
				if (buyPrice == 0) {
					continue;
				}

				zoushiNum = trendList.size();
				pointNum = lines.size();

				if (trendList.get(trendList.size() - 1) instanceof ZhongShu) {

					lastZhongshu = (ZhongShu) trendList.get(trendList.size() - 1);

				} else {

					lastZhongshu = (ZhongShu) trendList.get(trendList.size() - 2);
				}
				buyTm = record.getEndTime();
				flag = false;
			} else {
				// 这里开始卖
				List<HistoryRecord> simpleKLines = KLineUtils.handleKLine(list);
				List<Point> biLines = BiLineUtils.contructBiLines(simpleKLines);
				List<Point> lines = LineUtils.bi2Line(biLines);
				if (lines.size() <= pointNum) {
					continue;
				}
				List<TrendType> ttlist = ZhongShuUtils.findZhongShu(lines);

				// 最后两点
				Point a1 = lines.get(lines.size() - 1);// 最后一点
				Point a2 = lines.get(lines.size() - 2);// 最后第二点

				if (a1.getPrice() < a2.getPrice() && a2.getPrice() > lastZhongshu.getZd())

					if (a1.getPrice() < lastZhongshu.getGg()) {

						// 最后一个中枢升级（说明走势已经在原来中枢波动区间 回调了）此时就是买点 ，因为已经进入一个更大级别的中枢
						hlDto = new HLDto();
						hlDto.setCode(code);
						hlDto.setBuy(buyPrice);
						hlDto.setSale(record.getLow());
						hlDto.setDayNum(CommonUtils.caculateTotalTime(record.getEndTime(), buyTm));
						hlDto.setStartTm(buyTm);
						hlDto.setEndTm(record.getEndTime());
						hlDto.setYinliRate((record.getLow() - buyPrice) / buyPrice);
						hlDto.setPreKLine(buyfeature.getPreLineFeature().getkNum());
						hlDto.setPreVolume(buyfeature.getPreLineFeature().getVolume());
						hlDto.setPrePriceStep(buyfeature.getPreLineFeature().getPriceStep());
						
						hlDto.setAfterKLine(buyfeature.getAfterLineFeature().getkNum());
						hlDto.setAfterVolume(buyfeature.getAfterLineFeature().getVolume());
						hlDto.setAfterPriceStep(buyfeature.getAfterLineFeature().getPriceStep());
						
						hlDto.setPreDiff(buyfeature.getPreDiff());
						hlDto.setAfterDiff(buyfeature.getAfterDiff());
						
						hlDto.setPreZhongshuLineNum(buyfeature.getPreZhongshuLineNum());
						hlDto.setAfterZhongshuLineNum(buyfeature.getAfterZhongshuLineNum());
//						System.out.println(hlDto);
						hlList.add(hlDto);

						return list.size() + j;
					}

				if (ttlist.size() > zoushiNum) {

					if (ttlist.get(ttlist.size() - 1) instanceof ZhongShu) {
						ZhongShu salelastZhongshu = (ZhongShu) ttlist.get(ttlist.size() - 1);

						if (salelastZhongshu.getDd() > lastZhongshu.getGg()) {

							hlDto = new HLDto();
							hlDto.setCode(code);
							hlDto.setBuy(buyPrice);
							hlDto.setSale(record.getLow());
							hlDto.setDayNum(CommonUtils.caculateTotalTime(record.getEndTime(), buyTm));
							hlDto.setStartTm(buyTm);
							hlDto.setEndTm(record.getEndTime());
							hlDto.setYinliRate((record.getLow() - buyPrice) / buyPrice);
							
							hlDto.setPreKLine(buyfeature.getPreLineFeature().getkNum());
							hlDto.setPreVolume(buyfeature.getPreLineFeature().getVolume());
							hlDto.setPrePriceStep(buyfeature.getPreLineFeature().getPriceStep());
							
							hlDto.setAfterKLine(buyfeature.getAfterLineFeature().getkNum());
							hlDto.setAfterVolume(buyfeature.getAfterLineFeature().getVolume());
							hlDto.setAfterPriceStep(buyfeature.getAfterLineFeature().getPriceStep());
							
							hlDto.setPreDiff(buyfeature.getPreDiff());
							hlDto.setAfterDiff(buyfeature.getAfterDiff());
							
							hlDto.setPreZhongshuLineNum(buyfeature.getPreZhongshuLineNum());
							hlDto.setAfterZhongshuLineNum(buyfeature.getAfterZhongshuLineNum());
							
//							System.out.println(hlDto);
							hlList.add(hlDto);
							return list.size() + j;
						}
					}
				}

				// 风险控制
				// if (CommonUtils.caculateTotalTime(record.getEndTime(), buyTm)
				// > 30) {
				//
				// if ((record.getLow() - buyPrice) / buyPrice < 0.01) {
				//
				// System.out.println(code + " " + buyTm + "买入" + buyPrice + " "
				// + record.getEndTime() + "卖出"
				// + record.getLow() + "获利" + (record.getLow() - buyPrice) /
				// buyPrice + " index"
				// + index);
				// System.out.println("持仓天数（超过" + 30 + "） --- "
				// + CommonUtils.caculateTotalTime(record.getEndTime(), buyTm));
				// hl.setHl(hl.getHl() + ((record.getLow() - buyPrice) /
				// buyPrice) * 10000);
				// hl.setNum(hl.getNum() + 1);
				// hl.setDayNum(hl.getDayNum() +
				// CommonUtils.caculateTotalTime(record.getEndTime(), buyTm));
				// if ((record.getLow() - buyPrice) / buyPrice > 0.002) {
				// hl.setHlNum(hl.getHlNum() + 1);
				// }
				// System.out.println("总获利" + hl.getHl());
				// return list.size() + j;
				// }
				//
				// }

				if (j == afterlist.size() - 1) {

					hlDto = new HLDto();
					hlDto.setCode(code);
					hlDto.setBuy(buyPrice);
					hlDto.setSale(record.getLow());
					hlDto.setDayNum(CommonUtils.caculateTotalTime(record.getEndTime(), buyTm));
					hlDto.setStartTm(buyTm);
					hlDto.setEndTm(record.getEndTime());
					hlDto.setYinliRate((record.getLow() - buyPrice) / buyPrice);
					
					hlDto.setPreKLine(buyfeature.getPreLineFeature().getkNum());
					hlDto.setPreVolume(buyfeature.getPreLineFeature().getVolume());
					hlDto.setPrePriceStep(buyfeature.getPreLineFeature().getPriceStep());
					
					hlDto.setAfterKLine(buyfeature.getAfterLineFeature().getkNum());
					hlDto.setAfterVolume(buyfeature.getAfterLineFeature().getVolume());
					hlDto.setAfterPriceStep(buyfeature.getAfterLineFeature().getPriceStep());
					
					
					
					hlDto.setPreDiff(buyfeature.getPreDiff());
					hlDto.setAfterDiff(buyfeature.getAfterDiff());
					
					hlDto.setPreZhongshuLineNum(buyfeature.getPreZhongshuLineNum());
					hlDto.setAfterZhongshuLineNum(buyfeature.getAfterZhongshuLineNum());
//					System.out.println(hlDto);
					hlList.add(hlDto);
					return 0;
				}
			}
		}
		return 0;
	}

	// buyPrice = buy(list, simpleKLines, biLines, lines, trendList);
	private static BuyFeatrue buy(List<HistoryRecord> list, List<HistoryRecord> list2, List<Point> point, List<Point> lines,
			List<TrendType> trendList) throws IllegalAccessException, InvocationTargetException {
		
		BuyFeatrue buyf = new BuyFeatrue();
		
		// 中枢或走势连接必须大于4个
		if (trendList != null && trendList.size() > 3) {

			// 最后一个中枢或者连接
			TrendType lastTrend = trendList.get(trendList.size() - 1);

			// 判断最后一个是中枢 还是 连接
			if (lastTrend instanceof Line) {

				Line lastTrendLine = (Line) lastTrend;
				if (lastTrendLine.getDirect() == 1) {
					// 向上线段离开中枢，直接pass
					buyf.setPrice(0);
					return buyf;
				}

				TrendType beforeTrend = trendList.get(trendList.size() - 3);
				Line beforeTrendLine = (Line) beforeTrend;
				if (beforeTrendLine.getDirect() == 1) {
					// 倒数第二个中枢连接是向上的，直接pass
					buyf.setPrice(0);
					return buyf;
				}

				// 此时的走势必然是 中枢 ---向下--->中枢---向下--->
				// 计算连个中枢连接的信息，判断是否背驰

				// 获取第一个中枢（中枢 ---向下--->中枢---向下---> 走势）
				ZhongShu beforeTrendZS = (ZhongShu) trendList.get(trendList.size() - 4);

				// 获取第二个中枢（中枢 ---向下--->中枢---向下---> 走势）
				ZhongShu lastTrendSz = (ZhongShu) trendList.get(trendList.size() - 2);

				double preDiff = beforeTrendLine.getStartPoint().getPrice() - beforeTrendLine.getEndPoint().getPrice()
						- (beforeTrendLine.getStartPoint().getPrice() - beforeTrendZS.getDd());

				double afterDiff = Math
						.abs(lastTrendLine.getStartPoint().getPrice() - lastTrendLine.getEndPoint().getPrice()
								- (lastTrendLine.getStartPoint().getPrice() - lastTrendSz.getDd()));

				// 计算几个锚点
				double price = beforeTrendLine.getStartPoint().getPrice();

				// 两次离开中枢的幅度要超过一个固定的数目
				if (BeiChiUtils.isBeichi(beforeTrendLine, lastTrendLine, list2, preDiff / price, afterDiff / price,buyf)) {
					// 买入价格
//					System.out.println(preDiff / price + "---" + afterDiff / price);
					double buyPrice = list.get(list.size() - 1).getHigh();
					buyf.setPrice(buyPrice);
					buyf.setPreZhongshuLineNum(beforeTrendZS.getNum());
					buyf.setAfterZhongshuLineNum(lastTrendSz.getNum());
//					System.out.println("买入- "+list.get(list.size() - 1).getCode()+"   "+preDiff / price+"--- "+afterDiff / price+"--- "+beforeTrendZS.getNum()+"--- "+lastTrendSz.getNum());
					return buyf;
				}
			} else {

				/*
				 * // 获取最后一个中枢（中枢 ---向下--->中枢---向下 走势） ZhongShu lastTrendZs =
				 * (ZhongShu) lastTrend;
				 * 
				 * double currentPrice = lines.get(lines.size() - 1).getPrice();
				 * if (currentPrice >= lastTrendZs.getDd()) { // 中枢最后一笔比dd高
				 * return 0; }
				 * 
				 * Line lastTrendTypeLine = (Line)trendList.get(trendList.size()
				 * - 2); if (lastTrendTypeLine.getDirect() == 1) { // 向上 return
				 * 0; }
				 * 
				 * ZhongShu beforeTrendTypeZs = (ZhongShu)
				 * trendList.get(trendList.size() - 3);
				 * 
				 * 
				 * // 计算两者真实的价格区间 double preDiff =
				 * lastTrendTypeLine.getStartPoint().getPrice() -
				 * lastTrendTypeLine.getEndPoint().getPrice() -
				 * (lastTrendTypeLine.getStartPoint().getPrice() -
				 * beforeTrendTypeZs.getDd());
				 * 
				 * double afterDiff = Math.abs(currentPrice -
				 * lastTrendZs.getDd());
				 * 
				 * if (preDiff / currentPrice > 0.05 && afterDiff / currentPrice
				 * >0.05) {
				 * 
				 * 
				 * if (BeiChiUtils.isBeichi(lastTrendTypeLine,
				 * lines.get(lines.size() - 2), lines.get(lines.size() - 1),
				 * list)) { // 买入价格 double buyPrice = list.get(list.size() -
				 * 1).getHigh(); return buyPrice; } }
				 */
			}
		}
		buyf.setPrice(0);
		return buyf;
	}
}
