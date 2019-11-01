package com.chanlun.yx.data.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chanlun.yx.data.dao.MiDataMapper;
import com.chanlun.yx.data.model.MiDataWithBLOBs;

@Service
public class MiDataService {

	@Autowired
	private MiDataMapper miDataMapper;

	public List<MiDataWithBLOBs> getList(String code) {

		return miDataMapper.queryAllByCode(code);
	}
}
