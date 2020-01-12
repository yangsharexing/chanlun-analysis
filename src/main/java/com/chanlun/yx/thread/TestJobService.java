package com.chanlun.yx.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import com.chanlun.yx.data.dto.HLDto;
import com.chanlun.yx.data.util.ExportAbstractUtil;
import com.chanlun.yx.redis.RedisUtils;

public class TestJobService {

	public static void main(String[] args) throws InterruptedException, ExecutionException {

		List<String> codes = RedisUtils.getAllKeys();
		
//		codes = codes.subList(0, 800);
		List<HLDto> hlList = new ArrayList<HLDto>();
		int maxDay = 60;
		int step = 400;
		int num = codes.size() / step;

		List<Future<Object>> flist = new ArrayList<Future<Object>>();
		ExecutorService pool = Executors.newCachedThreadPool();
		for (int i = 0; i < num; i++) {

			List<String> scode = codes.subList(i * step, (i + 1) * step);
			TestJob job = new TestJob();
			job.setCodes(scode);
			job.setHlList(hlList);
			job.setMaxDay(maxDay);
			Future<Object> future = pool.submit(job);
			flist.add(future);
		}

		if (codes.size() % step > 0) {

			List<String> scode = codes.subList(num * 400, codes.size());
			TestJob job = new TestJob();
			job.setCodes(scode);
			job.setHlList(hlList);
			job.setMaxDay(maxDay);
			Future<Object> future = pool.submit(job);
			flist.add(future);
		}
		boolean flag = true;
		while (flag) {
			int tip = 0;
			for (Future<Object> future : flist) {

				if (!future.isDone()) {
					tip = 1;
					break;
				}
			}
			if(tip ==1) {
				continue;
			}
			flag = false;
		}
		
		for (Future<Object> future : flist) {
			
			System.out.println(future.get());
			System.out.println(future.isDone());

		}
		
		ExportAbstractUtil.excelOutPut(hlList, "100 .xls");
	}

}
