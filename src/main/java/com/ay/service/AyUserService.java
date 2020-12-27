package com.ay.service;

import com.ay.model.AyUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;
import java.util.Map;

public interface AyUserService {

    List<AyUser> findAll();

}
