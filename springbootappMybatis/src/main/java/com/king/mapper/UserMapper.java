package com.king.mapper;

import java.util.List;

import com.king.model.User;

public interface UserMapper {
    int deleteByPrimaryKey(Integer userId);

    int insert(User record);
    int insertone(User user);
    int insertSelective(User record);

    User selectByPrimaryKey(Integer userId);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);
  //这个方式我自己加的
    List<User> selectAllUser();
}