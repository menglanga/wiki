package com.leihao.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leihao.wiki.domain.User;
import com.leihao.wiki.domain.UserExample;
import com.leihao.wiki.exception.BusinessException;
import com.leihao.wiki.exception.BusinessExceptionCode;
import com.leihao.wiki.mapper.UserMapper;
import com.leihao.wiki.request.UserLoginRequest;
import com.leihao.wiki.request.UserQueryRequest;
import com.leihao.wiki.request.UserResetPasswordRequest;
import com.leihao.wiki.request.UserSaveRequest;
import com.leihao.wiki.response.UserLoginResponse;
import com.leihao.wiki.response.UserQueryResponse;
import com.leihao.wiki.response.PageResponse;
import com.leihao.wiki.util.CopyUtil;
import com.leihao.wiki.util.SnowFlake;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    private static final Logger LOG= LoggerFactory.getLogger(UserService.class);


    @Resource
    private UserMapper userMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResponse<UserQueryResponse> list(UserQueryRequest request){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(request.getLoginName())){
            criteria.andLoginNameEqualTo(request.getLoginName());
        }
        PageHelper.startPage(request.getPageNum(),request.getPageSize());
        List<User> userList = userMapper.selectByExample(userExample);

        PageInfo<User>pageInfo=new PageInfo<>(userList);
        LOG.info("总行数：{}",pageInfo.getTotal());

        List<UserQueryResponse> userResponseList= CopyUtil.copyList(userList, UserQueryResponse.class);

        PageResponse<UserQueryResponse> response = new PageResponse<>();
        response.setTotal(pageInfo.getTotal());
        response.setList(userResponseList);

        return response;
    }

    public void save(UserSaveRequest request) {
        User user = CopyUtil.copy(request, User.class);
        if (ObjectUtils.isEmpty(request.getId())){
            User userDB = selectByLoginName(request.getLoginName());
            if (ObjectUtils.isEmpty(userDB)){
                user.setId(snowFlake.nextId());
                userMapper.insert(user);
            }else {
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }

        }else {
            user.setLoginName(null);
            user.setPassword(null);
            userMapper.updateByPrimaryKeySelective(user);
        }

    }

    public void resetPassword(UserResetPasswordRequest request) {
        User user = CopyUtil.copy(request, User.class);
        userMapper.updateByPrimaryKeySelective(user);
    }

    public UserLoginResponse login(UserLoginRequest request) {
        User userDB = selectByLoginName(request.getLoginName());
        if (ObjectUtils.isEmpty(userDB)){
            //用户名不存在
            LOG.info("用户名不存在,{}",request.getLoginName());
            throw new BusinessException(BusinessExceptionCode.USER_LOGIN_ERROR);
        }else{
            if (userDB.getPassword().equals(request.getPassword())){
                //登录成功
                UserLoginResponse userLoginResponse = CopyUtil.copy(userDB, UserLoginResponse.class);
                return userLoginResponse;
            }else{
                //密码不正确
                LOG.info("密码不对,输入密码：{}，数据库密码：{}",request.getPassword(),userDB.getPassword());
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_ERROR);
            }
        }
    }

    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }


    public User selectByLoginName(String loginName){
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(loginName)){
            criteria.andLoginNameEqualTo(loginName);
        }
        List<User> userList = userMapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(userList)){
            return null;
        }else{
            return userList.get(0);
        }
    }


}
