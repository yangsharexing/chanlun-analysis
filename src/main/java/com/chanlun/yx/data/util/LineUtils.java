package com.chanlun.yx.data.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.chanlun.yx.data.dto.Point;

//笔转线段
public class LineUtils {
	public static List<Point> bi2Line(List<Point> biLines) throws IllegalAccessException, InvocationTargetException {

		int start = 0;
		boolean finish = true;
		List<Point> lines = new ArrayList<Point>();
		lines.add(biLines.get(0));
		while (finish) {
			// 寻找第start个线段（从0开始）
			if (biLines.get(start + 1).getPrice() > biLines.get(start).getPrice()) {
				List<Point> kiLines = new ArrayList<Point>();
				kiLines.add(biLines.get(start));
				Point tempHight = biLines.get(start + 1);
				Point tempLow = biLines.get(start + 2);
				// 上升
				for (int i = start + 3; i < biLines.size(); i = i + 2) {

					if (i == biLines.size() - 1) {

						lines.add(biLines.get(i));
						finish = false;
						break;

					}
					if (tempHight.getPrice() > biLines.get(i).getPrice()
							&& tempLow.getPrice() < biLines.get(i + 1).getPrice()) {

						// 前包含后
						tempLow = biLines.get(i + 1);

						finish(kiLines, kiLines, biLines, start, i);

					} else if (tempHight.getPrice() < biLines.get(i).getPrice() &&
					// 后包含
							tempLow.getPrice() > biLines.get(i + 1).getPrice()) {

						tempHight = biLines.get(i);

						finish(kiLines, kiLines, biLines, start, i);

					} else {

						Point newHight = new Point();
						Point newLow = new Point();
						BeanUtils.copyProperties(newHight, tempHight);
						BeanUtils.copyProperties(newLow, tempLow);

						kiLines.add(newHight);
						kiLines.add(newLow);
						tempHight = biLines.get(i);
						tempLow = biLines.get(i + 1);

						if (kiLines.size() > 3) {

							int netStart = haseUpLine(kiLines, kiLines, biLines, i, finish);
							if (netStart != 0) {
								start = netStart;
								break;
							}
						}
					}
				}
			} else {
				// 下降
				List<Point> tempBiLines = new ArrayList<Point>();
				List<Point> kiLines = new ArrayList<Point>();
				Point tempHight = biLines.get(start + 2);
				Point tempLow = biLines.get(start + 1);

				// 上升
				for (int i = start + 3; i < biLines.size(); i = i + 2) {

					if (i == biLines.size() - 1) {

						lines.add(biLines.get(i));
						finish = false;
						break;

					}

					if (tempHight.getPrice() > biLines.get(i + 1).getPrice()
							&& tempLow.getPrice() < biLines.get(i).getPrice()) {

						// 前包含后
						tempHight = biLines.get(i + 1);

						finish(kiLines, kiLines, biLines, start, i);

					} else if (tempHight.getPrice() < biLines.get(i + 1).getPrice() &&
					// 后包含
							tempLow.getPrice() > biLines.get(i).getPrice()) {

						tempLow = biLines.get(i);
						finish(kiLines, kiLines, biLines, start, i);

					} else {
						Point newHight = new Point();
						Point newLow = new Point();
						BeanUtils.copyProperties(newHight, tempHight);
						BeanUtils.copyProperties(newLow, tempLow);
						kiLines.add(newLow);
						kiLines.add(newHight);
						tempLow = biLines.get(i);
						tempHight = biLines.get(i + 1);
						boolean tempflag = false;

						if (kiLines.size() > 3) {

							int netStart = haseDownLine(kiLines, kiLines, tempBiLines, i, finish);
							if (netStart != 0) {
								start = netStart;
							}
						}
						if (tempflag) {

							break;
						} else {
							finish = finish(kiLines, kiLines, biLines, start, i);
						}
					}
				}
			}
		}
		return lines;
	}

	public static int haseDownLine(List<Point> kiLines, List<Point> lines, List<Point> biLines, Integer i,
			Boolean finish) {
		boolean tempflag = false;
		int start = 0;
		if (kiLines.get(kiLines.size() - 2).getPrice() < kiLines.get(kiLines.size() - 4).getPrice()) {

			if (kiLines.get(kiLines.size() - 2).getPrice() < biLines.get(i).getPrice()) {

				if (kiLines.get(kiLines.size() - 1).getPrice() < biLines.get(i + 1).getPrice()) {

					if (kiLines.get(kiLines.size() - 4).getPrice() < kiLines.get(kiLines.size() - 1).getPrice()) {
						// 第一种线段破坏 直接可以确认上一段
						lines.add(kiLines.get(kiLines.size() - 2));
						start = i - 2;
						tempflag = true;
					} else {
						// 第二种线段破坏,有缺口
						if (biLines.get(i + 2).getPrice() < biLines.get(i).getPrice()) {
							// 线段确认
							lines.add(kiLines.get(kiLines.size() - 2));
							start = i - 2;
							tempflag = true;
						}
					}
				}
			}
		}
		if (tempflag) {
			return start;
		} else {
			finish = finish(kiLines, kiLines, biLines, start, i);
		}
		return start;
	}

	public static int haseUpLine(List<Point> kiLines, List<Point> lines, List<Point> biLines, Integer i,
			Boolean finish) {
		boolean tempflag = false;
		int start = 0;
		if (kiLines.get(kiLines.size() - 2).getPrice() > kiLines.get(kiLines.size() - 4).getPrice()) {

			if (kiLines.get(kiLines.size() - 2).getPrice() > biLines.get(i).getPrice()) {

				if (kiLines.get(kiLines.size() - 1).getPrice() > biLines.get(i + 1).getPrice()) {

					if (kiLines.get(kiLines.size() - 4).getPrice() > kiLines.get(kiLines.size() - 1).getPrice()) {
						// 上面两个条件出现顶分型
						// 第一种线段破坏 直接可以确认上一段
						lines.add(kiLines.get(kiLines.size() - 2));
						start = i - 2;
						tempflag = true;
					} else {
						// 第二种线段破坏,有缺口

						if ((i + 2) > biLines.size()) {

						}

						if (biLines.get(i + 2).getPrice() < biLines.get(i).getPrice()) {
							// 线段确认
							lines.add(kiLines.get(kiLines.size() - 2));
							start = i - 2;
							tempflag = true;
						}
					}
				}
			}
		}
		if (tempflag) {
			return start;
		} else {
			finish = finish(kiLines, kiLines, biLines, start, i);
		}
		return start;
	}

	// 最后一个节点还未形成以一段，怎么终点为最后一点
	public static void finish(List<Point> kiLines, List<Point> lines, List<Point> biLines, int start, int i) {
//		if ((((biLines.size() - start) & 1) == 1 && (i + 1) == biLines.size() - 1)
//				|| (((biLines.size() - start) & 1) != 1 && (i + 2) == biLines.size() - 1)) {
//			// 最后一条了 把最后一点加入
//			lines.add(biLines.get(biLines.size() - 1));
//			return false;
//		}
//		return true;
		if (biLines.size() - 1 == i + 1) {

			lines.add(biLines.get(biLines.size() - 1));
		}
	}

	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {

		List<Point> biLines = new ArrayList<Point>();
		Point point1 = new Point(1);
		Point point2 = new Point(2);
		Point point3 = new Point(1.5);
		Point point4 = new Point(3);
		Point point5 = new Point(1.6);
		Point point6 = new Point(4);
		Point point7 = new Point(2.5);
		Point point8 = new Point(3.5);
		Point point9 = new Point(2.2);
		biLines.add(point1);
		biLines.add(point2);
		biLines.add(point3);
		biLines.add(point4);
		biLines.add(point5);
		biLines.add(point6);
		biLines.add(point7);
		biLines.add(point8);
		biLines.add(point9);

		// 1--4--1.4
		System.out.println(bi2Line(biLines));

	}
}
