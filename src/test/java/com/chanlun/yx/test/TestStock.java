package com.chanlun.yx.test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.chanlun.yx.data.dto.HLDto;
import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.util.ExportAbstractUtil;
import com.chanlun.yx.data.util.StockTest2;
import com.chanlun.yx.redis.RedisUtils;

public class TestStock {

	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {

		List<String> codes = RedisUtils.getAllKeys();
		List<HLDto> hlList = new ArrayList<HLDto>();
		int maxDay = 60;
		
		int cc = 0;
		int size = codes.size();
		for (String code : codes) {
			cc++;
			System.out.println("进度  -------------- "+ExportAbstractUtil.formatNum((double)cc/size));
			
			if (code.contains("day")) {
				continue;
			}
//			System.out.println("-----------------------"+code);
			List<HistoryRecord> list = RedisUtils.fetchData(code);
			int index = 1000;
			while (true) {
				index = StockTest2.test(code, list, index,hlList);
				if (index == 0) {
					break;
				}
			}
		}
		ExportAbstractUtil.excelOutPut(hlList,"62.xls");
	}
}
