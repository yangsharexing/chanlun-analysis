package com.chanlun.yx.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.chanlun.yx.data.dao.MonitorMapper;
import com.chanlun.yx.data.dto.MonitorQueryDto;
import com.chanlun.yx.data.dto.resp.MinotorResponse;
import com.chanlun.yx.data.model.Monitor;

@Service
public class MonitorService {

	@Autowired
	private MonitorMapper monitorMapper;

	public String queryMonitor(MonitorQueryDto queryDto) {
		MinotorResponse res = new MinotorResponse();

		// 计算总数
		int total = monitorMapper.countTotal(queryDto);
		
		
		queryDto.setStart(queryDto.getLimit()*(queryDto.getPage()-1));
		queryDto.setEnd(queryDto.getLimit()*(queryDto.getPage()-1)+queryDto.getLimit());
		List<Monitor> monitorList = monitorMapper.selectMonitor(queryDto);

		res.setCount(total);
		res.setData(monitorList);
		return JSON.toJSONString(res);
	}

}
