package com.chanlun.yx.data.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.chalun.yx.Exception.ExceptionCode;
import com.chalun.yx.Exception.ZSException;
import com.chanlun.yx.data.dto.Line;
import com.chanlun.yx.data.dto.Point;
import com.chanlun.yx.data.dto.TrendType;
import com.chanlun.yx.data.dto.ZhongShu;

public class ZhongShuUtils {

	public static void findZhongShu(List<Point> points, List<ZhongShu> zhList)
			throws IllegalAccessException, InvocationTargetException {
		int tempIndex = 0;
		ZhongShu tempZhongshu = null;
		Line tempLine = null;

		ZhongShu lastZhongshu = null;
		Line lastLine = null;

		List<TrendType> zoushiList = new ArrayList<TrendType>();

		while (true) {
			if (points.size() < 4) {

				throw new ZSException(ExceptionCode.DATAERROR);
				
			}
			if (tempZhongshu == null) {
				// 连续三段重叠即可确认中枢
				tempZhongshu = hasOverLap(points.get(tempIndex), points.get(tempIndex + 1), points.get(tempIndex + 2),
						points.get(tempIndex + 3));
				tempIndex = tempIndex + 4;
				continue;
			}

			double currentPrice = points.get(tempIndex).getPrice();
			if (currentPrice <= tempZhongshu.getGg() && currentPrice >= tempZhongshu.getDd()) {
				// 下一笔的终点在中枢的波动区间内
				tempZhongshu.setNum(tempZhongshu.getNum() + 1);
				tempIndex = tempIndex + 1;
				continue;
			}

			if (tempIndex + 1 >= points.size()) {
				// 笔画不够，当做最后一个点
				// DOTO

			}

			if (currentPrice > tempZhongshu.getGg()) {

				if (lastLine != null && lastLine.getType() == 0) {// 前一个中枢存在，且是从上方过来的，此时向上一笔需要考虑中枢的升级

					if (currentPrice >= lastZhongshu.getDd()) {

						// 产生波动区间的重叠 中枢升级，化为盘整

						// 1 移除原理的中枢 和最后一段
						ZhongShu preZhongshu = (ZhongShu) zoushiList.remove(zoushiList.size() - 1);
						Line preLine = (Line) zoushiList.remove(zoushiList.size() - 1);

						if (zoushiList.size() > 0) {// 原来还有中枢
							lastZhongshu = (ZhongShu) zoushiList.get(zoushiList.size() - 2);
							lastLine = (Line) zoushiList.get(zoushiList.size() - 1);
						} else {
							lastZhongshu = null;
							lastLine = null;
						}

//						合并原来的中枢;
						preZhongshu.setGg(max(preZhongshu.getGg(), tempZhongshu.getGg()));
						preZhongshu.setDd(min(preZhongshu.getDd(), tempZhongshu.getDd()));
						preZhongshu.setNum(preZhongshu.getNum() + tempZhongshu.getNum());
						preZhongshu.setLevel(preZhongshu.getLevel() + 1);// 中枢升级

						tempZhongshu = copyZhongShu(preZhongshu);
						tempLine = copyLine(tempLine);
						continue;
					}
				}
				// 往上离开
				if (points.get(tempIndex + 1).getPrice() < tempZhongshu.getGg()) {
					// 回调段回到中枢波动区间，中枢扩张
					tempZhongshu.setNum(tempZhongshu.getNum() + 1);
					tempZhongshu.setGg(currentPrice);
					tempIndex = tempIndex + 1;
					continue;
				} else {
					// 回调段回到没有回到中枢 中枢完成
					tempZhongshu.setDirect(1);// 中枢向上
					tempLine = new Line(points.get(tempIndex - 1), points.get(tempIndex));
					tempLine.setDirect(1);

					// 将中枢与离开段存入集合
					zoushiList.add(copyZhongShu(tempZhongshu));
					zoushiList.add(copyLine(tempLine));
					tempZhongshu = null;
//					tempIndex 保持不变
					continue;

				}

			} else {// 由于前面已经 判断了 不属于波动区间，所以这里else 表示currentPrice<tempZhongshu.getDd();
					// 往下离开

				if (lastLine != null && lastLine.getType() == 1) {// 前一个中枢存在，且是从上方过来的，此时向上一笔需要考虑中枢的升级

					if (currentPrice <= lastZhongshu.getGg()) {

						// 产生波动区间的重叠 中枢升级，化为盘整

						// 1 移除原理的中枢 和最后一段
						ZhongShu preZhongshu = (ZhongShu) zoushiList.remove(zoushiList.size() - 1);
						Line preLine = (Line) zoushiList.remove(zoushiList.size() - 1);

						if (zoushiList.size() > 0) {// 原来还有中枢
							lastZhongshu = (ZhongShu) zoushiList.get(zoushiList.size() - 2);
							lastLine = (Line) zoushiList.get(zoushiList.size() - 1);
						} else {
							lastZhongshu = null;
							lastLine = null;
						}

//						合并原来的中枢;
						preZhongshu.setGg(max(preZhongshu.getGg(), tempZhongshu.getGg()));
						preZhongshu.setDd(min(preZhongshu.getDd(), tempZhongshu.getDd()));
						preZhongshu.setNum(preZhongshu.getNum() + tempZhongshu.getNum());
						preZhongshu.setLevel(preZhongshu.getLevel() + 1);// 中枢升级
						tempZhongshu = copyZhongShu(preZhongshu);
						tempLine = copyLine(tempLine);
						continue;
					}
				}

				if (points.get(tempIndex + 1).getPrice() > tempZhongshu.getDd()) {
					// 回拉段回到中枢波动区间，中枢扩张
					tempZhongshu.setNum(tempZhongshu.getNum() + 1);
					tempZhongshu.setDd(currentPrice);
					tempIndex = tempIndex + 1;
					continue;
				} else {
					// 回拉段回到没有回到中枢 中枢完成

					// 回调段回到没有回到中枢 中枢完成
					tempZhongshu.setDirect(0);// 中枢向上
					tempLine = new Line(points.get(tempIndex - 1), points.get(tempIndex));
					tempLine.setDirect(0);

					// 将中枢与离开段存入集合
					zoushiList.add(copyZhongShu(tempZhongshu));
					zoushiList.add(copyLine(tempLine));

					tempZhongshu = null;
//					tempIndex 保持不变
					continue;
				}
			}
		}
	}

	private static double min(double... args) {
		double min = args[0];
		for (double v : args) {

			if (v < min) {

				min = v;
			}
		}
		return min;
	}

	private static double max(double... args) {
		double max = args[0];
		for (double v : args) {

			if (v > max) {

				max = v;
			}
		}
		return max;
	}

	private static ZhongShu hasOverLap(Point point1, Point point2, Point point3, Point point4) {

		ZhongShu zhongshu = new ZhongShu();

		if ((point3.getPrice() > point1.getPrice() && point3.getPrice() < point2.getPrice())
				|| point3.getPrice() > point2.getPrice() && point3.getPrice() < point1.getPrice()) {

			zhongshu.setGg(max(point1, point2, point3, point4));
			zhongshu.setDd(min(point1, point2, point3, point4));
			zhongshu.setZg(zg(point1, point2, point3, point4));
			zhongshu.setZg(zd(point1, point2, point3, point4));

			return zhongshu;
		}

		return null;
	}

	private static double zd(Point point1, Point point2, Point point3, Point point4) {

		if (point3.getPrice() > point2.getPrice()) {

			return point2.getPrice();
		} else {
			return point3.getPrice();
		}
	}

	private static double zg(Point point1, Point point2, Point point3, Point point4) {
		if (point3.getPrice() > point2.getPrice()) {

			return point3.getPrice();
		} else {
			return point2.getPrice();
		}
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

	private static ZhongShu copyZhongShu(ZhongShu org) throws IllegalAccessException, InvocationTargetException {

		ZhongShu zs = new ZhongShu();
		BeanUtils.copyProperties(zs, org);
		return zs;
	}

	private static Line copyLine(Line org) throws IllegalAccessException, InvocationTargetException {
		Line zs = new Line();
		BeanUtils.copyProperties(zs, org);
		return zs;
	}

}
