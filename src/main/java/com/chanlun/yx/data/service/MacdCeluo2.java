package com.chanlun.yx.data.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.util.TechnicalIndexUtils;
import com.chanlun.yx.redis.RedisUtils;

public class MacdCeluo2 {

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

		System.out.println("总盈利" + totalYinli);
		System.out.println("总次数" + time);
		System.out.println("平均" + totalYinli / time);
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
		double maxMacd = maxMacd(list, 0, list.size() - 1);
		int oi = 100;
		for (; oi < list.size(); oi++) {
			HistoryRecord record1 = list.get(oi - 3);
			HistoryRecord record2 = list.get(oi - 2);
			HistoryRecord record3 = list.get(oi - 1);
			
			
			
			
			if (record1.getBar() < record2.getBar() && record2.getBar() < record3.getBar()) {
				if (record3.getBar() < 0 && Math.abs(record1.getBar()) > 0.3 * maxMacd) {
//
					if (record1.getClose() > record2.getClose()) {
						
						
						boolean flag = false;
						for(int k=oi-30;k<oi-10;k++) {
							
							HistoryRecord recordk = list.get(k);
							if(Math.abs(recordk.getBar())>0.3*maxMacd) {
								
								flag = true;
								continue;
							}
						}
						
						if(flag) {
							continue;
						}

						buy(list, oi - 3);
					}
				}
			}
		}
	}

	private static void buy(List<HistoryRecord> list, int index) {
//		double buyprice = list.get(index).getClose() * 1.005;
		double buyprice = list.get(index).getClose();
		String buyTime = list.get(index).getTime();
		double salePrice = 0;
		String saleTime = "";
		
		
		if (max(list, index - 30, index) > 1.1 * min(list, index - 50, index - 30)) {

			return;
		}
		
		if (min(list, index - 30, index)*1.02<buyprice) {

			return;
		}
		
		if(yiziban(list, index-10, index)) {
			return;
		}
		
		if(buyprice>90) {
			return ;
		}
		//当天是阴线不买
		if(list.get(index).getOpen()>list.get(index).getClose()) {
			return ;
		}
		
		for (int i = index; i < list.size() - 3; i++) {

			HistoryRecord record1 = list.get(i);
			HistoryRecord record2 = list.get(i + 1);
			HistoryRecord record3 = list.get(i + 2);
			HistoryRecord record4 = list.get(i + 3);

			if (record3.getBar() < record2.getBar() && record2.getBar() < record1.getBar()) {
				
				
//				if(record4.getBar()<record3.getBar()) {
					
//					salePrice = record3.getClose() * 0.995;
					salePrice = record3.getClose();
					saleTime = record3.getTime();
					break;
//				}

			}
			
			if(record3.getHigh()<record2.getHigh() && record3.getClose()<record3.getOpen()) {
				
				if(record4.getClose()<record4.getOpen()) {
					salePrice = record2.getClose();
					saleTime = record2.getTime();
					break;
				}
				
			}

		}

		if (salePrice == 0) {
			// 没有出现卖点
			System.out
					.println("股票" + list.get(0).getCode() + "持仓" + dayStep(list.get(list.size() - 1).getTime(), buyTime)
							+ "  " + buyTime + " 买入" + format(buyprice) + "死了");

		} else {
			System.out.println("股票" + list.get(0).getCode() + "持仓" + dayStep(saleTime, buyTime) + "  " + buyTime + " 买入"
					+ format(buyprice) + "   " + saleTime + "  卖出" + format(salePrice) + "获利 "
					+ format((salePrice - buyprice) / buyprice) + "-------------");

			totalYinli = totalYinli + (salePrice - buyprice) / salePrice;
			time = time + 1;
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
			if (record.getHigh() == record.getLow()) {
				return true;
			}
		}
		return false;
	}

	public static double maxMacd(List<HistoryRecord> lists, int start, int end) {
		List<HistoryRecord> list = lists.subList(start, end);

		double maxMace = Math.abs(list.get(0).getBar());
		for (HistoryRecord record : list) {
			if (Math.abs(record.getBar()) > maxMace) {
				maxMace = Math.abs(record.getBar());
			}
		}
		return maxMace;
	}

}
