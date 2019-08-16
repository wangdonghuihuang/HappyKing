package com.king.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.king.model.People;

@Mapper
public interface UserMapperone {
	void addUser(People sysUser);
	 
    int updateUserByName(People sysUser);
 
    int selectByName(String name);
    List<People> selectall();
}
