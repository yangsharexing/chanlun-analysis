package com.chanlun.yx.redis;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.chanlun.yx.data.dto.AllHistoryRecord;
import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.util.DataFrame2List;

import redis.clients.jedis.Jedis;

public class RedisUtils {

	private static Jedis jedis = null;

	public static void init() {
		if (jedis == null) {
			jedis = new Jedis("127.0.0.1", 6379);
			// jedis = new Jedis("123.207.108.89", 6379);
		}
	}

	public static List<HistoryRecord> fetchData(String code) {
		init();
		String dataFrame = jedis.get(code);
		return DataFrame2List.json2List(dataFrame);
	}
	
	public static List<AllHistoryRecord> fetchDayData(String code) {
		init();
		String dataFrame = jedis.get(code);
		return DataFrame2List.json2DayList(dataFrame);
	}

	public static List<String> getAllKeys() {
		init();
		Set<String> keys = jedis.keys("java*");
		return new ArrayList<String>(keys);
	}

	public static void main(String[] args) {
		init();
		List<String> keys = getAllKeys();
		int i = 0;
		for (String key : keys) {
			i++;
			String dataFrame = jedis.get(key);
			writeFile(dataFrame);
			if (i > 0) {
				break;
			}
		}
//		fetchDataFromFile();
	}

	public static List<HistoryRecord> fetchDataFromFile() {
		List<HistoryRecord> list =  getFile();
		for(HistoryRecord record:list){
			System.out.println(record);
		}
		return list;
		
	}

	public static void writeFile(String text) {
		
		text = text.replaceAll("\r|\n", "");
		try {
			File writeName = new File("C:\\dataFrame.txt"); // 相对路径，如果没有则要建立一个新的output.txt文件
			writeName.createNewFile(); // 创建新文件,有同名的文件的话直接覆盖
			try (FileWriter writer = new FileWriter(writeName, true); BufferedWriter out = new BufferedWriter(writer)) {
				out.write(text); // \r\n即为换行
				out.write("\r\n");
				out.flush(); // 把缓存区内容压入文件
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static List<HistoryRecord> getFile() {
		/* 读取数据 */
		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new FileInputStream(new File("C:\\dataFrame.txt")), "UTF-8"));
			String lineTxt = null;
			while ((lineTxt = br.readLine()) != null) {
				return DataFrame2List.json2List(lineTxt);
			}
			br.close();
		} catch (Exception e) {
			System.err.println("read errors :" + e);
		}
		return null;

	}
}
