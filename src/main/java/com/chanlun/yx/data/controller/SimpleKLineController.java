package com.chanlun.yx.data.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chanlun.yx.data.dto.AllHistoryRecord;
import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.dto.Point;
import com.chanlun.yx.data.dto.SimpleRecord;
import com.chanlun.yx.data.dto.VpYDto;
import com.chanlun.yx.data.model.MiDataWithBLOBs;
import com.chanlun.yx.data.my.util.VPUtils;
import com.chanlun.yx.data.service.MiDataService;
import com.chanlun.yx.data.util.BiLineUtils;
import com.chanlun.yx.data.util.KLineUtils;
import com.chanlun.yx.data.util.LineUtils;
import com.chanlun.yx.data.util.TechnicalIndexUtils;
import com.chanlun.yx.redis.RedisUtils;

@RestController
public class SimpleKLineController {

	@Autowired
	private MiDataService service;

	@RequestMapping("/getData")
	public Map<String, List> getLine(String code) throws IllegalAccessException, InvocationTargetException {

		List<HistoryRecord> list = RedisUtils.fetchData(code);
		// System.out.println("原始k线" + list.size());

		List<HistoryRecord> list2 = KLineUtils.handleKLine(list);
		// System.out.println("合并后k线" + list2.size());

		List<Point> point = BiLineUtils.contructBiLines(list2);
		// System.out.println("笔数点为" + point.size());

		List<Point> point2 = LineUtils.bi2Line(point);
		// System.out.println("线段点为" + point2.size());

		// 计算相对高低点
		// 1、缠论简化k线过后顶分型与底分型构成的点集合为超短期走势
		// 2、将所有的底合并 将所有的顶合并 提取出来组成 短期走势
		// 3、将2中提取出来的在用类似2的处理方法提取出来组成中期走势
		// 4、将3中提取出来的再用类似2的处理方法提取出来的组成长期走势
		// 这里list2为简化过的K
		List<Point> level_1 = new ArrayList<Point>();

		for (int i = 3; i < list2.size(); i++) {

			HistoryRecord a1 = list2.get(i - 2);
			HistoryRecord a2 = list2.get(i - 1);
			HistoryRecord a3 = list2.get(i);

			Point fpoint = isFenXin(a1, a2, a3);
			if (fpoint != null) {

				level_1.add(fpoint);
			}
		}

		List<String> list_0 = new ArrayList<String>();
		List<Double> list_1 = new ArrayList<Double>();
		List<Point> level_1_c = level_1.subList(level_1.size() - 200, level_1.size() - 1);
		System.out.println(level_1_c.size());
		for (Point point_ : level_1_c) {
			list_0.add(point_.getTime());
			list_1.add(point_.getPrice());
		}

		List<Point> level_2 = new ArrayList<Point>();
		int start = 0;
		while (start < level_1_c.size()) {
			

			if (start == level_1_c.size() - 2) {
				level_2.add(level_1_c.get(level_1_c.size() - 2));
				level_2.add(level_1_c.get(level_1_c.size() - 1));
				break;
			}

			if (level_1_c.get(start).getType() == 1) {

				for (int i = start + 2;; i = i + 2) {
					if (i < level_1_c.size()) {
						if (level_1_c.get(i).getPrice() >= level_1_c.get(start).getPrice()) {
							// 还有更高顶点
							start = start + 2;
							continue;

						} else {
							// 顶点确认
							if(level_1_c.get(start).getPrice()==18.35) {
								System.out.println();
							}
							level_2.add(level_1_c.get(start));
							start = start + 1;
							break;
						}
					} else {
						break;
					}
				}

			} else {
				for (int i = start + 2;; i = i + 2) {

					if (i < level_1_c.size()) {
						if (level_1_c.get(i).getPrice() <= level_1_c.get(start).getPrice()) {
							// 还有更低点
							start = start + 2;
							continue;

						} else {
							// 低点确认
							level_2.add(level_1_c.get(start));
							start = start + 1;
							break;

						}
					} else {
						break;
					}

				}
			}
		}
		List<String> list2_0 = new ArrayList<String>();
		List<Double> list2_1 = new ArrayList<Double>();
		for (Point p2 : level_2) {
			list2_0.add(p2.getTime());
			list2_1.add(p2.getPrice());
		}

		List<String> list3_0 = new ArrayList<String>();
		List<Double> list3_1 = new ArrayList<Double>();
		for (Point p : point) {
			list3_0.add(p.getTime());
			list3_1.add(p.getPrice());
		}

		List<String> list4_0 = new ArrayList<String>();
		List<Double> list4_1 = new ArrayList<Double>();
		for (Point p : point2) {
			list4_0.add(p.getTime());
			list4_1.add(p.getPrice());
		}

//	List<String> list_0 = new ArrayList<String>();
//	List<Double> list_1 = new ArrayList<Double>();
//	for (HistoryRecord record : list) {
//		list_0.add(record.getEndTime());
//		list_1.add(record.getLow() / 2 + record.getHigh() / 2);
//	}

//	List<String> list2_0 = new ArrayList<String>();
//	List<Double> list2_1 = new ArrayList<Double>();for(
//	HistoryRecord record:list2)
//	{
//		list2_0.add(record.getEndTime());
//		list2_1.add(record.getLow() / 2 + record.getHigh() / 2);
//	}

		Map<String, List> map = new HashMap<String, List>();
		map.put("list_0", list_0);
		map.put("list_1", list_1);
		map.put("list2_0", list2_0);
		map.put("list2_1", list2_1);
		map.put("list3_0", list3_0);
		map.put("list3_1", list3_1);
		map.put("list4_0", list4_0);
		map.put("list4_1", list4_1);

		return map;
	}

	@RequestMapping("/macd")
	public Map<String, List> getMacd(String code) throws IllegalAccessException, InvocationTargetException {

		code = "day.java.sh.600283";
		List<HistoryRecord> list = RedisUtils.fetchData(code);
		TechnicalIndexUtils.computeMacd(list);
		// System.out.println("原始k线" + list.size());
		for (HistoryRecord record : list) {

			System.out.println(record.getTime() + ": bar:" + record.getBar() + "   macd:" + record.getMacd() + "  dif:"
					+ record.getDif());

		}

		return null;
	}

	@RequestMapping("/getMyData")
	public Map<String, List> getMyLine(String code) throws IllegalAccessException, InvocationTargetException {

		List<AllHistoryRecord> list = RedisUtils.fetchDayData(code);
		// System.out.println("原始k线" + list.size());

		List<VpYDto> vpList = VPUtils.vp(list);

		List<VpYDto> pcList = VPUtils.pc(list);

		List<String> list_0 = new ArrayList<String>();
		List<Double> list_1 = new ArrayList<Double>();
		for (VpYDto record : vpList) {
			list_0.add(record.getTime());
			list_1.add(record.getY());
		}

		List<String> list2_0 = new ArrayList<String>();
		List<Double> list2_1 = new ArrayList<Double>();
		for (VpYDto record : pcList) {
			list2_0.add(record.getTime());
			list2_1.add(record.getY());
		}

		List<String> list3_0 = new ArrayList<String>();
		List<Double> list3_1 = new ArrayList<Double>();
		for (AllHistoryRecord record : list) {
			list3_0.add(record.getTime());
			list3_1.add(record.getClose());
		}

		Map<String, List> map = new HashMap<String, List>();
		map.put("list_0", list_0);
		map.put("list_1", list_1);
		map.put("list2_0", list2_0);
		map.put("list2_1", list2_1);
		map.put("list3_0", list3_0);
		map.put("list3_1", list3_1);

		return map;
	}

	public static Point isFenXin(HistoryRecord recordA, HistoryRecord recordB, HistoryRecord recordC) {
		Point point = new Point();
		if (recordB.getHigh() > recordA.getHigh() && recordB.getHigh() > recordC.getHigh()) {
			// 顶
			point.setPrice(recordB.getHigh());
			point.setTime(recordB.getEndTime());
			point.setType(1);
			return point;

		}

		if (recordB.getLow() < recordA.getLow() && recordB.getLow() < recordC.getLow()) {
			// 底
			point.setPrice(recordB.getLow());
			point.setTime(recordB.getEndTime());
			point.setType(0);
			return point;
		}

		return null;

	}
}
