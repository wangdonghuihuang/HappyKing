package com.king.service;

import java.util.List;

import com.king.model.User;

public interface UserService {

    int addUser(User user);

    List<User> findAllUser(int pageNum, int pageSize);
}