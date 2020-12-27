package com.ay.service.impl;

import com.ay.dao.AyUserDao;
import com.ay.model.AyUser;
import com.ay.service.AyUserService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class AyUserServiceImpl implements AyUserService {

    @Resource
    private AyUserDao ayUserDao;

    public List<AyUser> findAll() {
        return ayUserDao.findAll();
    }


}
