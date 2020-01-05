package com.chanlun.yx.data.dao;

import java.util.List;

import com.chanlun.yx.data.dto.MonitorQueryDto;
import com.chanlun.yx.data.model.Monitor;

public interface MonitorMapper {
    int insert(Monitor record);

    int insertSelective(Monitor record);

	int countTotal(MonitorQueryDto queryDto);

	List<Monitor> selectMonitor(MonitorQueryDto queryDto);
}