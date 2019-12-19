package com.chanlun.yx.data.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chanlun.yx.data.dto.BuyDto;
import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.dto.Line;
import com.chanlun.yx.data.dto.Point;
import com.chanlun.yx.data.dto.ZhongShu;
import com.chanlun.yx.data.dto.ZouShi;
import com.chanlun.yx.redis.RedisUtils;

public class TradeUtils {

	static final Logger logger = LoggerFactory.getLogger(TradeUtils.class);

	public static BuyDto buy(String code, String time) throws IllegalAccessException, InvocationTargetException {

		BuyDto buydto = null;

		// 获取数据
		List<HistoryRecord> list = RedisUtils.fetchData(code);
		int index = split(list, time);

		list = list.subList(0, index);

		List<HistoryRecord> tailList = null;

		if (index != list.size()) {

			list.subList(index, list.size());
		}

		List<HistoryRecord> list2 = KLineUtils.handleKLine(list);
		List<Point> point = BiLineUtils.contructBiLines(list2);
		List<Point> point2 = LineUtils.bi2Line(point);

		ZouShi zs = new ZouShi();

		if (zs == null) {
			logger.info("[{}]走势为空", code);
			return null;
		}

		if (zs.getList().size() < 3) {
			logger.info("[{}]走势数目小于3当前走势数目为[{}]", code, zs.getList().size());
			return null;
		}

		if (zs.getList().size() % 2 == 0) {
			logger.info("[{}]三买暂时不考虑", code, zs.getList().size());
			return null;
		}

		ZhongShu zhs = (ZhongShu) zs.getList().get(zs.getList().size() - 1);
		Line lastLine = (Line) zs.getList().get(zs.getList().size() - 2);

		Line line = zhs.getLines().get(zhs.getLines().size() - 1);

		// 最后一段向下，并且背驰
		if (line.getDirect() == 0 && line.getEndPoint().getPrice() < zhs.getDd()) {

			// 跌幅足够
			if ((zhs.getDd() - line.getEndPoint().getPrice()) / zhs.getDd() > 0.3) {

				if (BeiChiUtils.isBeichi(lastLine, line, list)) {
					// 背驰
					double buyPrice = list.get(list.size() - 1).getHigh();
					String buyTime = list.get(list.size() - 1).getTime();

					buydto = new BuyDto();
					buydto.setBuyPrice(buyPrice);
					buydto.setBuyTime(buyTime);
					buydto.setHistorys(list);
					buydto.setZouShi(zs);
					buydto.setTailRecord(tailList);
				}
			}

		}
		return buydto;
	}

	private static int split(List<HistoryRecord> list, String time) {

		List<HistoryRecord> prelist = new ArrayList<HistoryRecord>();
		int index = list.size();
		for (int i = 0; i < list.size(); i++) {

			if (list.get(i).getStartTime().equals(time)) {

				index = i;
			}
		}

		return index;
	}

	public static BuyDto sale(BuyDto buydto, HistoryRecord record)
			throws IllegalAccessException, InvocationTargetException {

		List<HistoryRecord> list = buydto.getHistorys();
		list.add(record);
		List<HistoryRecord> list2 = KLineUtils.handleKLine(list);
		List<Point> point = BiLineUtils.contructBiLines(list2);
		List<Point> point2 = LineUtils.bi2Line(point);

		ZouShi newzs = new ZouShi();
		ZouShi oldzs = buydto.getZouShi();
		ZhongShu zhongshu = (ZhongShu) newzs.getList().get(newzs.getList().size() - 1);
		if (newzs.getNum() - oldzs.getNum() < 2) {

			// 没有多出两段 需要继续
			buydto.setHistorys(list);
		}

		if (newzs.getNum() - oldzs.getNum() == 2) {// 两段

			double price = point2.get(point2.size() - 2).getPrice();
			double currentPrice = point2.get(point2.size() - 1).getPrice();

			if (price <= zhongshu.getGg()) {

				// 走势不够强 当前价格卖出
				double salePrice = (record.getHigh() + record.getLow()) / 2;

			} else if (currentPrice <= zhongshu.getGg()) {

				double salePrice = (record.getHigh() + record.getLow()) / 2;

			} else {
				// 强力走势，还可以继续考察
				buydto.setHistorys(list);
			}

		}

		if (newzs.getNum() - oldzs.getNum() == 3) {// 两段

			double price = point2.get(point2.size() - 2).getPrice();

			if (price <= zhongshu.getGg()) {

				// 走势不够强 当前价格卖出
				double salePrice = (record.getHigh() + record.getLow()) / 2;

			} else {
				// 强力走势，还可以继续考察
				buydto.setHistorys(list);
			}

		}

		if (newzs.getNum() - oldzs.getNum() == 4) {// 多段
			if (point2.get(point2.size() - 3).getPrice() > point2.get(point2.size() - 1).getPrice()) {

				double salePrice = (record.getHigh() + record.getLow()) / 2;

			} else {

				buydto.setHistorys(list);
			}
		}

		if (newzs.getNum() - oldzs.getNum() > 4) {// 多段
			// 此时需要等中枢形成
			if (newzs.getZhsNum() == oldzs.getZhsNum() + 1) {
				// 出现
				if (point2.get(point2.size() - 1).getPrice() - point2.get(point2.size() - 2).getPrice() > 0) {
					// 最后一段上升
					
					if (record.getHigh() > zhongshu.getGg()) {
						
						//在相应的离开幅度后是否出现背驰
						if((record.getHigh()  - zhongshu.getGg())/record.getHigh()>0.03){
							
							if(BeiChiUtils.isBeichi(lastLine, lastLine2, lists)){
								//背驰卖出
							}else{
								buydto.setHistorys(list);
							}
						}
					}else{
						buydto.setHistorys(list);
					}
				} else {
					// 最后一段下跌 如果破中枢低点 那么就 卖出
					if (record.getLow() < zhongshu.getDd()) {
						double salePrice = (record.getHigh() + record.getLow()) / 2;
					} else {
						buydto.setHistorys(list);
					}
				}
			} else {
				// 没有出现新中枢 ，继续等待
				buydto.setHistorys(list);
			}
		}
		return buydto;
	}

	public static void trail() throws IllegalAccessException, InvocationTargetException {
		BuyDto buyDto = buy("java.sh.000006", "2019012120012450");
		if (buyDto == null) {
			return;
		}

		if (buyDto.getTailRecord() == null) {

			// 实际方式

		} else {
			// 模拟方式
			moniHandler1(buyDto);

		}

	}

	private static void moniHandler1(BuyDto buyDto) throws IllegalAccessException, InvocationTargetException {

		List<HistoryRecord> tails = buyDto.getTailRecord();
		for (HistoryRecord record : tails) {
			sale(buyDto, record);
		}

	}

}
