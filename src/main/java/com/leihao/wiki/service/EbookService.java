package com.leihao.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leihao.wiki.domain.Ebook;
import com.leihao.wiki.domain.EbookExample;
import com.leihao.wiki.mapper.EbookMapper;
import com.leihao.wiki.request.EbookQueryRequest;
import com.leihao.wiki.request.EbookSaveRequest;
import com.leihao.wiki.response.EbookQueryResponse;
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
public class EbookService {

    private static final Logger LOG= LoggerFactory.getLogger(EbookService.class);


    @Autowired
    private EbookMapper ebookMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResponse<EbookQueryResponse> list(EbookQueryRequest request){
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(request.getName())){
            criteria.andNameLike("%"+request.getName()+"%");
        }
        if (!ObjectUtils.isEmpty(request.getCategory2Id())){
            criteria.andCategory2IdEqualTo(request.getCategory2Id());
        }
        PageHelper.startPage(request.getPageNum(),request.getPageSize());
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook>pageInfo=new PageInfo<>(ebookList);
        LOG.info("总行数：{}",pageInfo.getTotal());

        List<EbookQueryResponse> ebookResponseList= CopyUtil.copyList(ebookList, EbookQueryResponse.class);

        PageResponse<EbookQueryResponse> response = new PageResponse<>();
        response.setTotal(pageInfo.getTotal());
        response.setList(ebookResponseList);

        return response;
    }

    public void save(EbookSaveRequest request) {
        Ebook ebook = CopyUtil.copy(request, Ebook.class);
        if (ObjectUtils.isEmpty(request.getId())){
            ebook.setId(snowFlake.nextId());
            ebookMapper.insert(ebook);
        }else {
            ebookMapper.updateByPrimaryKey(ebook);
        }

    }

    public void delete(Long id) {
        ebookMapper.deleteByPrimaryKey(id);
    }
}
