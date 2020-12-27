package com.ay.service.impl;

import com.ay.dao.MoodDao;
import com.ay.dao.UserDao;
import com.ay.dto.MoodDTO;
import com.ay.model.Mood;
import com.ay.model.User;
import com.ay.service.MoodService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 描述：说说服务类
 *
 * @author Ay
 * @date 2018/1/6.
 */
@Service
public class MoodServiveImpl implements MoodService {
    @Resource
    private MoodDao moodDao;
    @Resource
    private UserDao userDao;

    public List<MoodDTO> findAll() {
        List<Mood> moodList = moodDao.findAll();
        return converModel2DTO(moodList);
    }

    private List<MoodDTO> converModel2DTO(List<Mood> moodList) {
        if (CollectionUtils.isEmpty(moodList)) return Collections.EMPTY_LIST;
        List<MoodDTO> moodDTOList = new ArrayList<MoodDTO>();
        for (Mood mood : moodList) {
            MoodDTO moodDTO = new MoodDTO();
            moodDTO.setId(mood.getId());
            moodDTO.setContent(mood.getContent());
            moodDTO.setPraiseNum(mood.getPraiseNum());
            moodDTO.setPublishTime(mood.getPublishTime());
            moodDTO.setUserId(mood.getUserId());
            moodDTOList.add(moodDTO);
            //设置用户信息
            User user = userDao.find(mood.getUserId());
            moodDTO.setUserName(user.getName());
            moodDTO.setUserAccount(user.getAccount());
        }
        return moodDTOList;


    }
}
