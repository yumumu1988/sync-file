package com.yumumu.syncServer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yumumu.syncServer.model.bo.UserInfo;
import com.yumumu.syncServer.utils.AccessUtils;

/**
 * @author zhanghailin
 * @date 2022/6/11
 */
@Controller
@RequestMapping({"/", ""})
public class MainController {

    @GetMapping({"", "/"})
    public String index(HttpServletRequest httpServletRequest) {
        if (AccessUtils.access(httpServletRequest)) {
            return "redirect:/file/list";
        }
        return "index";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute UserInfo userInfo, HttpServletResponse httpServletResponse) {
        String username = userInfo.getUsername();
        String password = userInfo.getPassword();
        if (AccessUtils.access(username, password)) {
            AccessUtils.addCookie(username, password, httpServletResponse);
            return "redirect:/file/list";
        } else {
            return "index";
        }
    }

}
