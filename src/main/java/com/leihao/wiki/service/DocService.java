package com.leihao.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.leihao.wiki.domain.Content;
import com.leihao.wiki.domain.Doc;
import com.leihao.wiki.domain.DocExample;
import com.leihao.wiki.mapper.ContentMapper;
import com.leihao.wiki.mapper.DocMapper;
import com.leihao.wiki.mapper.DocMapperCustom;
import com.leihao.wiki.request.DocQueryRequest;
import com.leihao.wiki.request.DocSaveRequest;
import com.leihao.wiki.response.DocQueryResponse;
import com.leihao.wiki.response.PageResponse;
import com.leihao.wiki.util.CopyUtil;
import com.leihao.wiki.util.SnowFlake;
import com.mysql.cj.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);


    @Autowired
    private DocMapper docMapper;

    @Autowired
    private DocMapperCustom docMapperCustom;

    @Autowired
    private ContentMapper contentMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResponse<DocQueryResponse> list(DocQueryRequest request) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        PageHelper.startPage(request.getPageNum(), request.getPageSize());
        List<Doc> docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数：{}", pageInfo.getTotal());

        List<DocQueryResponse> docResponseList = CopyUtil.copyList(docList, DocQueryResponse.class);

        PageResponse<DocQueryResponse> response = new PageResponse<>();
        response.setTotal(pageInfo.getTotal());
        response.setList(docResponseList);

        return response;
    }


    public List<DocQueryResponse> all(Long ebookId) {
        DocExample docExample = new DocExample();
        docExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = docMapper.selectByExample(docExample);
        List<DocQueryResponse> docResponseList = CopyUtil.copyList(docList, DocQueryResponse.class);
        return docResponseList;
    }

    public void save(DocSaveRequest request) {
        Doc doc = CopyUtil.copy(request, Doc.class);
        Content content = CopyUtil.copy(request, Content.class);
        if (ObjectUtils.isEmpty(request.getId())) {
            doc.setId(snowFlake.nextId());
            doc.setViewCount(0);
            doc.setVoteCount(0);
            docMapper.insert(doc);
            content.setId(doc.getId());
            contentMapper.insert(content);
        } else {
            docMapper.updateByPrimaryKey(doc);
            int update = contentMapper.updateByPrimaryKeyWithBLOBs(content);
            if (update == 0) {
                contentMapper.insert(content);
            }
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

    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        docMapperCustom.increaseViewCount(id);
        if (ObjectUtils.isEmpty(content)) {
            return "";
        } else {
            return content.getContent();
        }
    }

    public void vote(Long id) {
        docMapperCustom.increaseVoteCount(id);
    }
}
