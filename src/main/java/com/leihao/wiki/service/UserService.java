package com.leihao.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leihao.wiki.domain.User;
import com.leihao.wiki.domain.UserExample;
import com.leihao.wiki.mapper.UserMapper;
import com.leihao.wiki.request.UserQueryRequest;
import com.leihao.wiki.request.UserSaveRequest;
import com.leihao.wiki.response.UserQueryResponse;
import com.leihao.wiki.response.PageResponse;
import com.leihao.wiki.util.CopyUtil;
import com.leihao.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
            user.setId(snowFlake.nextId());
            userMapper.insert(user);
        }else {
            userMapper.updateByPrimaryKey(user);
        }

    }

    public void delete(Long id) {
        userMapper.deleteByPrimaryKey(id);
    }
}
