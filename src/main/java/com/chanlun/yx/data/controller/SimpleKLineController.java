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

import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.dto.Point;
import com.chanlun.yx.data.dto.SimpleRecord;
import com.chanlun.yx.data.model.MiDataWithBLOBs;
import com.chanlun.yx.data.service.MiDataService;
import com.chanlun.yx.data.util.BiLineUtils;
import com.chanlun.yx.data.util.KLineUtils;
import com.chanlun.yx.data.util.LineUtils;
import com.chanlun.yx.redis.RedisUtils;

@RestController
public class SimpleKLineController {

	@Autowired
	private MiDataService service;

	@RequestMapping("/getOrgData")
	public String getLine(String code) throws IllegalAccessException, InvocationTargetException {

		List<HistoryRecord> list = RedisUtils.fetchData(code);
		// System.out.println("原始k线" + list.size());

		List<HistoryRecord> list2 = KLineUtils.handleKLine(list);
		// System.out.println("合并后k线" + list2.size());

		List<Point> point = BiLineUtils.contructBiLines(list2);
		// System.out.println("笔数点为" + point.size());

		List<Point> point2 = LineUtils.bi2Line(point);
		// System.out.println("线段点为" + point2.size());

		return point2.toString();

	}
}
