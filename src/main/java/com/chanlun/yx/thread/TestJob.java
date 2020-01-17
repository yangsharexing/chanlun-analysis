package com.chanlun.yx.thread;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.chanlun.yx.data.dto.HLDto;
import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.util.ExportAbstractUtil;
import com.chanlun.yx.data.util.StockTest2;
import com.chanlun.yx.redis.RedisUtils;
import java.util.concurrent.Callable;

public class TestJob implements Callable<Object> {

	private List<String> codes;
	private List<HLDto> hlList;
	private int maxDay;

	@Override
	public Object call() throws Exception {
		int i = 0;
		for (String code : codes) {
			i++;
			System.out.println(Thread.currentThread().getName()+"开始了第： "+i+"   总共："+codes.size());
			
//			System.out.println("-----------------------"+code);
			List<HistoryRecord> list = RedisUtils.fetchData(code);
			int index = 50;
			while (true) {
				index = StockTest2.test(code, list, index, hlList);
				if (index == 0) {
					break;
				}
			}
		}
		System.out.println("finish");
		return 1;
	}

	public List<String> getCodes() {
		return codes;
	}

	public void setCodes(List<String> codes) {
		this.codes = codes;
	}

	public List<HLDto> getHlList() {
		return hlList;
	}

	public void setHlList(List<HLDto> hlList) {
		this.hlList = hlList;
	}

	public int getMaxDay() {
		return maxDay;
	}

	public void setMaxDay(int maxDay) {
		this.maxDay = maxDay;
	}
}
