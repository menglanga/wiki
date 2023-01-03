package com.leihao.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.leihao.wiki.domain.Category;
import com.leihao.wiki.domain.CategoryExample;
import com.leihao.wiki.mapper.CategoryMapper;
import com.leihao.wiki.request.CategoryQueryRequest;
import com.leihao.wiki.request.CategorySaveRequest;
import com.leihao.wiki.response.CategoryQueryResponse;
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
public class CategoryService {

    private static final Logger LOG= LoggerFactory.getLogger(CategoryService.class);


    @Autowired
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;

    public PageResponse<CategoryQueryResponse> list(CategoryQueryRequest request){
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        PageHelper.startPage(request.getPageNum(),request.getPageSize());
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category>pageInfo=new PageInfo<>(categoryList);
        LOG.info("总行数：{}",pageInfo.getTotal());

        List<CategoryQueryResponse> categoryResponseList= CopyUtil.copyList(categoryList, CategoryQueryResponse.class);

        PageResponse<CategoryQueryResponse> response = new PageResponse<>();
        response.setTotal(pageInfo.getTotal());
        response.setList(categoryResponseList);

        return response;
    }


    public List<CategoryQueryResponse> all(){
        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
        List<CategoryQueryResponse> categoryResponseList= CopyUtil.copyList(categoryList, CategoryQueryResponse.class);
        return categoryResponseList;
    }

    public void save(CategorySaveRequest request) {
        Category category = CopyUtil.copy(request, Category.class);
        if (ObjectUtils.isEmpty(request.getId())){
            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        }else {
            categoryMapper.updateByPrimaryKey(category);
        }

    }

    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
}
