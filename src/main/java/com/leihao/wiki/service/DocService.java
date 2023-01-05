package com.leihao.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leihao.wiki.domain.Doc;
import com.leihao.wiki.domain.DocExample;
import com.leihao.wiki.mapper.DocMapper;
import com.leihao.wiki.request.DocQueryRequest;
import com.leihao.wiki.request.DocSaveRequest;
import com.leihao.wiki.response.DocQueryResponse;
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
public class DocService {

    private static final Logger LOG= LoggerFactory.getLogger(DocService.class);


    @Autowired
    private DocMapper docMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResponse<DocQueryResponse> list(DocQueryRequest request){
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        PageHelper.startPage(request.getPageNum(),request.getPageSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc>pageInfo=new PageInfo<>(docList);
        LOG.info("总行数：{}",pageInfo.getTotal());

        List<DocQueryResponse> docResponseList= CopyUtil.copyList(docList, DocQueryResponse.class);

        PageResponse<DocQueryResponse> response = new PageResponse<>();
        response.setTotal(pageInfo.getTotal());
        response.setList(docResponseList);

        return response;
    }


    public List<DocQueryResponse> all(){
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
        List<DocQueryResponse> docResponseList= CopyUtil.copyList(docList, DocQueryResponse.class);
        return docResponseList;
    }

    public void save(DocSaveRequest request) {
        Doc doc = CopyUtil.copy(request, Doc.class);
        if (ObjectUtils.isEmpty(request.getId())){
            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);
        }else {
            docMapper.updateByPrimaryKey(doc);
        }

    }

    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.Criteria criteria = docExample.createCriteria();
        criteria.andIdIn(ids);
        docMapper.deleteByExample(docExample);
    }
}
