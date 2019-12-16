package com.chanlun.yx.data.util;

import java.util.List;

import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.dto.Point;
import com.chanlun.yx.data.dto.TrendType;

/**
 * 缠论走势工具
 * 
 * @author Administrator
 *
 */
public class ChanlunUtils {

	public static List<TrendType> execute(List<HistoryRecord> list) {

		List<TrendType> trendTypes = null;

		try {
			// step 1 :simple K line
			List<HistoryRecord> simpleKLines = KLineUtils.handleKLine(list);

			// step 2 :Kline to Bi
			List<Point> biPoints = BiLineUtils.contructBiLines(simpleKLines);

			// step 3: Bi to Line
			List<Point> linePoints = LineUtils.bi2Line(biPoints);

			// step 4 : line to trender
			trendTypes = ZhongShuUtils.findZhongShu(linePoints);

		} catch (Exception e) {

		}
		return trendTypes;
	}

}
