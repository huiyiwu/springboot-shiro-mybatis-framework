package com.huchx.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huchx.entity.MUserEntity;
import com.huchx.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
    public MUserEntity findUserById(String id){
        return  userMapper.selectByPrimaryKey(Integer.valueOf(id));
    }

    public PageInfo<MUserEntity> findUserByPage() {
        PageHelper.startPage(0,10);
        PageInfo<MUserEntity> pageInfo = new PageInfo<>(userMapper.findAll());
        return pageInfo;
    }

    public int InsertUserByName(MUserEntity mUserEntity) {
        return userMapper.insert(mUserEntity);
    }
}
