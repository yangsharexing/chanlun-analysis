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

	public static int test(String code, List<HistoryRecord> historyList, int index, List<HLDto> hlList)
			throws IllegalAccessException, InvocationTargetException {

		TechnicalIndexUtils.computeMacd(historyList);

		List<HistoryRecord> list = new ArrayList<HistoryRecord>();
		List<HistoryRecord> afterlist = new ArrayList<HistoryRecord>();
		if (historyList.size() < 50) {
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
		double preDd = 0;
		double preGg = 0;
		for (int j = 0; j < afterlist.size(); j++) {
			HistoryRecord record = afterlist.get(j);
			list.add(record);

			// 寻找买点
			if (flag) {
				List<HistoryRecord> simpleKLines = KLineUtils.handleKLine(list);
				List<Point> lines = BiLineUtils.contructBiLines(simpleKLines);
				// List<Point> lines = LineUtils.bi2Line(biLines);
				List<TrendType> trendList = ZhongShuUtils.findZhongShu(lines);

				// 买入
				buyfeature = buy(list, simpleKLines, lines, trendList);
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
				preDd = lastZhongshu.getDd();
				preGg = lastZhongshu.getGg();
				buyTm = record.getEndTime();
				flag = false;
			} else {

				if (buyTm.length() > 10) {
					// 如果当前点和买点在同一天则不处理
					if (list.get(list.size() - 1).getEndTime().substring(0, 8).equals(buyTm.substring(0, 8))) {

						continue;
					}
				} else {
					if (list.get(list.size() - 1).getEndTime().equals(buyTm)) {

						continue;
					}

				}

				// 这里开始卖
				List<HistoryRecord> simpleKLines = KLineUtils.handleKLine(list);
				List<Point> lines = BiLineUtils.contructBiLines(simpleKLines);
				// List<Point> lines = LineUtils.bi2Line(biLines);
				List<TrendType> ttlist = ZhongShuUtils.findZhongShu(lines);

				if (lines.size() <= pointNum) {
					continue;
				}

				if (lines.size() == pointNum + 1) {
					// 这里点的位置很关键
					Point point1 = lines.get(lines.size() - 1);
					Point point2 = lines.get(lines.size() - 2);
					Point point3 = lines.get(lines.size() - 3);

					if (record.getLow() > buyPrice && point2.getPrice() < lastZhongshu.getDd()) {

						// 回调没有回到中枢，走势弱，有机会立马跑
						setTradeRecord(code, hlList, buyPrice, buyTm, buyfeature, record, 0);
						return list.size() + j;
					}

					if (point1.getPrice() < point3.getPrice()) {
						// 创新低
						setTradeRecord(code, hlList, buyPrice, buyTm, buyfeature, record, 0);
						return list.size() + j;
					}
				}

				if (lines.size() == pointNum + 2) {
					Point point2 = lines.get(lines.size() - 2);
					if (point2.getPrice() > preDd && point2.getPrice() < preGg) {

						// 进入原中枢
						setTradeRecord(code, hlList, buyPrice, buyTm, buyfeature, record, 0);
						return list.size() + j;
					}

				}

				if (lines.size() == pointNum + 3) {
					Point point2 = lines.get(lines.size() - 2);
					Point point4 = lines.get(lines.size() - 4);
					if (point2.getPrice() < point4.getPrice()) {

						// 止损
						setTradeRecord(code, hlList, buyPrice, buyTm, buyfeature, record, 0);
						return list.size() + j;
					}

				}

				if (j == afterlist.size() - 1) {

					setTradeRecord(code, hlList, buyPrice, buyTm, buyfeature, record, 2);
					return 0;
				}
			}
		}
		return 0;
	}

	private static boolean risk(String code, List<HLDto> hlList, List<HistoryRecord> list, double buyPrice,
			String buyTm, BuyFeatrue buyfeature, int j, HistoryRecord record, List<Point> lines, int pointNum,
			int orgPointNum) {
    
		boolean flag = false;

		// // 风险控制 持仓第一天出现就跌了两个点
		// if (dayStep(record.getEndTime(), buyTm) == 1) {
		//
		// if ((record.getLow() - buyPrice) / buyPrice < -0.02) {
		// System.out.println("持仓1天亏损2个点");
		// setTradeRecord(code, hlList, buyPrice, buyTm, buyfeature, record, 1);
		// flag = true;
		// }
		// }
		// // 风险控制 持仓第2天出现就跌了两个点
		// if (dayStep(record.getEndTime(), buyTm) == 2) {
		//
		// if ((record.getLow() - buyPrice) / buyPrice < -0.01) {
		// System.out.println("持仓2天亏损一个点");
		// setTradeRecord(code, hlList, buyPrice, buyTm, buyfeature, record, 1);
		// flag = true;
		// }
		// }
		// // 风险控制 持仓第3天还没有盈利
		// if (dayStep(record.getEndTime(), buyTm) == 3) {
		//
		// if ((record.getLow() - buyPrice) / buyPrice < 0.00) {
		// System.out.println("持仓三天处于亏损");
		// setTradeRecord(code, hlList, buyPrice, buyTm, buyfeature, record, 1);
		// flag = true;
		// }
		// }
		// // 风险控制 持仓第3天还没有盈利
		// if (dayStep(record.getEndTime(), buyTm) == 4) {
		//
		// if ((record.getLow() - buyPrice) / buyPrice < 0.01) {
		// System.out.println("持仓4天获利少于1个点");
		// setTradeRecord(code, hlList, buyPrice, buyTm, buyfeature, record, 1);
		// flag = true;
		// }
		// }
		// // 风险控制 持仓第3天还没有盈利
		// if (dayStep(record.getEndTime(), buyTm) >= 5) {
		//
		// if ((record.getLow() - buyPrice) / buyPrice < 0.02) {
		// System.out.println("持仓大于等于5天获利少于2个点");
		// setTradeRecord(code, hlList, buyPrice, buyTm, buyfeature, record, 1);
		// flag = true;
		// }
		// }

		if (pointNum >= orgPointNum + 1) {

			if (lines.get(lines.size() - 1).getPrice() < lines.get(lines.size() - 1).getPrice()) {
				// 最后一段是向下段

				if (lines.get(lines.size() - 1).getPrice() < lines.get(orgPointNum - 1).getPrice()) {

					setTradeRecord(code, hlList, buyPrice, buyTm, buyfeature, record, 1);
					return true;

				}
			}
		}

		if (pointNum >= orgPointNum + 3) {

			double lastPointv1 = lines.get(lines.size() - 1).getPrice();

			double lastPointv2 = lines.get(lines.size() - 2).getPrice();

			double maxv = max(lastPointv1, lastPointv2);

			if (maxv < buyfeature.getAfterZhongshu().getDd()) {

				setTradeRecord(code, hlList, buyPrice, buyTm, buyfeature, record, 1);
				return true;

			}

		}

		// if(record.getLow()<buyfeature.getPressPrice()&&(buyPrice -
		// record.getLow())/buyPrice>0.05) {
		//
		// setTradeRecord(code, hlList, buyPrice, buyTm, buyfeature, record, 1);
		// flag = true;
		// }
		return flag;
	}

	private static void setTradeRecord(String code, List<HLDto> hlList, double buyPrice, String buyTm,
			BuyFeatrue buyfeature, HistoryRecord record, int type) {
		HLDto hlDto;
		hlDto = new HLDto();
		hlDto.setCode(code);
		hlDto.setBuy(buyPrice);
		hlDto.setSale(record.getLow());
		hlDto.setDayNum(dayStep(record.getEndTime(), buyTm));
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
		hlDto.setZhishui(type);

		hlDto.setPreMacd(buyfeature.getPreMacd());
		hlDto.setAfterMacd(buyfeature.getAfterMacd());
		;

		System.out.println("持仓" + hlDto.getDayNum() + "天  收益" + hlDto.getYinliRate());
		hlList.add(hlDto);
	}

	// buyPrice = buy(list, simpleKLines, biLines, lines, trendList);
	private static BuyFeatrue buy(List<HistoryRecord> list, List<HistoryRecord> list2, List<Point> lines,
			List<TrendType> trendList) throws IllegalAccessException, InvocationTargetException {

		BuyFeatrue buyf = new BuyFeatrue();

		// 中枢或走势连接必须大于4个
		if (trendList != null && trendList.size() >= 12) {

			// 最后一个中枢或者连接
			TrendType lastTrend = trendList.get(trendList.size() - 1);

			// 判断最后一个是中枢 还是 连接
			if (lastTrend instanceof ZhongShu) {

				Line lastTrend1 = (Line) trendList.get(trendList.size() - 10);
				Line lastTrend2 = (Line) trendList.get(trendList.size() - 8);
				Line lastTrend3 = (Line) trendList.get(trendList.size() - 6);
				Line lastTrend4 = (Line) trendList.get(trendList.size() - 4);
				Line lastTrend5 = (Line) trendList.get(trendList.size() - 2);

				ZhongShu lastZh1 = (ZhongShu) trendList.get(trendList.size() - 11);
				ZhongShu lastZh2 = (ZhongShu) trendList.get(trendList.size() - 9);
				ZhongShu lastZh3 = (ZhongShu) trendList.get(trendList.size() - 5);
				ZhongShu lastZh4 = (ZhongShu) trendList.get(trendList.size() - 4);
				ZhongShu lastZh5 = (ZhongShu) trendList.get(trendList.size() - 3);
				ZhongShu lastZh6 = (ZhongShu) trendList.get(trendList.size() - 1);

				if (lastTrend1.getDirect() == 0 && lastTrend2.getDirect() == 0 && lastTrend3.getDirect() == 0
						&& lastTrend4.getDirect() == 0 && lastTrend5.getDirect() == 0) {

					if (lastZh1.getNum() >= 9 && lastZh4.getNum() >= 9) {

						if (lastZh2.getNum() < 9 && lastZh3.getNum() < 9) {

							if (lastZh5.getNum() < 9 && lastZh6.getNum() < 9) {
								
								System.out.println("ADDAAAAAAAAAAAAAAAAAAA");
							}

						}

					}

				}

			}
		}
		buyf.setPrice(0);
		return buyf;
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
