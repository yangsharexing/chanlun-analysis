package com.chanlun.yx.data.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ArrayUtils;

import com.chalun.yx.Exception.ExceptionCode;
import com.chalun.yx.Exception.ZSException;
import com.chanlun.yx.data.dto.Line;
import com.chanlun.yx.data.dto.Point;
import com.chanlun.yx.data.dto.TrendType;
import com.chanlun.yx.data.dto.ZhongShu;

public class ZhongShuUtils {

	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {

		List<Point> points = new ArrayList<Point>();
		Point p1 = new Point("1", 0, 0);
		Point p2 = new Point("2", 2, 1);
		Point p3 = new Point("3", 1, 0);
		Point p4 = new Point("4", 3, 1);
		Point p5 = new Point("5", 1, 0);

		Point p6 = new Point("6", 6, 1);
		Point p7 = new Point("7", 5, 0);
		Point p8 = new Point("8", 6.5, 1);
		Point p9 = new Point("9", 4, 0);
		Point p10 = new Point("10", 10, 1);
		Point p11 = new Point("11", 8, 0);
		Point p12 = new Point("12", 11, 1);
		Point p13 = new Point("13", 7, 0);
		Point p14 = new Point("14", 14, 1);
		Point p15 = new Point("15", 5, 0);
		Point p16 = new Point("16", 8, 1);

		points.add(p1);
		points.add(p2);
		points.add(p3);
		points.add(p4);
		points.add(p5);
		points.add(p6);
		points.add(p7);
		points.add(p8);
		points.add(p9);
		points.add(p10);
		points.add(p11);
		points.add(p12);
		points.add(p13);
		points.add(p14);
		points.add(p15);
		points.add(p16);

		List<TrendType> list = findZhongShu(points);
		for (TrendType ten : list) {

			System.out.println(ten);
		}

	}

	public static List<TrendType> findZhongShu(List<Point> points)
			throws IllegalAccessException, InvocationTargetException {

		if (points.size() < 4) {
//			throw new ZSException(ExceptionCode.DATAERROR);
			return null;
		}

		int tempIndex = 0;
		ZhongShu tempZhongshu = null;
		Line tempLine = null;

		ZhongShu lastZhongshu = null;
		Line lastLine = null;

		List<TrendType> zoushiList = new ArrayList<TrendType>();

		while (true) {

			if (tempZhongshu == null) {
				// 连续三段重叠即可确认中枢

				if (tempIndex + 3 > points.size() - 1) {

					// 结束，最后一个点当作离开中枢最后一点
					if (zoushiList.size() > 0) {

						lastLine = (Line) zoushiList.get(zoushiList.size() - 1);
						lastLine.setEndPoint(points.get(points.size() - 1));

						zoushiList.remove(zoushiList.size() - 1);
						zoushiList.add(lastLine);

						break;

					} else {
						// 一个确认的中枢没找到就没数据了，直接推出即可
						break;
					}
				}
				tempZhongshu = hasOverLap(points.get(tempIndex), points.get(tempIndex + 1), points.get(tempIndex + 2),
						points.get(tempIndex + 3));

				if (tempIndex + 4 > points.size() - 1) {

					if (tempZhongshu != null) {

						// 整个走势以中枢结束，中枢后面并没有线段
						zoushiList.add(tempZhongshu);
						break;
					} else {
						// 结束，最后一个点当作离开中枢最后一点
						lastLine = (Line) zoushiList.get(zoushiList.size() - 1);
						lastLine.setEndPoint(points.get(points.size() - 1));
						zoushiList.remove(zoushiList.size() - 1);
						zoushiList.add(lastLine);
						break;
					}
				} else {
					if (tempZhongshu == null) {
						if (zoushiList.size() > 0) {
							// 存在中枢的情况下，出现连续3段没有重复，此时中枢离开段延伸
							lastLine = (Line) zoushiList.get(zoushiList.size() - 1);
							lastLine.setEndPoint(points.get(tempIndex + 2));
							zoushiList.remove(zoushiList.size() - 1);
							lastLine.setNum(lastLine.getNum() + 1);
							zoushiList.add(lastLine);
							tempIndex = tempIndex + 2;
						} else {
							// 不存在中枢的情况下，出现连续3段没有重复，前面的线段忽略
							tempIndex = tempIndex + 2;
							
						}
						if(tempIndex >points.size()-1) {
							break;
						}
						continue;
					} else {
						tempIndex = tempIndex + 4;
						if(tempIndex >points.size()-1) {
							break;
						}
						continue;
					}
				}
			}
			
			//FIXME
			if(tempIndex + 1 >points.size()-1) {
				
				//下一笔到头了
				// 没有更多的点来轮询，就此打住，最后一个中枢放入走势链
				zoushiList.add(tempZhongshu);
				break;
				
			}
			if(tempIndex >points.size()-1) {
				break;
			}
			Point currentPoint = points.get(tempIndex);
			double currentPrice = points.get(tempIndex).getPrice();
			if (currentPrice <= tempZhongshu.getGg() && currentPrice >= tempZhongshu.getDd()) {
				// 下一笔的终点在中枢的波动区间内
				tempZhongshu.setNum(tempZhongshu.getNum() + 1);
//				if(tempZhongshu.getNum()==9){ //中枢暂时不升级
//					
//					tempZhongshu.set
//					
//				}
				tempZhongshu.setEndTime(currentPoint.getTime());

				if (tempIndex + 1 > points.size() - 1) {
					// 没有更多的点来轮询，就此打住，最后一个中枢放入走势链
					zoushiList.add(tempZhongshu);
					break;
				}

				// 否则继续轮询
				tempIndex = tempIndex + 1;
				if(tempIndex >points.size()-1) {
					break;
				}
				continue;
			}

			if (currentPrice > tempZhongshu.getGg()) {

				if (lastZhongshu != null && currentPrice >= lastZhongshu.getDd()
						&& currentPrice <= lastZhongshu.getGg()) {

					if (lastLine != null && lastLine.getDirect() == 0) {// 前一个中枢存在，且是从上方过来的，此时向上一笔需要考虑中枢的升级

						// 产生波动区间的重叠 中枢升级，化为盘整
						// 1 移除原理的中枢 和最后一段
						Line preLine = (Line) zoushiList.remove(zoushiList.size() - 1);
						ZhongShu preZhongshu = (ZhongShu) zoushiList.remove(zoushiList.size() - 1);

						if (zoushiList.size() > 0) {// 原来还有中枢
							lastZhongshu = (ZhongShu) zoushiList.get(zoushiList.size() - 2);
							lastLine = (Line) zoushiList.get(zoushiList.size() - 1);
						} else {
							lastZhongshu = null;
							lastLine = null;
						}

						// 合并原来的中枢;
						preZhongshu.setGg(max(preZhongshu.getGg(), tempZhongshu.getGg()));
						preZhongshu.setDd(min(preZhongshu.getDd(), tempZhongshu.getDd()));
						preZhongshu.setNum(preZhongshu.getNum() + tempZhongshu.getNum() + preLine.getNum() + 2);// 最后一段记得加上
						preZhongshu.setLevel(preZhongshu.getLevel() + 1);// 中枢升级
						preZhongshu.setEndTime(currentPoint.getTime());
						tempZhongshu = copyZhongShu(preZhongshu);
						tempLine = copyLine(tempLine);
						tempIndex = tempIndex + 1;
						if(tempIndex >points.size()-1) {
							break;
						}
						continue;
					}
				}
				
				// 往上离开
				if (points.get(tempIndex + 1).getPrice() < tempZhongshu.getGg()) {
					// 回调段回到中枢波动区间，中枢扩张
					tempZhongshu.setNum(tempZhongshu.getNum() + 1);
					tempZhongshu.setGg(currentPrice);
					tempIndex = tempIndex + 1;
					if(tempIndex >points.size()-1) {
						break;
					}
					continue;
				} else {
					// 回调段回到没有回到中枢 中枢完成
					tempZhongshu.setDirect(1);// 中枢向上
					tempZhongshu.setEndTime(points.get(tempIndex - 1).getTime());
					tempLine = new Line(points.get(tempIndex - 1), points.get(tempIndex));
					tempLine.setDirect(1);
					tempLine.setNum(1);

					// 将中枢与离开段存入集合
					zoushiList.add(copyZhongShu(tempZhongshu));
					zoushiList.add(copyLine(tempLine));

					lastZhongshu = copyZhongShu(tempZhongshu);
					lastLine = copyLine(tempLine);

					tempZhongshu = null;
					// tempIndex 保持不变
					if(tempIndex >points.size()-1) {
						break;
					}
					continue;

				}

			} else {// 由于前面已经 判断了 不属于波动区间，所以这里else
					// 表示currentPrice<tempZhongshu.getDd();
					// 往下离开

				if (lastZhongshu != null && currentPrice <= lastZhongshu.getGg()
						&& currentPrice >= lastZhongshu.getDd()) {

					if (lastLine != null && lastLine.getDirect() == 1) {// 前一个中枢存在，且是从上方过来的，此时向上一笔需要考虑中枢的升级

						// 产生波动区间的重叠 中枢升级，化为盘整

						// 1 移除原理的中枢 和最后一段
						Line preLine = (Line) zoushiList.remove(zoushiList.size() - 1);
						ZhongShu preZhongshu = (ZhongShu) zoushiList.remove(zoushiList.size() - 1);

						if (zoushiList.size() > 0) {// 原来还有中枢
							lastZhongshu = (ZhongShu) zoushiList.get(zoushiList.size() - 2);
							lastLine = (Line) zoushiList.get(zoushiList.size() - 1);
						} else {
							lastZhongshu = null;
							lastLine = null;
						}

						// 合并原来的中枢;
						preZhongshu.setGg(max(preZhongshu.getGg(), tempZhongshu.getGg()));
						preZhongshu.setDd(min(preZhongshu.getDd(), tempZhongshu.getDd()));
						preZhongshu.setNum(preZhongshu.getNum() + tempZhongshu.getNum() + preLine.getNum() + 1);// 最后一段记得加上
						preZhongshu.setLevel(preZhongshu.getLevel() + 1);// 中枢升级
						preZhongshu.setEndTime(currentPoint.getTime());
						tempZhongshu = copyZhongShu(preZhongshu);
						tempLine = copyLine(tempLine);
						tempIndex = tempIndex + 1;
						if(tempIndex >points.size()-1) {
							break;
						}
						continue;
					}
				}
				
				//FIXME
				if(tempIndex + 1 >points.size()-1) {
					
					//下一笔到头了
					// 没有更多的点来轮询，就此打住，最后一个中枢放入走势链
					zoushiList.add(tempZhongshu);
					break;
					
				}

				if (points.get(tempIndex + 1).getPrice() > tempZhongshu.getDd()) {
					// 回拉段回到中枢波动区间，中枢扩张
					tempZhongshu.setNum(tempZhongshu.getNum() + 1);
					tempZhongshu.setDd(currentPrice);
					tempIndex = tempIndex + 1;
					if(tempIndex >points.size()-1) {
						break;
					}
					continue;
				} else {
					// 回拉段回到没有回到中枢 中枢完成

					// 回调段回到没有回到中枢 中枢完成
					tempZhongshu.setDirect(0);// 中枢向上
					tempZhongshu.setEndTime(points.get(tempIndex - 1).getTime());
					tempLine = new Line(points.get(tempIndex - 1), points.get(tempIndex));
					tempLine.setDirect(0);
					tempLine.setNum(1);

					// 将中枢与离开段存入集合
					zoushiList.add(copyZhongShu(tempZhongshu));
					zoushiList.add(copyLine(tempLine));
					lastZhongshu = copyZhongShu(tempZhongshu);
					lastLine = copyLine(tempLine);

					tempZhongshu = null;
					// tempIndex 保持不变
					if(tempIndex >points.size()-1) {
						break;
					}
					continue;
				}
			}
		}
		return zoushiList;
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

	private static double zd(Point point1, Point point2, Point point3, Point point4) {

		if (point3.getPrice() > point2.getPrice()) {

			return point2.getPrice();
		} else {
			return point3.getPrice();
		}
	}

	private static double zg(Point point1, Point point2, Point point3, Point point4) {

//		double max = max(point1,point2,point3,point4);
		return 1;

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

	private static Point copyPoint(Point point) {
		Point p = new Point();
		p.setPrice(point.getPrice());
		p.setTime(point.getTime());
		p.setType(point.getType());
		return p;
	}
}
