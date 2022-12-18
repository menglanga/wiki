package com.leihao.wiki.service;

import com.leihao.wiki.domain.Demo;
import com.leihao.wiki.domain.Ebook;
import com.leihao.wiki.domain.EbookExample;
import com.leihao.wiki.mapper.DemoMapper;
import com.leihao.wiki.mapper.EbookMapper;
import com.leihao.wiki.request.EbookRequest;
import com.leihao.wiki.response.EbookResponse;
import com.leihao.wiki.util.CopyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {

    @Autowired
    private EbookMapper ebookMapper;

    public List<EbookResponse> list(EbookRequest  request){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(request.getName())){
            criteria.andNameLike("%"+request.getName()+"%");
        }
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
        List<EbookResponse> ebookResponseList=new ArrayList<>();
        for (Ebook ebook : ebookList){
//            EbookResponse ebookResponse=new EbookResponse();
//            BeanUtils.copyProperties(ebook,ebookResponse);
//            ebookResponseList.add(ebookResponse);
            //单体复制
//            EbookResponse ebookResponse = CopyUtil.copy(ebook, EbookResponse.class);
//            ebookResponseList.add(ebookResponse);
            //列表复制
            ebookResponseList= CopyUtil.copyList(ebookList, EbookResponse.class);
        }
        return ebookResponseList;
    }
}
