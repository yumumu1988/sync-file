package com.yumumu.syncServer.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.yumumu.syncServer.model.bo.DownloadFileInfo;
import com.yumumu.syncServer.service.FileRecordService;
import com.yumumu.syncServer.utils.AccessUtils;

/**
 * @author zhanghailin
 * @date 2022/6/11
 */
@Controller
@RequestMapping("/file")
public class FileController {

    @Resource
    private FileRecordService fileRecordService;

    @GetMapping("/list")
    public ModelAndView getFileList(@RequestParam(value = "pageNum", required = false) Integer pageNum,
        @RequestParam(value = "pageSize", required = false) Integer pageSize,
        @RequestParam(value = "name", required = false) String name, HttpServletRequest httpServletRequest) {
        if (!AccessUtils.access(httpServletRequest)) {
            return new ModelAndView(new RedirectView("/"));
        }
        ModelAndView modelAndView = new ModelAndView("fileList");
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 50;
        }
        List<DownloadFileInfo> fileList = fileRecordService.getFileList(pageNum, pageSize, name);
        modelAndView.addObject("fileList", fileList);
        modelAndView.addObject("pageNum", pageNum);
        return modelAndView;
    }

}
