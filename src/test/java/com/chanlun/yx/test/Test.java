package com.chanlun.yx.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.dto.Point;
import com.chanlun.yx.data.dto.TrendType;
import com.chanlun.yx.data.util.BiLineUtils;
import com.chanlun.yx.data.util.KLineUtils;
import com.chanlun.yx.data.util.LineUtils;
import com.chanlun.yx.data.util.ZhongShuUtils;
import com.chanlun.yx.redis.RedisUtils;

public class Test {

	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException {

		List<String> codes = RedisUtils.getAllKeys();
		int i= 1;
		for (String code : codes) {
			
			List<HistoryRecord> list = RedisUtils.fetchData(code);
//			System.out.println("原始k线" + list.size());

			List<HistoryRecord> list2 = KLineUtils.handleKLine(list);
//			System.out.println("合并后k线" + list2.size());

			List<Point> point = BiLineUtils.contructBiLines(list2);
//			System.out.println("笔数点为" + point.size());

			List<Point> point2 = LineUtils.bi2Line(point);
//			System.out.println("线段点为" + point2.size());
			
		}

	}

//	public static void writeFile(String text) {
//		  try {
//	            File writeName = new File("C:\\output.txt"); // 相对路径，如果没有则要建立一个新的output.txt文件
//	            writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
//	            try (FileWriter writer = new FileWriter(writeName,true);
//	                 BufferedWriter out = new BufferedWriter(writer)
//	            ) {
//	                out.write(text); // \r\n即为换行
//	                out.write("\r\n");
//	                out.flush(); // 把缓存区内容压入文件
//	            }
//	        } catch (IOException e) {
//	            e.printStackTrace();
//	        }
//		
//	}

}
