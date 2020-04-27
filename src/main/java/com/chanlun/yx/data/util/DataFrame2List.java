package com.chanlun.yx.data.util;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chanlun.yx.data.dto.AllHistoryRecord;
import com.chanlun.yx.data.dto.HistoryRecord;

public class DataFrame2List {

	public static List<HistoryRecord> json2List(String dataFrameStr) {

		JSONObject obj = JSONObject.parseObject(dataFrameStr);

		HistoryRecord data = null;
		JSONArray dataList = obj.getJSONArray("date");
		JSONArray timeList = obj.getJSONArray("time");
		JSONArray codeList = obj.getJSONArray("code");
		JSONArray openList = obj.getJSONArray("open");
		JSONArray highList = obj.getJSONArray("high");
		JSONArray lowList = obj.getJSONArray("low");
		JSONArray closeList = obj.getJSONArray("close");
		JSONArray volumeList = obj.getJSONArray("volume");
		JSONArray trunList = obj.getJSONArray("turn");
		JSONArray pctChgList = obj.getJSONArray("pctChg");

		List<HistoryRecord> list = new ArrayList<HistoryRecord>(dataList.size());
		for (int i = 0; i < dataList.size(); i++) {

			if (volumeList == null || volumeList.getDouble(i) == null) {
				System.out.println();
			}
			if (volumeList.getDouble(i) == null || volumeList.getDouble(i) == 0) {
				continue;
			}

			data = new HistoryRecord();
			if (timeList == null || timeList.size() == 0) {
				data.setTime(dataList.getString(i));
				data.setStartTime(dataList.getString(i));
				data.setEndTime(dataList.getString(i));
			} else {
				data.setTime(timeList.getString(i));
				data.setStartTime(timeList.getString(i));
				data.setEndTime(timeList.getString(i));
			}

			data.setCode(codeList.getString(i));
			data.setOpen(openList.getDouble(i));
			data.setHigh(highList.getDouble(i));
			data.setLow(lowList.getDouble(i));
			data.setClose(closeList.getDouble(i));
			data.setVolume(volumeList.getDouble(i));

			if (trunList != null && trunList.size() > 0) {
				try {
					data.setTurn(trunList.getDouble(i));
				} catch (Exception e) {
					System.out.println(trunList.get(i));
					e.printStackTrace();
				}

			}

			list.add(data);
		}
		handle(list);
		return list;
	}

	public static List<AllHistoryRecord> json2DayList(String dataFrameStr) {

		JSONObject obj = JSONObject.parseObject(dataFrameStr);

		AllHistoryRecord data = null;

		JSONArray dataList = obj.getJSONArray("date");
		JSONArray timeList = obj.getJSONArray("time");
		JSONArray codeList = obj.getJSONArray("code");
		JSONArray openList = obj.getJSONArray("open");
		JSONArray highList = obj.getJSONArray("high");
		JSONArray lowList = obj.getJSONArray("low");
		JSONArray closeList = obj.getJSONArray("close");
		JSONArray volumeList = obj.getJSONArray("volume");
		JSONArray trunList = obj.getJSONArray("turn");
		JSONArray pcChgList = obj.getJSONArray("pctChg");

		List<AllHistoryRecord> list = new ArrayList<AllHistoryRecord>(dataList.size());
		for (int i = 0; i < dataList.size(); i++) {
			data = new AllHistoryRecord();
			data.setTime(dataList.getString(i));
			data.setStartTime(dataList.getString(i));
			data.setEndTime(dataList.getString(i));
			data.setCode(codeList.getString(i));
			data.setOpen(openList.getDouble(i));
			data.setHigh(highList.getDouble(i));
			data.setLow(lowList.getDouble(i));
			data.setClose(closeList.getDouble(i));
			data.setVolume(volumeList.getDouble(i));
			data.setTurn(trunList.getDouble(i));
			data.setPctChg(pcChgList.getDouble(i));
			list.add(data);
		}
		return list;
	}
	
	private static void handle(List<HistoryRecord> list) {
		// TODO Auto-generated method stub
		for (HistoryRecord record : list) {
			record.setAbs_length(Math.abs(record.getOpen() - record.getClose()));
		}

		for (HistoryRecord record : list) {

			if (record.getOpen() > record.getClose()) {

				record.setUpLineRate((record.getHigh() - record.getOpen()) / record.getOpen());
				record.setDownLineRate((record.getClose() - record.getLow()) / record.getOpen());
			} else {

				record.setUpLineRate((record.getHigh() - record.getClose()) / record.getOpen());
				record.setDownLineRate((record.getOpen() - record.getLow()) / record.getOpen());
			}
		}

		for (int i = 1; i < list.size(); i++) {
			list.get(i).setPriceCh(((list.get(i).getClose() - list.get(i - 1).getClose()) / list.get(i - 1).getClose())*100);
			
			if(list.get(i).getTurn() !=0) {
				
				
				list.get(i).setLiDu(list.get(i).getPriceCh()/list.get(i).getTurn());
				
				
				HistoryRecord record = list.get(i);
				
//				if(!(Math.abs(record.getPriceCh())>9 ||( Math.abs(record.getPriceCh())>4.5 && Math.abs(record.getPriceCh())<5.5) )){
//					
//					if(list.get(i).getLiDu()>20 ) {
//						System.out.println(list.get(i).getTime());
//						System.out.println(list.get(i).getPriceCh());
//						System.out.println(list.get(i).getTurn());
//						System.out.println(list.get(i).getCode());
//						System.out.println("--------------------");
//					}
//				}
				
			}
			
		}

	}

}
