package com.ay.dao;

import com.ay.model.Mood;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MoodDao {

    List<Mood> findAll();
}
