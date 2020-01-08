package com.chanlun.yx.test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.chanlun.yx.data.dto.HL;
import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.util.StockTest;
import com.chanlun.yx.redis.RedisUtils;

public class TestStock {

	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {

		List<String> codes = RedisUtils.getAllKeys();
		HL hl = new HL();
		int maxDay = 14;
		for (String code : codes) {

			if (code.contains("day")) {
				continue;
			}
			
//			System.out.println("-----------------------"+code);
			List<HistoryRecord> list = RedisUtils.fetchData(code);
			int index = 1000;
			while (true) {
				index = StockTest.test(code, list, index,hl,maxDay);
				if (index == 0) {
					break;
				}
			}
		}
		System.out.println(hl.toString());
	}
}
