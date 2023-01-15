package com.leihao.wiki.controller;

import com.leihao.wiki.request.UserQueryRequest;
import com.leihao.wiki.request.UserSaveRequest;
import com.leihao.wiki.response.CommonResponse;
import com.leihao.wiki.response.UserQueryResponse;
import com.leihao.wiki.response.PageResponse;
import com.leihao.wiki.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/list")
    public CommonResponse list(@Valid UserQueryRequest request){
        CommonResponse<PageResponse<UserQueryResponse>> response = new CommonResponse<>();
        PageResponse<UserQueryResponse> userList = userService.list(request);
        response.setData(userList);
        return response;
    }


    @PostMapping("/save")
    public CommonResponse save( @Valid @RequestBody UserSaveRequest request){
        request.setPassword(DigestUtils.md5DigestAsHex(request.getPassword().getBytes()));
        CommonResponse response = new CommonResponse<>();
        userService.save(request);
        return response;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable Long id){
        CommonResponse response = new CommonResponse<>();
        userService.delete(id);
        return response;
    }

}
