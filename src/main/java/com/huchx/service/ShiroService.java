package com.huchx.service;

import com.huchx.entity.MUserEntity;
import com.huchx.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShiroService {

    @Autowired
    UserMapper userMapper;

    public MUserEntity findUserById(Integer id){
        return userMapper.selectByPrimaryKey(id);
    }
}
