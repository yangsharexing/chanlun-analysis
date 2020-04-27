package com;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.redis.RedisUtils;

public class Test2 {
	public static void main(String[] args) {

		List<String> codes = RedisUtils.getAllKeys();
		int index = 0;
		double huoli = 0.0;
		double zongshu = 0.0;
		Map<String, Double> codeMaps = new HashMap<String, Double>();
		for (String code : codes) {

//			System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA  "+index);
			index++;
			List<HistoryRecord> list = null;
			try {
				list = RedisUtils.fetchData(code);
				if(list.size()<30) {
					continue;
				}else {
					list = list.subList(list.size()-30, list.size());
				}
			} catch (Exception e) {
				continue;
			}

			// System.out.println("原始k线" + list.size());
			int i = 1;
			double range = 0;
			while (i < list.size()) {

				range = range + Math.abs(list.get(i).getHigh() - list.get(i).getLow());
				 i = i+1;
			}
			codeMaps.put(code, range);
		}

		List<Map.Entry<String, Double>> list = new ArrayList<>(codeMaps.entrySet());
		Collections.sort(list, new Comparator<Map.Entry<String, Double>>() {
			@Override
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				// 按照value值，重小到大排序
//	                return o1.getValue() - o2.getValue();

				// 按照value值，从大到小排序
//	                return o2.getValue() - o1.getValue();

				// 按照value值，用compareTo()方法默认是从小到大排序
				return o1.getValue().compareTo(o2.getValue());
			}
		});
		
		
		//注意这里遍历的是list，也就是我们将map.Entry放进了list，排序后的集合
        for (Map.Entry s : list)
        {
            System.out.println(s.getKey()+"--"+s.getValue());
        }

	}

}
