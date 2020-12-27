package com.ay.controller;

import com.ay.model.AyUser;
import com.ay.service.AyUserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import java.util.List;

/**
 * 用户控制层
 *
 * @author Ay
 * @date 2018/04/02
 */
@Controller
@RequestMapping("/user")
public class AyUserController {

    @Resource
    private AyUserService ayUserService;
    @GetMapping("/findAll")
   public String findAll(Model model){
       List<AyUser> ayUserList = ayUserService.findAll();
        for(AyUser ayUser : ayUserList){
           System.out.println("id: " + ayUser.getId());
            System.out.println("name: " + ayUser.getName());
        }
        return "hello";
   }

}
