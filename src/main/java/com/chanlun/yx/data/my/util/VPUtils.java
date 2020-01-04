package com.chanlun.yx.data.my.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import com.chanlun.yx.data.dto.AllHistoryRecord;
import com.chanlun.yx.data.dto.HistoryRecord;
import com.chanlun.yx.data.dto.VpYDto;

public class VPUtils {

	/**
	 * 成交量*价格变化
	 * 
	 * @param list
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 */
	public static List<VpYDto> vp(List<AllHistoryRecord> list)
			throws IllegalAccessException, InvocationTargetException {

		List<VpYDto> vlist = new ArrayList<VpYDto>();
		VpYDto dto = null;
		for (AllHistoryRecord record : list) {

			dto = new VpYDto();
			dto.setTime(record.getEndTime());
			dto.setY(record.getTurn() * record.getPctChg());
			vlist.add(dto);
		}
		return vlist;
	}

	/**
	 * 价格/换手
	 * 
	 * @param list
	 * @return
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static List<VpYDto> pc(List<AllHistoryRecord> list)
			throws IllegalAccessException, InvocationTargetException {

		List<VpYDto> vlist = new ArrayList<VpYDto>();
		VpYDto dto = null;
		for (AllHistoryRecord record : list) {

			dto = new VpYDto();
			dto.setTime(record.getEndTime());
			dto.setY(record.getPctChg() / record.getTurn());
			vlist.add(dto);
		}
		return vlist;
	}

}
