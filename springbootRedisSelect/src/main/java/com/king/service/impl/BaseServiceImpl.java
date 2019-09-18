package com.king.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.king.mapper.base.cityMapper;
import com.king.service.BaseService;
@Service
public class BaseServiceImpl implements BaseService{
@Autowired
private cityMapper city;
	@Override
	public int queryCountService() {
		int num=city.queryCount();
		return num;
	}

}
