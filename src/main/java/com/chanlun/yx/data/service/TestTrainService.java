package com.chanlun.yx.data.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.dto.Line;
import com.chanlun.yx.data.dto.Point;
import com.chanlun.yx.data.dto.TrendType;
import com.chanlun.yx.data.dto.ZhongShu;
import com.chanlun.yx.data.util.BeiChiUtils;
import com.chanlun.yx.data.util.BiLineUtils;
import com.chanlun.yx.data.util.ChanlunUtils;
import com.chanlun.yx.data.util.KLineUtils;
import com.chanlun.yx.data.util.LineUtils;
import com.chanlun.yx.data.util.ZhongShuUtils;

/**
 * 由于数据，最后一笔，一段 都不是完成的，所以测试的时候，数据不能以未来的数据做走势划分。 1、按正常交易的流程测试
 * 
 * @author Administrator
 *
 */
public class TestTrainService {

	private String TIME_FORMAT = "14:30";

	public void testTrain(String code) throws IllegalAccessException, InvocationTargetException {

		// 下载
		List<HistoryRecord> lists = downLoadHistory(code);

		// 按时间截取数据
		List<HistoryRecord> preLists = interceptBefore(lists, TIME_FORMAT);
		List<HistoryRecord> afterLists = interceptAfter(lists, TIME_FORMAT);

		// step 1 :simple K line
		List<HistoryRecord> preSimpleKLines = KLineUtils.handleKLine(preLists);

		// step 2 :Kline to Bi
		List<Point> preBiPoints = BiLineUtils.contructBiLines(preSimpleKLines);

		// step 3: Bi to Line
		List<Point> preLinePoints = LineUtils.bi2Line(preBiPoints);

		// step 4 : line to trender
		List<TrendType> preTrends = ZhongShuUtils.findZhongShu(preLinePoints);

		if (preTrends.size() < 5) {

			return;// 数据不够
		}

		if (preTrends.size() % 2 == 0) {// 双数 说明最后一个原素是Line

			Line lastLine = (Line) preTrends.get(preTrends.size() - 1);
			if (lastLine.getDirect() == 0) {
				// 最后一段位向下离开中枢

				Line lastLine2 = (Line) preTrends.get(preTrends.size() - 3);

				if (lastLine2.getDirect() == 0) {
					// 确认，中枢-->下-->中枢-->下走势 寻找一买
					// 接下来需要判断 两段的背驰
					// 暂定背驰比较系数为 ：成交量*价格变化
					if (BeiChiUtils.isBeichi(lastLine, lastLine2, lists)) {

						// 确认买点时间和价格
						double buyPrice = lastLine.getEndPoint().getPrice();
						String buyTime = lastLine.getEndPoint().getTime();

						// 后面开始模拟走势
						// 1、背驰后在底部形成新的中枢，此时立马退出
						// 2、背驰后上升后形成中枢不高于上一个中枢退出
						// 3、背驰后上升后形成高于上一个中枢的中枢，且中枢延续超过9段 或者中枢升级，也退出
						// 3、背驰后上升后形成高于上一个中枢的中枢，中枢形成后破中枢低点，也退出
						// 5、背驰后上升后形成高于上一个中枢的中枢，之后向上以笔离开中枢产生背驰也退出

						// 下载
						// List<HistoryRecord> lists = downLoadHistory(code);
						ZhongShu preZhongShu = (ZhongShu) preTrends.get(preTrends.size() - 2);
						for (int i = 0; i < afterLists.size() - 1; i = i + 2) {
							preLists.add(afterLists.get(i));
							preLists.add(afterLists.get(i + 1));

							// 前一个中枢开始时间
							

							List<TrendType> trends = ChanlunUtils.execute(preLists);
							// 第一种情况，前一个中枢升级，表示进入盘整
							
							if(trends.size()%2 ==1){
								//以中枢结尾，只有两种可能，一种是原来最后一个中枢升级，另外就是 生成新的中枢
								ZhongShu newZhongShu = (ZhongShu) trends.get(trends.size()-1);
								
								if(newZhongShu.getStartTime().equals(preZhongShu.getStartTime())){
									
									if(newZhongShu.getLevel() == preZhongShu.getLevel()){
										//原来的走势延续，下跌走势还在延续
									}else{
										//原来的中枢升级，
										
									}
									
								}
								
								
							}
							
						}

					}
				}
			}

		} else {
			// 最后一个元素为中枢，那么考察最后一个线段是否为向下 的线段，如果向下，且超过了中枢区间， 也可以考虑。。。。
//			ZhongShu preZhongShu = (ZhongShu) preTrends.get(preTrends.size() - 1);
//			Point point = preLinePoints.get(preLinePoints.size()-1);
//			
//			if(point.getPrice()<preZhongShu.getDd()){
//				
//				//考虑是否背驰
//				
//				
//				
//				
//			}
			
			
			

		}

	}

	private List<HistoryRecord> interceptBefore(List<HistoryRecord> lists, String tIME_FORMAT2) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<HistoryRecord> interceptAfter(List<HistoryRecord> lists, String code) {
		// TODO Auto-generated method stub
		return null;
	}

	// 按时间点截取数据
	private List<HistoryRecord> intercept(List<HistoryRecord> lists, String code) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<HistoryRecord> downLoadHistory(String code) {
		// TODO Auto-generated method stub
		return null;
	}
}
