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

		List<HistoryRecord> list = new ArrayList<HistoryRecord>(dataList.size());
		for (int i = 0; i < dataList.size(); i++) {
			data = new HistoryRecord();
			if(timeList == null || timeList.size()==0){
				data.setTime(dataList.getString(i));
			}else{
				data.setTime(timeList.getString(i));
			}
			
			data.setStartTime(timeList.getString(i));
			data.setEndTime(timeList.getString(i));
			data.setCode(codeList.getString(i));
			data.setOpen(openList.getDouble(i));
			data.setHigh(highList.getDouble(i));
			data.setLow(lowList.getDouble(i));
			data.setClose(closeList.getDouble(i));
			data.setVolume(volumeList.getDouble(i));
			list.add(data);
		}
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


}
