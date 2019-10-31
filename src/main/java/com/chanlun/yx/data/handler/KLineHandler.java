package com.chanlun.yx.data.handler;

import java.util.List;

import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.util.KLineUtils;

public class KLineHandler {

	public  static List<HistoryRecord> handlerSimpleKLine(List<HistoryRecord> list) {

		return KLineUtils.simpleKLine(list);
	}
}
