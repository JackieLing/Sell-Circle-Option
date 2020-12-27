package com.ay.dao;

import com.ay.model.User;
import org.springframework.stereotype.Repository;

/**
 * 描述：用户Dao
 *
 * @author Ay
 * @create 2018/06/30
 **/
@Repository
public interface UserDao {

    User find(String id);
}
