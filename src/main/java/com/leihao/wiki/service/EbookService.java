package com.leihao.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leihao.wiki.domain.Demo;
import com.leihao.wiki.domain.Ebook;
import com.leihao.wiki.domain.EbookExample;
import com.leihao.wiki.mapper.DemoMapper;
import com.leihao.wiki.mapper.EbookMapper;
import com.leihao.wiki.request.EbookRequest;
import com.leihao.wiki.response.EbookResponse;
import com.leihao.wiki.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {

    private static final Logger LOG= LoggerFactory.getLogger(EbookService.class);


    @Autowired
    private EbookMapper ebookMapper;

    public List<EbookResponse> list(EbookRequest  request){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(request.getName())){
            criteria.andNameLike("%"+request.getName()+"%");
        }
        PageHelper.startPage(1,3);
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook>pageInfo=new PageInfo<>(ebookList);
        LOG.info("总行数：{}",pageInfo.getTotal());
        LOG.info("总页数：{}",pageInfo.getPages());

        List<EbookResponse> ebookResponseList= CopyUtil.copyList(ebookList, EbookResponse.class);


        return ebookResponseList;
    }
}
