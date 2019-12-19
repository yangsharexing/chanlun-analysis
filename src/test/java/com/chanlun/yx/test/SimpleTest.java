package com.chanlun.yx.test;

import java.util.List;

import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.dto.Point;
import com.chanlun.yx.data.util.BiLineUtils;
import com.chanlun.yx.data.util.KLineUtils;
import com.chanlun.yx.data.util.LineUtils;
import com.chanlun.yx.redis.RedisUtils;

/**
 * 缩量下跌就是买入机会 test
 * 
 * @author Administrator
 *
 */
public class SimpleTest {

	public static void main(String[] args) {
		List<String> codes = RedisUtils.getAllKeys();
		for (String code : codes) {

			List<HistoryRecord> list = RedisUtils.fetchData(code);
			
			//半小时 5分钟有6根
			for(int i =0;i<list.size()-7;i++){
				
				if(list.get(i+5).getHigh()<list.get(i).getHigh()){
					
					double range = (list.get(i).getHigh() -list.get(i+5).getHigh())/(list.get(i).getHigh());
					if(range>0.03){
						
						
					}
					
					
				}
				
				
			}

		}

	}

}
