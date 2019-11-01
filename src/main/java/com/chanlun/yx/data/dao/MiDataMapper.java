package com.chanlun.yx.data.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.chanlun.yx.data.model.MiDataWithBLOBs;

public interface MiDataMapper {
    int insert(MiDataWithBLOBs record);

    int insertSelective(MiDataWithBLOBs record);

	List<MiDataWithBLOBs> queryAllByCode(@Param("code")String code);
}