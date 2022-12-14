package com.leihao.wiki.controller;

import com.leihao.wiki.domain.Demo;
import com.leihao.wiki.domain.Test;
import com.leihao.wiki.service.DemoService;
import com.leihao.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {

    @Autowired
    private DemoService demoService;


    @GetMapping("/demo/list")
    public List<Demo> list(){
        return demoService.list();
    }

}
