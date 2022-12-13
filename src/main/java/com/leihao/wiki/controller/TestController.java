package com.leihao.wiki.controller;

import com.leihao.wiki.domain.Test;
import com.leihao.wiki.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/hello")
    public String hello(){
        return "hello world !";
    }

    @GetMapping("/test/list")
    public List<Test> list(){
        return testService.list();
    }

}
