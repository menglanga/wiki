package com.leihao.wiki.controller;

import com.alibaba.fastjson.JSONObject;
import com.leihao.wiki.request.UserLoginRequest;
import com.leihao.wiki.request.UserQueryRequest;
import com.leihao.wiki.request.UserResetPasswordRequest;
import com.leihao.wiki.request.UserSaveRequest;
import com.leihao.wiki.response.CommonResponse;
import com.leihao.wiki.response.UserLoginResponse;
import com.leihao.wiki.response.UserQueryResponse;
import com.leihao.wiki.response.PageResponse;
import com.leihao.wiki.service.UserService;
import com.leihao.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger LOG= LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private SnowFlake snowFlake;




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

    @PostMapping("/reset-password")
    public CommonResponse resetPassword( @Valid @RequestBody UserResetPasswordRequest request){
        request.setPassword(DigestUtils.md5DigestAsHex(request.getPassword().getBytes()));
        CommonResponse response = new CommonResponse<>();
        userService.resetPassword(request);
        return response;
    }


    @PostMapping("/login")
    public CommonResponse<UserLoginResponse> login( @Valid @RequestBody UserLoginRequest request){
        request.setPassword(DigestUtils.md5DigestAsHex(request.getPassword().getBytes()));
        CommonResponse<UserLoginResponse> response = new CommonResponse<>();
        UserLoginResponse userLoginResponse=userService.login(request);

        Long token = snowFlake.nextId();
        LOG.info("生成单点登录token:{},存入redis",token);
        userLoginResponse.setToken(token.toString());
        redisTemplate.opsForValue().set(token.toString(), JSONObject.toJSONString(userLoginResponse),3600*24, TimeUnit.SECONDS);

        response.setData(userLoginResponse);
        return response;
    }

    @GetMapping("/logout/{token}")
    public CommonResponse logout(@PathVariable String token){
        CommonResponse response = new CommonResponse<>();
        redisTemplate.delete(token);
        LOG.info("从redis中删除token:{}",token);
        return response;
    }
}
