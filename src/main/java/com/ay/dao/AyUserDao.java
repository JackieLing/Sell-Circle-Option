package com.ay.dao;

import com.ay.model.AyUser;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AyUserDao {

    List<AyUser> findAll();


}
