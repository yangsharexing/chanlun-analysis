package com.chanlun.yx.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.util.DataFrame2List;

import redis.clients.jedis.Jedis;

public class RedisUtils {

	private static Jedis jedis = null;

	public static void init() {
		if (jedis == null) {
			jedis = new Jedis("127.0.0.1", 6379);
//			jedis = new Jedis("123.207.108.89", 6379);
		}
	}

	public static List<HistoryRecord> fetchData(String code) {
		init();
		String dataFrame = jedis.get(code);
		return DataFrame2List.json2List(dataFrame);
	}

	public static List<String> getAllKeys() {
		init();
		Set<String> keys = jedis.keys("java*");
		return new ArrayList<String>(keys);
	}
}
