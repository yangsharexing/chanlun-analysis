package com.chanlun.yx.data.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.chanlun.yx.data.dto.Line;
import com.chanlun.yx.data.dto.Point;
import com.chanlun.yx.data.dto.ZhongShu;

public class MyChanUtils {

	public static void struck(List<Point> points) {
		
		List<ZhongShu> zhognshuList = new ArrayList<ZhongShu>();

		ZhongShu currentZhongshu = new ZhongShu();
		int startIndex = -1;
		// 寻找第一个中枢
		for (int i = 0; i + 3 < points.size(); i++) {

			currentZhongshu = hasOverLap(points.get(i), points.get(i + 1), points.get(i + 2), points.get(i + 3));
			if (currentZhongshu != null) {
				startIndex = i + 3;
				break;
			}
		}
		if (currentZhongshu == null) {
			return;
		}
		int index = startIndex + 1;
		while (index < points.size()) {

			// 向上
			if (points.get(index + 1).getType() == 0) {

				// 新一点回调是否回到中枢
				if (points.get(index + 1).getPrice() > currentZhongshu.getGg()) {
					// 没
//					离开确认
					Line line = new Line();
					line.setStartPoint(points.get(index-1));
					line.setEndPoint(points.get(index));
					line.setType(1);
					line.setNum(1);
					

				} else {
					// 回到中枢,刷新中枢
					flushZhongshu(currentZhongshu, points.get(index), points.get(index + 1), 1);

				}
			}
			// 向下
			else {

				// 新一点回调是否回到中枢
				if (points.get(index + 1).getPrice() < currentZhongshu.getDd()) {
					// 没

				} else {
					// 回到中枢,刷新中枢
					flushZhongshu(currentZhongshu, points.get(index), points.get(index + 1), 0);
				}
			}
			index++;
		}
	}

	// type =1 表示向上离开 =0表示像下离开
	private static void flushZhongshu(ZhongShu currentZhongshu, Point point, Point point2, int type) {
		if (type == 1) {

			if (point.getPrice() > currentZhongshu.getGg()) {
				currentZhongshu.setGg(point.getPrice());
			}
		} else {
			if (point.getPrice() < currentZhongshu.getGg()) {
				currentZhongshu.setDd(point.getPrice());
			}
		}
		currentZhongshu.setNum(currentZhongshu.getNum() + 1);
	}

	private static ZhongShu hasOverLap(Point point1, Point point2, Point point3, Point point4) {

		ZhongShu zhongshu = new ZhongShu();
		if (max(point1, point2) < min(point3, point4) || min(point1, point2) > max(point3, point4)) {

		} else {
			zhongshu.setGg(max(point1, point2, point3, point4));
			zhongshu.setDd(min(point1, point2, point3, point4));
			zhongshu.setZg(sort(point1, point2, point3, point4).get(2).getPrice());
			zhongshu.setZd(sort(point1, point2, point3, point4).get(1).getPrice());
			zhongshu.setNum(3);
			zhongshu.setLevel(1);
			zhongshu.setStartTime(point1.getTime());
			zhongshu.setEndTime(point3.getTime());
			zhongshu.setType(1);
			return zhongshu;
		}

		return null;
	}

	private static double max(Point... points) {

		double max = points[0].getPrice();
		for (Point p : points) {

			max = p.getPrice() > max ? p.getPrice() : max;
		}

		return max;

	}

	private static double min(Point... points) {

		double min = points[0].getPrice();
		for (Point p : points) {

			min = p.getPrice() < min ? p.getPrice() : min;
		}
		return min;
	}

	private static List<Point> sort(Point... points) {

		for (int i = 0; i < points.length; i++) {

			for (int j = i + 1; j < points.length; j++) {

				if (points[i].getPrice() > points[j].getPrice()) {
					// 一次循环找出最大的
					Point tempPoint = points[j];
					points[j] = points[i];
					points[i] = tempPoint;
				}
			}
		}
		return Arrays.asList(points);
	}
}
