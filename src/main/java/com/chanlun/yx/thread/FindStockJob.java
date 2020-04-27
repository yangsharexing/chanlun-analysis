package com.chanlun.yx.thread;

import java.util.List;
import java.util.concurrent.Callable;

import com.chanlun.yx.data.dto.HLDto;
import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.util.FindStockTest;
import com.chanlun.yx.data.util.StockTest2;
import com.chanlun.yx.redis.RedisUtils;

public class FindStockJob implements Callable<Object> {

	private List<String> codes;
	private List<HLDto> hlList;
	private int maxDay;

	@Override
	public Object call() throws Exception {
		for (String code : codes) {
//			System.out.println("-----------------------"+code);
			List<HistoryRecord> list = RedisUtils.fetchData(code);
			if (list == null) {
				continue;
			}
			int index = 50;
			FindStockTest.test(code, list, index, hlList);
		}
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
