package com.chanlun.yx.data.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chanlun.yx.data.dto.MonitorQueryDto;
import com.chanlun.yx.data.dto.resp.MinotorResponse;
import com.chanlun.yx.data.service.MonitorService;

@RestController
public class TradeController {

	@Autowired
	private MonitorService monitorService;

	@RequestMapping("/queryMonitorData")
	public String queryMonitorData(MonitorQueryDto queryDto) {

		return monitorService.queryMonitor(queryDto);
	}
}
