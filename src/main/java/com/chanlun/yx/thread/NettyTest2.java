package com.chanlun.yx.thread;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class NettyTest2 {

	public static void main(String[] args) {

		//实现一个Callable接口
		Callable<Netty> c = new Callable<NettyTest2.Netty>() {
			@Override
			public Netty call() throws Exception {
				//这里是你的业务逻辑处理
				//让当前线程阻塞5秒看下效果
				Thread.sleep(5000);
				return new Netty("张三");
			}
		};
		ExecutorService es = Executors.newFixedThreadPool(2);
		
		//记得要用submit，执行Callable对象
		Future<Netty> fn = es.submit(c);
		
		//无限循环等待任务处理完毕  如果已经处理完毕 isDone返回true
		while (!fn.isDone()) {
			try {
				//处理完毕后返回的结果
				Netty nt = fn.get();
				System.out.println(nt.name);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	static class Netty {
		private Netty(String name) {
			this.name = name;
		}

		private String name;
	}
}


