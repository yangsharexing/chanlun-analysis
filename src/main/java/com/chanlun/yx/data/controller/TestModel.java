package com.chanlun.yx.data.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chanlun.yx.data.dto.HLDto;
import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.dto.MonitorQueryDto;
import com.chanlun.yx.data.dto.Point;
import com.chanlun.yx.data.util.BiLineUtils;
import com.chanlun.yx.data.util.ExportAbstractUtil;
import com.chanlun.yx.data.util.KLineUtils;
import com.chanlun.yx.data.util.LineUtils;
import com.chanlun.yx.redis.RedisUtils;
import com.chanlun.yx.thread.TestJob;
import com.chanlun.yx.thread.TestJob2;
import com.chanlun.yx.thread.TestJob4;

public class TestModel {

	public static void main(String[] agrs) {
		Test6();
	}

	public static void Test() {

		List<String> codeList = RedisUtils.getAllKeys();
		int yin = 0;
		int kui = 0;
		double zongyinli = 0;

		for (String code : codeList) {
			List<HistoryRecord> list = RedisUtils.fetchData(code);
			// System.out.println("原始k线" + list.size());

			for (int i = 1; i < list.size() - 10; i++) {
				HistoryRecord before = list.get(i - 1);
				HistoryRecord first = list.get(i);
				HistoryRecord second = list.get(i + 1);
				HistoryRecord third = list.get(i + 2);

				if (first.getOpen() > first.getClose()) {

					if (second.getClose() > second.getOpen()) {

						if (first.getHigh() > second.getHigh()) {

							if (first.getLow() > second.getLow()) {

								if (before.getLow() > second.getLow()) {

//									if ((second.getOpen() - second.getLow())/second.getOpen()<0.01 && (second.getOpen() - second.getLow())/second.getOpen()>0.001) {
									if ((second.getHigh() - second.getClose()) / second.getOpen() < 0.01
											&& (second.getOpen() - second.getLow()) / second.getOpen() < 0.01) {

										if (second.getVolume() > first.getVolume() * 1.5) {

											if (third.getOpen() < second.getClose()) {

//												// 买入
												String buyTm = third.getTime();
												double buyprice = third.getOpen();
												boolean flag = false;
												int index = 0;
												for (int k = i + 2; k < list.size(); k++) {
													index++;
													HistoryRecord ifirst = list.get(k);
													HistoryRecord isecond = list.get(k + 1);
													if (list.size() - 1 == (k + 1)) {
														flag = true;
														break;
													}

//													if ((isecond.getClose() - ifirst.getClose())
//															/ ifirst.getClose() > -0.02
//															&& 2 * isecond.getVolume() < ifirst.getVolume()) {

													if (((isecond.getClose() - ifirst.getClose())
															/ ifirst.getClose() > -0.02
															&& 2 * isecond.getVolume() < ifirst.getVolume())
															|| (isecond.getClose() - ifirst.getClose())
																	/ ifirst.getClose() < -0.05
															|| (index > 5 && (isecond.getClose() - buyprice)
																	/ buyprice < 0.03)) {

														double saleprice = isecond.getClose();

//														System.out.println( (saleprice - buyprice) / saleprice);

														System.out.println(
																code + "    " + buyTm + "    " + isecond.getTime()
																		+ "    " + (saleprice - buyprice) / saleprice);

														zongyinli = zongyinli + (saleprice - buyprice) / saleprice;
														if (saleprice - buyprice > 0) {

															yin++;

														} else {

															kui++;
														}
														flag = true;
														break;
													}
												}

												if (flag) {
													break;
												}
											}
										}

									}
								}
							}
						}
					}
				}
			}

		}
		System.out.println("总数" + (yin + kui));
		System.out.println("总盈利" + zongyinli);
		System.out.println("平均每次收益" + zongyinli / (yin + kui));
		System.out.println("获胜率" + (double) yin / (yin + kui));

	}

	public static void Test2() {

		List<String> codeList = RedisUtils.getAllKeys();
		int yin = 0;
		int kui = 0;
		double zongyinli = 0;
		double tkui = 0;
		double tyin = 0;

		for (String code : codeList) {
			List<HistoryRecord> list = RedisUtils.fetchData(code);

			// System.out.println("原始k线" + list.size());

			for (int i = 1; i < list.size() - 10; i++) {
				HistoryRecord before = list.get(i - 1);
				HistoryRecord first = list.get(i);
				HistoryRecord second = list.get(i + 1);
				HistoryRecord third = list.get(i + 3);

				if (before.getOpen() > before.getClose() && first.getOpen() > first.getClose()
						&& second.getOpen() < first.getClose()
						&& (first.getClose() - second.getOpen()) / first.getClose() > 0.02
						&& (first.getClose() - second.getOpen()) / first.getClose() < 0.03) {
					if (2 * before.getAbs_length() < first.getAbs_length()) {
//						if ( before.getAbs_length() / before.getVolume() < first.getAbs_length()
//								/ first.getVolume()) { // 放量下跌

						if (second.getOpen() < second.getClose()
								&& (second.getClose() - second.getOpen()) / second.getOpen() > 0.02) {

							if (Math.abs(before.getHigh() - second.getLow()) / second.getLow() < 0.15) {

								double buyPrice = second.getClose();
								double salePrice = third.getClose();

								zongyinli = zongyinli + (salePrice - buyPrice) / buyPrice;

								System.out.println(second.getCode() + "  " + second.getTime() + "  "
										+ (salePrice - buyPrice) / buyPrice);
								if (third.getClose() > second.getOpen()) {
									tyin = tyin + (salePrice - buyPrice) / buyPrice;
									yin++;
								} else {
									tkui = tkui + (salePrice - buyPrice) / buyPrice;
									kui++;
								}
							}
						}
//						}
					}
				}
			}

		}
		System.out.println("总数" + (yin + kui));
		System.out.println("总盈利" + zongyinli);
		System.out.println("平均每次收益" + zongyinli / (yin + kui));
		System.out.println("获胜率" + (double) yin / (yin + kui));

		System.out.println("盈利每次为" + tyin / yin);
		System.out.println("亏损每次为" + tkui / kui);

	}

	public static void Test3() {

		List<String> codeList = RedisUtils.getAllKeys();
		int yin = 0;
		int kui = 0;
		double zongyinli = 0;
		double tkui = 0;
		double tyin = 0;

		double totalPrice3 = 0;
		double totalnum3 = 0;
		double totalPrice4 = 0;
		double totalnum4 = 0;
		double totalPrice5 = 0;
		double totalnum5 = 0;
		double totalPrice6 = 0;
		double totalnum6 = 0;
		double totalPrice7 = 0;
		double totalnum7 = 0;
		double totalPrice8 = 0;
		double totalnum8 = 0;
		double totalPrice9 = 0;
		double totalnum9 = 0;

		for (String code : codeList) {
			List<HistoryRecord> list = RedisUtils.fetchData(code);
			if (list.size() == 0 || list.get(0).getTurn() == 0.0) {

				continue;
			}

			// System.out.println("原始k线" + list.size());

			for (int i = 0; i < list.size(); i++) {
				HistoryRecord record = list.get(i);

				if (record.getAbs_length() / record.getOpen() >= 0.03
						&& record.getAbs_length() / record.getOpen() < 0.04) {

					totalPrice3 = totalPrice3 + record.getAbs_length() / record.getOpen();
					totalnum3 = totalnum3 + record.getTurn();

				} else if (record.getAbs_length() / record.getOpen() >= 0.04
						&& record.getAbs_length() / record.getOpen() < 0.05) {
					totalPrice4 = totalPrice4 + record.getAbs_length() / record.getOpen();
					totalnum4 = totalnum4 + record.getTurn();
				} else if (record.getAbs_length() / record.getOpen() >= 0.05
						&& record.getAbs_length() / record.getOpen() < 0.06) {
					totalPrice5 = totalPrice5 + record.getAbs_length() / record.getOpen();
					totalnum5 = totalnum5 + record.getTurn();
				} else if (record.getAbs_length() / record.getOpen() >= 0.06
						&& record.getAbs_length() / record.getOpen() < 0.07) {
					totalPrice6 = totalPrice6 + record.getAbs_length() / record.getOpen();
					totalnum6 = totalnum6 + record.getTurn();
				} else if (record.getAbs_length() / record.getOpen() >= 0.07
						&& record.getAbs_length() / record.getOpen() < 0.08) {
					totalPrice7 = totalPrice7 + record.getAbs_length() / record.getOpen();
					totalnum7 = totalnum7 + record.getTurn();
				} else if (record.getAbs_length() / record.getOpen() >= 0.08
						&& record.getAbs_length() / record.getOpen() < 0.09) {
					totalPrice8 = totalPrice8 + record.getAbs_length() / record.getOpen();
					totalnum8 = totalnum8 + record.getTurn();
				} else if (record.getAbs_length() / record.getOpen() >= 0.09
						&& record.getAbs_length() / record.getOpen() < 0.11) {
					totalPrice9 = totalPrice9 + record.getAbs_length() / record.getOpen();
					totalnum9 = totalnum9 + record.getTurn();
				}

			}

		}
		double a = totalPrice3 + totalPrice4 + totalPrice5 + totalPrice6 + totalPrice7 + totalPrice8 + totalPrice9;
		double b = totalnum3 + totalnum4 + totalnum5 + totalnum6 + totalnum7 + totalnum8 + totalnum9;
		double bz = a / b;
		System.out.println("-----" + bz);

		int y = 0;
		int k = 0;
		int sta = 0;
		double yt = 0.0;
		double kt = 0.0;
		for (String code : codeList) {

			List<HistoryRecord> list = RedisUtils.fetchData(code);
			if (list.size() == 0 || list.get(0).getTurn() == 0.0) {

				continue;
			}

			setAverageLine(list, 5);
			setAverageLine(list, 10);
			setAverageLine(list, 20);

			for (int i = 2; i < list.size() - 5; i++) {
				HistoryRecord record_1 = list.get(i - 1);
				HistoryRecord record1 = list.get(i);
				HistoryRecord record2 = list.get(i + 1);

				// 第一天阴线
				if (record1.getVolume() > record_1.getVolume()) {

					if (record1.getPriceCh() > -0.09 && record1.getPriceCh() < -0.06) {

						if (Math.abs(record1.getPriceCh() / record1.getTurn()) > 5 * bz) {

							double buy = record1.getClose();
							double sale = record2.getClose();

							if (sale > buy) {
								y++;
								yt = yt + (sale - buy) / buy;
							} else {
								k++;
								kt = kt + (sale - buy) / buy;
							}
							System.out.println(
									record1.getCode() + "  " + record1.getTime() + "  " + ((sale - buy) / buy));
						}
					}
				}

			}
		}
		System.out.println("==== " + sta);
		System.out.println("" + (k + y) + "   " + (y * 1.0) / (k + y));

		System.out.println("zyk " + (yt + kt));
		System.out.println("pyk " + (yt + kt) / (y + k));
		System.out.println("py " + yt / y);
		System.out.println("pk " + kt / k);

		System.out.println("-----" + bz);

//增量上涨后 缩量回调 第三日上涨概率大
	}

	public static void Test4() {

		List<String> codeList = RedisUtils.getAllKeys();
		int yin = 0;
		int kui = 0;
		double zongyinli = 0;
		double tkui = 0;
		double tyin = 0;

		double totalPrice3 = 0;
		double totalnum3 = 0;
		double totalPrice4 = 0;
		double totalnum4 = 0;
		double totalPrice5 = 0;
		double totalnum5 = 0;
		double totalPrice6 = 0;
		double totalnum6 = 0;
		double totalPrice7 = 0;
		double totalnum7 = 0;
		double totalPrice8 = 0;
		double totalnum8 = 0;
		double totalPrice9 = 0;
		double totalnum9 = 0;

		for (String code : codeList) {
			List<HistoryRecord> list = RedisUtils.fetchData(code);
			if (list.size() == 0 || list.get(0).getTurn() == 0.0) {

				continue;
			}

			// System.out.println("原始k线" + list.size());

			for (int i = 0; i < list.size(); i++) {
				HistoryRecord record = list.get(i);

				if (Math.abs(record.getPriceCh()) >= 0.03 && Math.abs(record.getPriceCh()) < 0.04) {

					totalPrice3 = totalPrice3 + Math.abs(record.getPriceCh());
					totalnum3 = totalnum3 + record.getTurn();

				} else if (Math.abs(record.getPriceCh()) >= 0.04 && Math.abs(record.getPriceCh()) < 0.05) {
					totalPrice4 = totalPrice4 + Math.abs(record.getPriceCh());
					totalnum4 = totalnum4 + record.getTurn();
				} else if (Math.abs(record.getPriceCh()) >= 0.05 && Math.abs(record.getPriceCh()) < 0.06) {
					totalPrice5 = totalPrice5 + Math.abs(record.getPriceCh());
					totalnum5 = totalnum5 + record.getTurn();
				} else if (Math.abs(record.getPriceCh()) >= 0.06 && Math.abs(record.getPriceCh()) < 0.07) {
					totalPrice6 = totalPrice6 + Math.abs(record.getPriceCh());
					totalnum6 = totalnum6 + record.getTurn();
				} else if (Math.abs(record.getPriceCh()) >= 0.07 && Math.abs(record.getPriceCh()) < 0.08) {
					totalPrice7 = totalPrice7 + Math.abs(record.getPriceCh());
					totalnum7 = totalnum7 + record.getTurn();
				} else if (Math.abs(record.getPriceCh()) >= 0.08 && Math.abs(record.getPriceCh()) < 0.09) {
					totalPrice8 = totalPrice8 + Math.abs(record.getPriceCh());
					totalnum8 = totalnum8 + record.getTurn();
				} else if (Math.abs(record.getPriceCh()) >= 0.09 && Math.abs(record.getPriceCh()) < 0.11) {
					totalPrice9 = totalPrice9 + Math.abs(record.getPriceCh());
					totalnum9 = totalnum9 + record.getTurn();
				}

			}

		}
		double a = totalPrice3 + totalPrice4 + totalPrice5 + totalPrice6 + totalPrice7 + totalPrice8 + totalPrice9;
		double b = totalnum3 + totalnum4 + totalnum5 + totalnum6 + totalnum7 + totalnum8 + totalnum9;
		double bz = a / b;
		System.out.println("-----" + bz);

		int y = 0;
		int k = 0;
		int sta = 0;
		double yt = 0.0;
		double kt = 0.0;
		for (String code : codeList) {

			List<HistoryRecord> listo = RedisUtils.fetchData(code);
			if (listo.size() == 0 || listo.get(0).getTurn() == 0.0) {

				continue;
			}

			List<HistoryRecord> list = new ArrayList<HistoryRecord>();
			for (HistoryRecord r : listo) {
				if (r.getTime().contains("2020")) {
					list.add(r);
				}
			}
//			2014  61.5    2.2
//			2015  53.5    -4.66
//			2016  70    25	
//			2017  73    6.3	
//			2018  66    22	
//			2019  82    24	
//			2019  86    15	
			setAverageLine(list, 5);
			setAverageLine(list, 10);
			setAverageLine(list, 20);

			for (int i = 2; i < list.size() - 5; i++) {
				HistoryRecord record_1 = list.get(i - 1);
				HistoryRecord record1 = list.get(i);
				HistoryRecord record2 = list.get(i + 1);

				// 第一天阴线
//				if (record1.getVolume() > record_1.getVolume()) {

				if (record1.getPriceCh() > -0.09 && record1.getPriceCh() < -0.06) {

					if (Math.abs(record1.getPriceCh() / record1.getTurn()) > 4 * bz
							&& Math.abs(record1.getPriceCh() / record1.getTurn()) < 10 * bz) {

						double buy = record1.getClose();
						double sale = record2.getClose();

						if (sale > buy) {
							y++;
							yt = yt + (sale - buy) / buy;
						} else {
							k++;
							kt = kt + (sale - buy) / buy;
						}
						System.out.println(record1.getCode() + "  " + record1.getTime() + "  " + ((sale - buy) / buy));
					}

//					}
				}
			}

		}
		System.out.println("==== " + sta);
		System.out.println("" + (k + y) + "   " + (y * 1.0) / (k + y));

		System.out.println("zyk " + (yt + kt));
		System.out.println("pyk " + (yt + kt) / (y + k));
		System.out.println("py " + yt / y);
		System.out.println("pk " + kt / k);

		System.out.println("-----" + bz);

		// 增量上涨后 缩量回调 第三日上涨概率大
	}

	public static void Test5() {

		List<String> codeList = RedisUtils.getAllKeys();
		int y = 0;
		int k = 0;
		int sta = 0;
		double yt = 0.0;
		double kt = 0.0;
		double bz = 0.01;

		for (String code : codeList) {

			List<HistoryRecord> listo = RedisUtils.fetchData(code);
			if (listo.size() == 0 || listo.get(0).getTurn() == 0.0) {

				continue;
			}

			List<HistoryRecord> list = new ArrayList<HistoryRecord>();
			for (HistoryRecord r : listo) {
				if (r.getTime().contains("2020")) {
					list.add(r);
				}

			}

//			2014    54    -2.2
//			2015    76     68
//			2015    42     -2     228			
//			2017    58     0.12    60		
//			2018    58     0.6    613
//			2019    66     1.2    127
//			2020    64     0.13   65
			setAverageLine(list, 5);
			setAverageLine(list, 10);
			setAverageLine(list, 20);

			for (int i = 2; i < list.size() - 5; i++) {
				HistoryRecord record_1 = list.get(i - 1);
				HistoryRecord record1 = list.get(i);
				HistoryRecord record2 = list.get(i + 1);

				// 第一天阴线
				if (record1.getVolume() < record_1.getVolume()) {

					if ((record1.getPriceCh() > 0.03 && record1.getPriceCh() < 0.045)
							|| (record1.getPriceCh() > 0.055 && record1.getPriceCh() < 0.07)) {

						if (Math.abs(record1.getPriceCh() / record1.getTurn()) > 10 * bz) {

							double buy = record1.getClose();
							double sale = record2.getClose();

							if (sale > buy) {
								y++;
								yt = yt + (sale - buy) / buy;
							} else {
								k++;
								kt = kt + (sale - buy) / buy;
							}
							System.out.println(record1.getCode() + "  " + record1.getTime() + "  "
									+ ((sale - buy) / buy) + "   " + record1.getPriceCh() + "    "
									+ Math.abs(record1.getPriceCh() / record1.getTurn()));
						}

					}
				}
			}

		}
		System.out.println("==== " + sta);
		System.out.println("" + (k + y) + "   " + (y * 1.0) / (k + y));

		System.out.println("zyk " + (yt + kt));
		System.out.println("pyk " + (yt + kt) / (y + k));
		System.out.println("py " + yt / y);
		System.out.println("pk " + kt / k);

		System.out.println("-----" + bz);

		// 增量上涨后 缩量回调 第三日上涨概率大
	}

	public static void Test6() {

		List<String> codeList = RedisUtils.getAllKeys();
		int y = 0;
		int k = 0;
		int sta = 0;
		double yt = 0.0;
		double kt = 0.0;
		double bz = 0.01;
		int tip = 0;
		for (String code : codeList) {
			List<HistoryRecord> listo = RedisUtils.fetchData(code);
			if (listo.size() == 0 || listo.get(0).getTurn() == 0.0) {

				continue;
			}

			List<HistoryRecord> list = new ArrayList<HistoryRecord>();
			for (HistoryRecord r : listo) {
//				if(r.getTime().contains("2020")) {
//					list.add(r);
//				}
				list.add(r);
			}
			setAverageLine(list, 5);
			setAverageLine(list, 10);
			setAverageLine(list, 20);

			for (int i = 2; i < list.size() - 5; i++) {
				HistoryRecord record_1 = list.get(i - 1);
				HistoryRecord record1 = list.get(i);
				HistoryRecord record2 = list.get(i + 1);
				HistoryRecord record3 = list.get(i + 2);
				HistoryRecord record4 = list.get(i + 3);
				HistoryRecord record5 = list.get(i + 4);

				// 第一天阴线

//				if (include(record1.getLiDu(), -7, -3) && include(record1.getPriceCh(), -9, -5.5)) { // 0.66 0.007

//				if (include(record1.getLiDu(), -10, -5) && include(record1.getPriceCh(), -9, -5.5)) { // 0.68 0.008
//				if (include(record1.getLiDu(), -15, -7) && include(record1.getPriceCh(), -9, -5.5)) { // 0.69 0.009
//				if (include(record1.getLiDu(), -100, -10) && include(record1.getPriceCh(), -9, -5.5)) { // 0.70 0.01

				if (include(record1.getLiDu(), -7, -3) && include(record1.getPriceCh(), -9, -5.5)) {

					if (include(record_1.getLiDu(), -2, 2) && record_1.getClose()<record_1.getMa10() && record1.getClose()<record1.getMa5()) {
						
						HistoryRecord buyCord = record1;
						double buy = buyCord.getClose();
						double sale = record2.getClose();

						if (sale > buy) {
							y++;
							yt = yt + (sale - buy) / buy;
						} else {
							k++;
							kt = kt + (sale - buy) / buy;
						}
						System.out.println(buyCord.getCode() + "  " + buyCord.getTime() + "  " + ((sale - buy) / buy)
								+ "   " + buyCord.getPriceCh() + "    " + buyCord.getLiDu());
					}
				}

			}
		}
		System.out.println("==== " + sta);
		System.out.println("" + (k + y) + "   " + (y * 1.0) / (k + y));

		System.out.println("zyk " + (yt + kt));
		System.out.println("pyk " + (yt + kt) / (y + k));
		System.out.println("py " + yt / y);
		System.out.println("pk " + kt / k);

		System.out.println("-----" + bz);

		// 增量上涨后 缩量回调 第三日上涨概率大
	}

	public static void Testn() {

		List<String> codeList = RedisUtils.getAllKeys();
		int totalyinNum = 0;
		double totalYin = 0.0;
		int totalkuiNum = 0;
		double totalkui = 0.0;

		int totalnum = 0;
		double total = 0.0;
		int tip = 0;
		for (String code : codeList) {
			System.out.println((tip++));

			List<HistoryRecord> list = RedisUtils.fetchData(code);
			if (list.size() == 0 || list.get(0).getTurn() == 0.0) {

				continue;
			}

			for (int i = 0; i < list.size(); i++) {

				HistoryRecord record = list.get(i);

				if (Math.abs(record.getPriceCh()) > 9
						|| (Math.abs(record.getPriceCh()) > 4.5 && Math.abs(record.getPriceCh()) < 5.5)) {

					continue;
				}

				if (record.getLiDu() == 0 || record.getTurn() < 0.1) {
					continue;
				}

				if (record.getPriceCh() > 0 && record.getLiDu() > 0) {

					totalYin = totalYin + record.getLiDu();
					totalyinNum = totalyinNum + 1;
				} else if (record.getPriceCh() < 0) {

					totalkui = totalkui + record.getLiDu();
					totalkuiNum = totalkuiNum + 1;
				}
				totalnum = totalnum + 1;
				total = total + record.getLiDu();

			}

		}

		System.out.println(totalYin / totalyinNum);
		System.out.println(totalkui / totalkuiNum);
		System.out.println(total / totalnum);

	}

	public static void setAverageLine(List<HistoryRecord> list, int days) {

		for (int i = days; i < list.size(); i++) {
			HistoryRecord record = list.get(i);
			int count = 0;
			double total = 0;
			while (count < days) {

				total = total + list.get(i - count).getClose();
				count++;
			}

			double averagePrice = total / days;

			if (days == 5) {
				record.setMa5(averagePrice);
			}
			if (days == 10) {
				record.setMa10(averagePrice);
			}
			if (days == 20) {
				record.setMa20(averagePrice);
			}
			if (days == 30) {
				record.setMa30(averagePrice);
			}
			if (days == 60) {
				record.setMa60(averagePrice);
			}
		}

	}

	public static boolean include(double value, double start1, double end1, double start2, double end2) {

		if (value >= start1 && value < end1) {
			return true;
		}
		if (value >= start2 && value < end2) {
			return true;
		}
		return false;
	}

	public static boolean include(double value, double start1, double end1) {

		if (value >= start1 && value < end1) {
			return true;
		}
		return false;
	}
}
