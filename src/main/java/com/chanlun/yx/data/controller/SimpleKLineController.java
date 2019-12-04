package com.chanlun.yx.data.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.dto.Point;
import com.chanlun.yx.data.dto.SimpleRecord;
import com.chanlun.yx.data.handler.KLineHandler;
import com.chanlun.yx.data.model.MiData;
import com.chanlun.yx.data.model.MiDataWithBLOBs;
import com.chanlun.yx.data.service.MiDataService;
import com.chanlun.yx.data.util.BiLineUtils;
import com.chanlun.yx.data.util.KLineUtils;

@RestController
public class SimpleKLineController {
	
	@Autowired
	private MiDataService service;

	@RequestMapping("/hello")
	public String Hello(String name) {

		Map<String, Integer> map = new HashMap<String, Integer>();
		List<HistoryRecord> records = new ArrayList<HistoryRecord>();
		HistoryRecord record = null;
		int count = 0;

		/* 读取数据 */
		try {

			// ：开盘(open)，收盘(close)，最低(lowest)，最高(highest)
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File("C:/data.txt")), "UTF-8"));
			String lineTxt = null;
			while ((lineTxt = br.readLine()) != null) {
				String[] names = lineTxt.split(",");
				record = new HistoryRecord();
				record.setTime(names[0]);
				record.setOpen(Double.parseDouble(names[3]));
				record.setClose(Double.parseDouble(names[4]));
				// record.setOpen(Double.parseDouble(names[1]));
				// record.setClose(Double.parseDouble(names[2]));
				record.setLow(Double.parseDouble(names[3]));
				record.setHigh(Double.parseDouble(names[4]));
				record.setStartTime(names[0]);
				record.setEndTime(names[0]);
				record.setVolume(100);
				records.add(record);
				count++;
			}
			br.close();
		} catch (Exception e) {
			System.err.println("read errors :" + e);
		}
		records = KLineUtils.handleKLine(records);

		// 打印最高值，最低值
		double higthv = records.get(0).getHigh();
		double lowv = records.get(0).getLow();
		for (HistoryRecord re : records) {
			if (re.getHigh() > higthv) {
				higthv = re.getHigh();
			}
			if (re.getLow() < lowv) {
				lowv = re.getLow();
			}
		}
		// System.out.println(higthv);
		// System.out.println(lowv);

		List<List<Object>> listos = new ArrayList<List<Object>>();
		List<SimpleRecord> simpleRecord = new ArrayList<SimpleRecord>();
		SimpleRecord sRecord = null;

		List<Object> listo = null;
		for (HistoryRecord rd : records) {
			listo = new ArrayList<Object>();

			listo.add(rd.getStartTime());
			listo.add(rd.getLow());
			listo.add(rd.getHigh());
			// listo.add(rd.getOpen());
			// listo.add(rd.getClose());
			listo.add(rd.getLow());
			listo.add(rd.getHigh());
			listos.add(listo);

			sRecord = new SimpleRecord();
			BeanUtils.copyProperties(rd, sRecord);
			simpleRecord.add(sRecord);

		}
//		System.out.println(BiLineUtils.handleBiLine(simpleRecord));
		return listos.toString();

	}

//	@RequestMapping("/getLine")
//	public String getLine(String name) {
//
//		Map<String, Integer> map = new HashMap<String, Integer>();
//		List<HistoryRecord> records = new ArrayList<HistoryRecord>();
//		HistoryRecord record = null;
//		int count = 0;
//
//		/* 读取数据 */
//		try {
//
//			// ：开盘(open)，收盘(close)，最低(lowest)，最高(highest)
//			BufferedReader br = new BufferedReader(
//					new InputStreamReader(new FileInputStream(new File("C:/data.txt")), "UTF-8"));
//			String lineTxt = null;
//			while ((lineTxt = br.readLine()) != null) {
//				String[] names = lineTxt.split(",");
//				record = new HistoryRecord();
//				record.setTime(names[0]);
//				record.setOpen(Double.parseDouble(names[3]));
//				record.setClose(Double.parseDouble(names[4]));
//				// record.setOpen(Double.parseDouble(names[1]));
//				// record.setClose(Double.parseDouble(names[2]));
//				record.setLow(Double.parseDouble(names[3]));
//				record.setHigh(Double.parseDouble(names[4]));
//				record.setStartTime(names[0]);
//				record.setEndTime(names[0]);
//				record.setVolume(100);
//				records.add(record);
//				count++;
//			}
//			br.close();
//		} catch (Exception e) {
//			System.err.println("read errors :" + e);
//		}
//		records = KLineUtils.simpleKLine(records);
//
//		// 打印最高值，最低值
//		List<List<Object>> listos = new ArrayList<List<Object>>();
//		List<SimpleRecord> simpleRecord = new ArrayList<SimpleRecord>();
//		SimpleRecord sRecord = null;
//
//		List<Object> listo = null;
//		for (HistoryRecord rd : records) {
//
//			sRecord = new SimpleRecord();
//			BeanUtils.copyProperties(rd, sRecord);
//			simpleRecord.add(sRecord);
//
//		}
//		List<Point> points = BiLineUtils.contructBiLine(simpleRecord);
//
//		for (Point point : points) {
//
//			listo = new ArrayList<Object>();
//
//			listo.add(point.getTime());
//			listo.add(point.getPrice());
//			listo.add(point.getPrice());
//			listo.add(point.getPrice());
//			listo.add(point.getPrice());
//			listos.add(listo);
//		}
//
//		return listos.toString();
//
//	}
	
	
	@RequestMapping("/getLine")
	public String getLine(String name) {

		Map<String, Integer> map = new HashMap<String, Integer>();
		List<HistoryRecord> records = new ArrayList<HistoryRecord>();
		HistoryRecord record = null;
		int count = 0;
		
		List<MiDataWithBLOBs> list  = service.getList("sz.000776");
		
		for(MiDataWithBLOBs data:list) {
			
			record = new HistoryRecord();
			record.setTime(data.getTime());
			record.setOpen(data.getOpen());
			record.setClose(data.getClose());
			// record.setOpen(Double.parseDouble(names[1]));
			// record.setClose(Double.parseDouble(names[2]));
			record.setLow(data.getLow());
			record.setHigh(data.getHigh());
			record.setStartTime(data.getTime());
			record.setEndTime(data.getTime());
			record.setVolume(data.getVolume());
			records.add(record);
		}

		
		records = KLineUtils.handleKLine(records);

		// 打印最高值，最低值
		List<List<Object>> listos = new ArrayList<List<Object>>();
		List<SimpleRecord> simpleRecord = new ArrayList<SimpleRecord>();
		SimpleRecord sRecord = null;

		List<Object> listo = null;
		for (HistoryRecord rd : records) {

			sRecord = new SimpleRecord();
			BeanUtils.copyProperties(rd, sRecord);
			simpleRecord.add(sRecord);

		}
		
		System.out.println("数据大小 = "+records.size());
		List<Point> points = BiLineUtils.contructBiLine(simpleRecord);

		for (Point point : points) {

			listo = new ArrayList<Object>();

			listo.add(point.getTime());
			listo.add(point.getPrice());
			listo.add(point.getPrice());
			listo.add(point.getPrice());
			listo.add(point.getPrice());
			listos.add(listo);
		}

		return listos.toString();

	}
}
