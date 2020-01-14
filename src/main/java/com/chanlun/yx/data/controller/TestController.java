package com.chanlun.yx.data.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chanlun.yx.data.dto.HLDto;
import com.chanlun.yx.data.dto.MonitorQueryDto;
import com.chanlun.yx.data.util.ExportAbstractUtil;
import com.chanlun.yx.redis.RedisUtils;
import com.chanlun.yx.thread.TestJob;

@RestController
public class TestController {
	
	
	@RequestMapping("/test")
	public void test() throws InterruptedException, ExecutionException {
   
		test1();
	}
	
	public void test1() throws InterruptedException, ExecutionException {
	List<String> codes = RedisUtils.getAllKeys();
		
		codes = codes.subList(800, 1600);
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
		
		ExportAbstractUtil.excelOutPut(hlList, "101.xls");
	}
}
