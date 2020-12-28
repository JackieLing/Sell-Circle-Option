package com.ay.controller;

import com.ay.dto.MoodDTO;
import com.ay.service.MoodService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * 描述：说说控制层
 *
 * @author Ay
 * @date 2018/1/6.
 */
@Controller
@RequestMapping("/mood")
public class MoodController {

    @Resource
    private MoodService moodService;

    @GetMapping(value = "/findAll")
    public String findAll(Model model) {
        List<MoodDTO> moodDTOList = moodService.findAll();
        model.addAttribute("moods", moodDTOList);
        return "mood";
    }
    @GetMapping(value = "/{moodId}/praise")
    public String praise(Model model, @PathVariable(value = "moodId") String moodId,
                         @RequestParam(value = "userId") String userId) {
        boolean isPraise = moodService.praiseMood(userId, moodId);

        List<MoodDTO> moodDTOList = moodService.findAll();
        model.addAttribute("moods", moodDTOList);
        model.addAttribute("isPraise", isPraise);
        return "mood";
    }


}
