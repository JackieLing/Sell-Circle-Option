package com.ay.test;

import com.ay.dao.AyUserDao;
import com.ay.model.AyUser;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.junit.Test;
import org.mybatis.spring.SqlSessionFactoryBean;

import javax.annotation.Resource;
import java.sql.Connection;
import java.util.List;

/**
 * 描述：用户DAO测试类
 *
 * @author Ay
 * @create 2018/05/04
 **/
public class AyUserDaoTest extends BaseJunit4Test {

    @Resource
    private AyUserDao ayUserDao;

    @Test
    public void testFindAll() {
        List<AyUser> userList = ayUserDao.findAll();
        System.out.println(userList.size());

    }
}



