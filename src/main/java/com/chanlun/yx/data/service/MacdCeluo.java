package com.chanlun.yx.data.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.util.TechnicalIndexUtils;
import com.chanlun.yx.redis.RedisUtils;

public class MacdCeluo {

	public static double totalYinli = 0;
	public static double time = 0;
	public static double success = 0;
	// 格式
	public static List<String> tradeList = new ArrayList<String>();// 2020-01-01
	// 格式

	static {
		List<HistoryRecord> list = RedisUtils.fetchData("day.java.sh.600000");
		for (HistoryRecord record : list) {
			tradeList.add(record.getTime());
		}
	

	}

	public static void main(String[] args) {

		List<String> codes = RedisUtils.getAllKeys();
		for (String code : codes) {
			test(code);
		}
		
		
		System.out.println("总盈利"+totalYinli);
		System.out.println("总次数"+time);
		System.out.println("平均"+totalYinli/time);
	}

	public static void test(String code) {
		// 下载数据
		// 计算macd
		List<HistoryRecord> list = RedisUtils.fetchData(code);

		TechnicalIndexUtils.computeMacd(list);

		if (list.size() < 400) {
			return;
		}
		list = list.subList(list.size() - 350, list.size() - 1);
		double maxMacd = maxMacd(list, 0, list.size()-1);

		int start = -1;
		int count = 0;
		int oi = 100;
		for (; oi < list.size(); oi++) {
			HistoryRecord record = list.get(oi);

			if (Math.abs(record.getBar()) <= 0.2*maxMacd) {

				count = count + 1;

			} else {

				if (count < 40) {

					count = 0;
				} else {
//						
					List<HistoryRecord> macdList = new ArrayList<HistoryRecord>();
					for (int k = oi - 40; k <= oi; k++) {
						macdList.add(list.get(k));
					}

					boolean xishu = false;
					start = oi;
					int countj = 0;
					for (int j = start; j < list.size() - 1; j++) {
						HistoryRecord recordj1 = list.get(j);
						HistoryRecord recordj2 = list.get(j + 1);

						if (recordj2.getBar() < recordj1.getBar()) {

							countj++;
						} else {

							if (recordj2.getBar() < -0.2*maxMacd  || recordj2.getBar() >0.2*maxMacd) {// 0.2

								// 确认出现了买点在j+1收盘
								int restart = buy(list, j + 1, macdList,recordj2);
								if (restart == -1) {
									xishu = true;
									start = -1;
									count = 0;
									break;
								} else {
									start = -1;
									count = 0;
									oi = restart;
									break;
								}
							}

						}

					}
				}

			}
		}

	}

	private static int buy(List<HistoryRecord> list, int index, List<HistoryRecord> macdList,HistoryRecord recordj2) {

		double buyprice = list.get(index).getClose() * 1.005;
		String buyTime = list.get(index).getTime();
		double salePrice = 0;
		String saleTime = "";
		int count = 0;
		int tail = -1;

		if (max(list, index - 30, index) > 1.1 * min(list, index - 50, index - 30)) {

			return -1;
		}
		
		if (min(list, index - 30, index)*1.02<buyprice) {

			return -1;
		}
		
		if(yiziban(list, index-10, index)) {
			return -1;
		}
		
		if(buyprice>90) {
			return -1;
		}
		//处在跌势不买

		for (int i = index; i < list.size() - 3; i++) {
			HistoryRecord record1 = list.get(i);
			HistoryRecord record2 = list.get(i + 1);
			HistoryRecord record3 = list.get(i + 2);
//			System.out.println(record1);
//			System.out.println(record2);
//			System.out.println(record3);

			if (record3.getBar() < record2.getBar() && record2.getBar() < record1.getBar()) {

//				if (record1.getBar() > 0.1 && record3.getBar() < -0.1) {
				tail = i;
				salePrice = record3.getClose() * 0.995;
				saleTime = record3.getTime();
				break;
//				}

			}
		}
		if (salePrice == 0) {
			// 没有出现卖点
			System.out
					.println("股票" + list.get(0).getCode() + "持仓" + dayStep(list.get(list.size() - 1).getTime(), buyTime)
							+ "  " + buyTime + " 买入" + format(buyprice) + "死了");
			if (list.get(0).getCode().equals("sh.600673") && buyTime.equals("2019-05-08")) {

				for (HistoryRecord re : macdList) {
					System.out.println(re.getTime() + "  " + format(re.getBar()));
				}
				System.out.println("max "+max(list, index - 30, index));
				System.out.println("min "+min(list, index - 50, index - 30));
			}
			
			
			return -1;

		} else {
			System.out.println("股票" + list.get(0).getCode() + "持仓" + dayStep(saleTime, buyTime) + "  " + buyTime + " 买入"
					+ format(buyprice) + "   " + saleTime + "  卖出" + format(salePrice) + "获利 "
					+ format((salePrice - buyprice) / buyprice) + "-------------");

//			System.out.println(format(100*((salePrice-buyprice)/buyprice))+"%");

//			System.out.println("持仓" + dayStep(saleTime, buyTime) + "   盈利" + format((salePrice - buyprice) / buyprice));

//			System.out.println("buy:" + buyTime + "  sale " + saleTime);
			totalYinli = totalYinli + (salePrice - buyprice) / salePrice;
			time = time + 1;
//			System.out.println("buy:" + buyTime + "  sale " + saleTime);
//			System.out.println("总次数" + time + " 总获利:  " + totalYinli + "   平局获利:" + totalYinli / time);
			
			System.out.println(recordj2.getTime()+"  "+recordj2.getBar());
			if (list.get(0).getCode().equals("sh.600673") && buyTime.equals("2019-05-08")) {

				for (HistoryRecord re : macdList) {
					System.out.println(re.getTime() + "  " + format(re.getBar()));
				}
				System.out.println("max "+max(list, index - 30, index));
				System.out.println("min "+min(list, index - 50, index - 30));
			}
			return tail + 3;
		}
	}

	public static String format(double value) {

		return new BigDecimal(value).setScale(3, BigDecimal.ROUND_HALF_UP).toString();
	}

	public static double min(List<HistoryRecord> lists, int start, int end) {
		List<HistoryRecord> list = lists.subList(start, end);
		double min = list.get(0).getLow();
		for (HistoryRecord record : list) {
			if (record.getLow() < min) {
				min = record.getLow();
			}
		}
		return min;
	}

	public static double max(List<HistoryRecord> lists, int start, int end) {
		List<HistoryRecord> list = lists.subList(start, end);
		double max = list.get(0).getHigh();
		for (HistoryRecord record : list) {
			if (record.getHigh() > max) {
				max = record.getHigh();
			}
		}
		return max;
	}

	public static int dayStep(String endTime, String startTime) {

		int start = 0;
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
	}
	
	public static boolean yiziban(List<HistoryRecord> lists, int start, int end) {
		List<HistoryRecord> list = lists.subList(start, end);
		for (HistoryRecord record : list) {
			if (record.getHigh()==record.getLow()) {
				return true;
			}
		}
		return false;
	}
	
	public static double maxMacd(List<HistoryRecord> lists, int start, int end) {
		List<HistoryRecord> list = lists.subList(start, end);
		
		double maxMace = Math.abs(list.get(0).getBar());
		for (HistoryRecord record : list) {
			if ( Math.abs(record.getBar())>maxMace) {
				 maxMace =Math.abs(record.getBar());
			}
		}
		return maxMace;
	}

}
