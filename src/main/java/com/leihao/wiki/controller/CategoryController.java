package com.leihao.wiki.controller;

import com.leihao.wiki.request.CategoryQueryRequest;
import com.leihao.wiki.request.CategorySaveRequest;
import com.leihao.wiki.response.CommonResponse;
import com.leihao.wiki.response.CategoryQueryResponse;
import com.leihao.wiki.response.PageResponse;
import com.leihao.wiki.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;


    @GetMapping("/list")
    public CommonResponse list(@Valid CategoryQueryRequest request){
        CommonResponse<PageResponse<CategoryQueryResponse>> response = new CommonResponse<>();
        PageResponse<CategoryQueryResponse> categoryList = categoryService.list(request);
        response.setData(categoryList);
        return response;
    }


    @PostMapping("/save")
    public CommonResponse save( @Valid @RequestBody CategorySaveRequest request){
        CommonResponse response = new CommonResponse<>();
        categoryService.save(request);
        return response;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable Long id){
        CommonResponse response = new CommonResponse<>();
        categoryService.delete(id);
        return response;
    }
}
