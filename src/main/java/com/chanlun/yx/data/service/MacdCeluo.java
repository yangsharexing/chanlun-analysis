package com.chanlun.yx.data.service;

import java.math.BigDecimal;
import java.util.List;

import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.util.TechnicalIndexUtils;
import com.chanlun.yx.redis.RedisUtils;

public class MacdCeluo {

	public static double totalYinli = 0;
	public static double time = 0;

	public static void main(String[] args) {
		
		List<String> codes = RedisUtils.getAllKeys();
		for(String code:codes) {
			test(code);
		}
	}
	
	public static void test(String code) {
		// 下载数据
		// 计算macd
		List<HistoryRecord> list = RedisUtils.fetchData(code);
		TechnicalIndexUtils.computeMacd(list);
		int start = -1;
		int count = 0;
		int oi=0;
		for (;oi < list.size(); oi++) {
			HistoryRecord record = list.get(oi);
			
			if(record.getTime().equals("2019-03-15") && record.getCode().equals("sz.000550")) {
				
				System.out.println(record.getBar());
			}

			if (Math.abs(record.getBar()) <= 0.2) {

				count = count + 1;

			} else {

				if (count < 20) {

					count = 0;
				} else {

//					System.out.println(record);
					boolean xishu = false;
					start = oi;
					int countj = 0;
					for (int j = start; j < list.size() - 1; j++) {
						HistoryRecord recordj1 = list.get(j);
						HistoryRecord recordj2 = list.get(j + 1);

						if (recordj2.getBar() < recordj1.getBar()) {

							countj++;
						} else {

//							if ( recordj2.getBar() < -0.5) {//0.2

								// 确认出现了买点在j+1收盘
								int restart = buy(list, j + 1);
								if (restart == -1) {
									xishu = true;
									break;
								} else {
									start = -1;
									count = 0;
									oi = restart;
									break;
								}
//							}

						}

					}
				}

			}
		}

	}

	private static int buy(List<HistoryRecord> list, int index) {

		double buyprice = list.get(index).getClose() * 1.005;
		String buyTime = list.get(index).getTime();
		double salePrice = 0;
		String saleTime = "";
		int count = 0;
		int tail = -1;
		for (int i = index; i < list.size() - 3; i++) {
			HistoryRecord record1 = list.get(i);
			HistoryRecord record2 = list.get(i + 1);
			HistoryRecord record3 = list.get(i + 2);
//			System.out.println(record1);
//			System.out.println(record2);
//			System.out.println(record3);

			if (record3.getBar() < record2.getBar() && record2.getBar() < record1.getBar()) {
				
				if(record1.getBar()>0.2 &&record3.getBar()<-0.2) {
					tail = i;
					salePrice = record3.getClose() * 0.995;
					saleTime = record3.getTime();
					break;
				}
			
			}
		}
		if (salePrice == 0) {
			// 没有出现卖点
			return -1;
		} else {
//			System.out.println("股票" + list.get(0).getCode() +"  "+buyTime +" 买入" + format(buyprice)+ "   "+saleTime+"  卖出" + format(salePrice)+"获利 "+format((salePrice-buyprice)/buyprice));
			
//			System.out.println(format(100*((salePrice-buyprice)/buyprice))+"%");
			
			System.out.println(format((salePrice-buyprice)/buyprice));

//			System.out.println("buy:" + buyTime + "  sale " + saleTime);
//			totalYinli = totalYinli + (salePrice - buyprice) / salePrice;
//			time = time + 1;
//			System.out.println("buy:" + buyTime + "  sale " + saleTime);
//			System.out.println("总次数" + time + " 总获利:  " + totalYinli + "   平局获利:" + totalYinli / time);
			return tail + 3;
		}
	}
	
	public static String format(double value) {
		
		return new BigDecimal(value).setScale(3, BigDecimal.ROUND_HALF_UP).toString();
	}

}
