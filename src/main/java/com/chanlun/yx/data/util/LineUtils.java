package com.chanlun.yx.data.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.chanlun.yx.data.dto.BiDesc;
import com.chanlun.yx.data.dto.Point;

//笔转线段
public class LineUtils {
	
//	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {
//		
//		
//		List<Point> biLines  = new ArrayList<Point>();
//		Point p1 = new Point("1", 1, 0, 1000);
//		Point p2 = new Point("2", 3, 1, 1000);
//		Point p3 = new Point("3", 1.2, 0, 1000);
//		
//		Point pa = new Point("a", 2, 1, 1000);
//		Point pb = new Point("a", 1.5, 0, 1000);
//		
//		Point p4 = new Point("4", 2.5, 1, 1000);
//		Point p5 = new Point("5", 1, 0, 1000);
//		Point p6 = new Point("6", 4, 1, 1000);
//		Point p7 = new Point("7", 1.5, 0, 1000);
//		Point p8 = new Point("8", 2.5, 1, 1000);
//		Point p9 = new Point("9", 1, 0, 1000);
//		
//		biLines.add(p1);
//		biLines.add(p2);
//		biLines.add(p3);
//		biLines.add(p4);
//		biLines.add(p5);
//		biLines.add(p6);
//		biLines.add(p7);
//		biLines.add(p8);
//		biLines.add(p9);
//		
//		
//		
//		System.out.println(bi2Line(biLines));
//	}
//	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {
//		
//		
//		List<Point> biLines  = new ArrayList<Point>();
//		Point p1 = new Point("1", 1, 0, 1000);
//		Point p2 = new Point("2", 3, 1, 1000);
//		Point p3 = new Point("3", 1.5, 0, 1000);
//		Point p4 = new Point("4", 2.5, 1, 1000);
//		Point p5 = new Point("5", 1, 0, 1000);
//		Point p6 = new Point("6", 4, 1, 1000);
//		Point p7 = new Point("7", 1.5, 0, 1000);
//		Point p8 = new Point("8", 2.5, 1, 1000);
//		Point p9 = new Point("9", 1, 0, 1000);
//		
//		biLines.add(p1);
//		biLines.add(p2);
//		biLines.add(p3);
//		biLines.add(p4);
//		biLines.add(p5);
//		biLines.add(p6);
//		biLines.add(p7);
//		biLines.add(p8);
//		biLines.add(p9);
//		
//		
//		
//		System.out.println(bi2Line(biLines));
//	}
//	
//	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {
//		
//		
//		List<Point> biLines  = new ArrayList<Point>();
//		Point p1 = new Point("1", 1, 0, 1000);
//		Point p2 = new Point("2", 2.4, 1, 1000);
//		Point p3 = new Point("3", 2, 0, 1000);
//		Point p4 = new Point("4", 4, 1, 1000);
//		Point p5 = new Point("5", 2.5, 0, 1000);
//		Point p6 = new Point("6", 3.5, 1, 1000);
//		Point p7 = new Point("7", 3, 0, 1000);
//		Point p8 = new Point("8", 3.5, 1, 1000);
//		Point p9 = new Point("9", 2, 0, 1000);
//		Point p10 = new Point("10", 3, 1, 1000);
//		
//		
//		Point p11 = new Point("11", 0.2, 0, 1000);
//		Point p12 = new Point("12", 1, 1, 1000);
//		Point p13 = new Point("13", 0.5, 0, 1000);
//		Point p14 = new Point("14", 2, 1, 1000);
//		Point p15 = new Point("14", 0.6, 0, 1000);
//		Point p16 = new Point("14", 1, 1, 1000);
//		
//		
//		biLines.add(p1);
//		biLines.add(p2);
//		biLines.add(p3);
//		biLines.add(p4);
//		biLines.add(p5);
//		biLines.add(p6);
//		biLines.add(p7);
//		biLines.add(p8);
//		biLines.add(p9);
//		biLines.add(p10);
//		biLines.add(p11);
//		biLines.add(p12);
//		biLines.add(p13);
//		biLines.add(p14);
//		biLines.add(p15);
//		biLines.add(p16);
//		
//		
//		System.out.println(bi2Line(biLines));
//	}

	public static List<Point> bi2Line(List<Point> biLines) throws IllegalAccessException, InvocationTargetException {

		List<Point> lines = new ArrayList<Point>();
		int count = 0;
		while (true) {
			// 寻找第start个线段（从0开始）

			if (count == -1) {
				break;
			}
			count = handup(lines, biLines, count);
			if (count != -1) {
				count = handDown(lines, biLines, count);
			}

		}
		return lines;
	}

	// 向上笔开始
	private static int handup(List<Point> Lines, List<Point> biLines, int start) {

		if (Lines.size() == 0) {
			// 默认第一个点在  biLines 第一点和第二点之间
			Point point = new Point();
			point.setTime("0");
			point.setPrice((biLines.get(1).getPrice()+biLines.get(2).getPrice())/2.0);
			if(biLines.get(1).getPrice()>biLines.get(2).getPrice()){
				point.setType(0);
			}else{
				point.setType(1);
			}
			Lines.add(point);
		}
		BiDesc temptzStart = null;

		// 第一个特征向量
		temptzStart = new BiDesc(start + 1, start + 2, biLines.get(start + 1), biLines.get(start + 2), 0);

		return handleSecondTzBi(temptzStart, biLines, Lines);
	}

	// 向下笔开始
	private static int handDown(List<Point> lines, List<Point> biLines, int start) {
		BiDesc temptzStart = null;
		// 第一个特征向量
		temptzStart = new BiDesc(start + 1, start + 2, biLines.get(start + 1), biLines.get(start + 2), 1);

		return handleSecondTzBiDown(temptzStart, biLines, lines);
	}

	private static int handleSecondTzBiDown(BiDesc temptzStart, List<Point> biLines, List<Point> lines) {

		// 下一个特征向量
		BiDesc temptzMid = nextTZBi(temptzStart, biLines);

		if (temptzMid == null) {
			lines.add(biLines.get(biLines.size() - 1));
			return -1;
		}

		int yes = isContains(temptzStart, temptzMid);
		if (yes == 1) {
			// 包含关系
			temptzStart = merge(temptzStart, temptzMid);
			return handleSecondTzBiDown(temptzStart, biLines, lines);
		} else {
			// 非包含关系
			if (temptzStart.getStartBi().getPrice() < temptzMid.getStartBi().getPrice()) {
				temptzStart = copyProperties(temptzMid);
				return handleSecondTzBiDown(temptzStart, biLines, lines);
			} else {

				return handleThirdTzBiDown(temptzStart, temptzMid, biLines, lines);
			}

		}

	}

	private static int handleSecondTzBi(BiDesc temptzStart, List<Point> biLines, List<Point> lines) {

		// 下一个特征向量
		BiDesc temptzMid = nextTZBi(temptzStart, biLines);

		if (temptzMid == null) {
			lines.add(biLines.get(biLines.size() - 1));
			return -1;
		}

		int yes = isContains(temptzStart, temptzMid);
		if (yes == 1) {
			// 包含关系
			temptzStart = merge(temptzStart, temptzMid);
			return handleSecondTzBi(temptzStart, biLines, lines);
		} else {
			// 非包含关系
			if (temptzStart.getStartBi().getPrice() > temptzMid.getStartBi().getPrice()) {
				temptzStart = copyProperties(temptzMid);
				return handleSecondTzBi(temptzStart, biLines, lines);
			} else {

				return handleThirdTzBi(temptzStart, temptzMid, biLines, lines);
			}
		}
	}

	private static int handleThirdTzBi(BiDesc temptzStart, BiDesc temptzMid, List<Point> biLines, List<Point> lines) {

		// 下一个特征向量
		BiDesc temptzEnd = nextTZBi(temptzMid, biLines);

		if (temptzEnd == null) {
			lines.add(biLines.get(biLines.size() - 1));
			return -1;
		}

		int yes = isContains(temptzMid, temptzEnd);
		if (yes == 1) {
			// 包含关系
			temptzMid = merge(temptzMid, temptzEnd);
			return handleThirdTzBi(temptzStart, temptzMid, biLines, lines);
		} else {
			// 非包含关系
			if (temptzMid.getStartBi().getPrice() > temptzEnd.getStartBi().getPrice()) {
				// 顶分型确认，看是否构成缺口

				if (temptzStart.getStartBi().getPrice() >= temptzMid.getEndBi().getPrice()) {
					// 顶分型，第一种情况
					lines.add(temptzMid.getStartBi());
					return temptzMid.getStartIndex();
				} else {
					// 第二种情况 有缺口
					return handleAfterTzBi(temptzStart, temptzMid, temptzEnd, biLines, lines);
				}
			} else {
				temptzStart = copyProperties(temptzMid);
				temptzMid = copyProperties(temptzEnd);
				return handleThirdTzBi(temptzStart, temptzMid, biLines, lines);
			}
		}

	}

	private static int handleThirdTzBiDown(BiDesc temptzStart, BiDesc temptzMid, List<Point> biLines,
			List<Point> lines) {

		// 下一个特征向量
		BiDesc temptzEnd = nextTZBi(temptzMid, biLines);

		if (temptzEnd == null) {
			lines.add(biLines.get(biLines.size() - 1));
			return -1;
		}

		int yes = isContains(temptzMid, temptzEnd);
		if (yes == 1) {
			// 包含关系
			temptzMid = merge(temptzMid, temptzEnd);
			return handleThirdTzBiDown(temptzStart, temptzMid, biLines, lines);
		} else {
			// 非包含关系
			if (temptzMid.getStartBi().getPrice() < temptzEnd.getStartBi().getPrice()) {
				// 顶分型确认，看是否构成缺口

				if (temptzStart.getStartBi().getPrice() <= temptzMid.getEndBi().getPrice()) {
					// 顶分型，第一种情况
					lines.add(temptzMid.getStartBi());
					return temptzStart.getStartIndex();
				} else {
					// 第二种情况 有缺口
					return handleAfterTzBiDown(temptzStart, temptzMid, temptzEnd, biLines, lines);
				}

			} else {
				temptzStart = copyProperties(temptzMid);
				temptzMid = copyProperties(temptzEnd);
				return handleThirdTzBiDown(temptzStart, temptzMid, biLines, lines);
			}
		}

	}

	private static int handleAfterTzBi(BiDesc temptzStart, BiDesc temptzMid, BiDesc temptzEnd, List<Point> biLines,
			List<Point> lines) {
		// 下一个特征向量
		BiDesc temptzAfter = nextTZBi(temptzEnd, biLines);

		if (temptzAfter == null) {
			lines.add(biLines.get(biLines.size() - 1));
			return -1;
		}

		int yes = isContains(temptzEnd, temptzAfter);
		if (yes == 1) {
			// 包含关系
			temptzEnd = merge(temptzEnd, temptzAfter);
			return handleAfterTzBi(temptzStart, temptzMid, temptzEnd, biLines, lines);
		} else {

			if (temptzAfter.getStartBi().getPrice() < temptzEnd.getStartBi().getPrice()) {
				// 顶分型确认后 的一次反弹没有出新高，顶分型高点为线段结束点
				lines.add(temptzMid.getStartBi());
				return temptzMid.getStartIndex();
			} else {
				// 出新高，那么，回到原来的逻辑
				temptzStart = copyProperties(temptzEnd);
				temptzEnd = copyProperties(temptzAfter);
				return handleThirdTzBi(temptzStart, temptzMid, biLines, lines);

			}
		}

	}

	private static int handleAfterTzBiDown(BiDesc temptzStart, BiDesc temptzMid, BiDesc temptzEnd, List<Point> biLines,
			List<Point> lines) {
		// 下一个特征向量
		BiDesc temptzAfter = nextTZBi(temptzEnd, biLines);

		if (temptzAfter == null) {
			lines.add(biLines.get(biLines.size() - 1));
			return -1;
		}

		int yes = isContains(temptzEnd, temptzAfter);
		if (yes == 1) {
			// 包含关系
			temptzEnd = merge(temptzEnd, temptzAfter);
			return handleAfterTzBiDown(temptzStart, temptzMid, temptzEnd, biLines, lines);
		} else {

			if (temptzAfter.getStartBi().getPrice() > temptzEnd.getStartBi().getPrice()) {
				// 顶分型确认后 的一次反弹没有出新高，顶分型高点为线段结束点
				lines.add(temptzMid.getStartBi());
				return temptzMid.getStartIndex();
			} else {
				// 出新高，那么，回到原来的逻辑
				temptzStart = copyProperties(temptzEnd);
				temptzMid = copyProperties(temptzAfter);
				return handleThirdTzBiDown(temptzStart, temptzMid, biLines, lines);
			}
		}

	}

	// 下一个特征向量，如果超出了界限就返回空
	private static BiDesc nextTZBi(BiDesc temptzStart, List<Point> biLines) {

		if (temptzStart.getEndIndex() + 2 > biLines.size() - 1) {
			return null;
		}
		int start = temptzStart.getEndIndex() + 1;
		int end = temptzStart.getEndIndex() + 2;
		return new BiDesc(start, end, biLines.get(start), biLines.get(end), temptzStart.getType());
	}

	public static int isContains(BiDesc biA, BiDesc biB) {

		if (biA.getType() != biB.getType()) {
			System.out.println("1111111数据错误111111");
			System.out.println("1111111数据错误111111");
			System.out.println("1111111数据错误111111");
			System.out.println("1111111数据错误111111");
			return -2;// 方向不一样不能比较
		}

		if (biA.getType() == 1) {// 向上

			// biA 包含biB
			if (biA.getStartBi().getPrice() < biB.getStartBi().getPrice()
					&& biA.getEndBi().getPrice() > biB.getEndBi().getPrice()) {

				return 1;

			}

			// biB 包含biA
			if (biA.getStartBi().getPrice() > biB.getStartBi().getPrice()
					&& biA.getEndBi().getPrice() < biB.getEndBi().getPrice()) {

				return -1;
			}

		} else {// 向下

			// biA 包含biB
			if (biA.getStartBi().getPrice() > biB.getStartBi().getPrice()
					&& biA.getEndBi().getPrice() < biB.getEndBi().getPrice()) {

				return 1;

			}

			// biB 包含biA
			if (biA.getStartBi().getPrice() < biB.getStartBi().getPrice()
					&& biA.getEndBi().getPrice() > biB.getEndBi().getPrice()) {

				return -1;
			}

		}

		return 0;

	}

	// 合并
	public static BiDesc merge(BiDesc biA, BiDesc biB) {
		BiDesc result = new BiDesc();
		int fx = isContains(biA, biB);
		if (biA.getType() == 1) {// 向上笔特征 表示 是下的走势
			if (fx == 1) {// A包含B 方向向上
				result.setStartIndex(biA.getStartIndex());
				result.setEndIndex(biB.getEndIndex());
				result.setStartBi(biA.getStartBi());
				result.setEndBi(biB.getEndBi());
				result.setType(1);
			} else if (fx == -1) {
				result.setStartIndex(biB.getStartIndex());
				result.setEndIndex(biA.getEndIndex());
				result.setStartBi(biB.getStartBi());
				result.setEndBi(biA.getEndBi());
				result.setType(1);
			}
		} else {// 向下笔特征 表示 是上的走势
			if (fx == 1) {// A包含B 方向向上
				result.setStartIndex(biA.getStartIndex());
				result.setEndIndex(biB.getEndIndex());
				result.setStartBi(biA.getStartBi());
				result.setEndBi(biB.getEndBi());
				result.setType(0);
			} else if (fx == -1) {
				result.setStartIndex(biB.getStartIndex());
				result.setEndIndex(biA.getEndIndex());
				result.setStartBi(biB.getStartBi());
				result.setEndBi(biA.getEndBi());
				result.setType(0);
			}
		}
		return result;
	}

	public static BiDesc copyProperties(BiDesc biDesc) {
		return new BiDesc(biDesc.getStartIndex(),biDesc.getEndIndex(),copyPoint(biDesc.getStartBi()), copyPoint(biDesc.getEndBi()), biDesc.getType());
	}

	public static Point copyPoint(Point point) {

		Point result = new Point();
		result.setPrice(point.getPrice());
		result.setTime(point.getTime());
		result.setType(point.getType());
		return result;
	}
}
