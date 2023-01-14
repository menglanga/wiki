package com.leihao.wiki.controller;

import com.leihao.wiki.request.DocQueryRequest;
import com.leihao.wiki.request.DocSaveRequest;
import com.leihao.wiki.response.DocQueryResponse;
import com.leihao.wiki.response.CommonResponse;
import com.leihao.wiki.response.PageResponse;
import com.leihao.wiki.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Autowired
    private DocService docService;


    @GetMapping("/list")
    public CommonResponse list(@Valid DocQueryRequest request){
        CommonResponse<PageResponse<DocQueryResponse>> response = new CommonResponse<>();
        PageResponse<DocQueryResponse> docList = docService.list(request);
        response.setData(docList);
        return response;
    }


    @GetMapping("/all/{ebookId}")
    public CommonResponse all(@PathVariable Long ebookId){
        CommonResponse<List<DocQueryResponse>> response = new CommonResponse<>();
        List<DocQueryResponse> docList = docService.all(ebookId);
        response.setData(docList);
        return response;
    }

    @PostMapping("/save")
    public CommonResponse save( @Valid @RequestBody DocSaveRequest request){
        CommonResponse response = new CommonResponse<>();
        docService.save(request);
        return response;
    }

    @DeleteMapping("/delete/{idsStr}")
    public CommonResponse delete(@PathVariable String idsStr){
        CommonResponse response = new CommonResponse<>();
        List<String> list = Arrays.asList(idsStr.split(","));
        docService.delete(list);
        return response;
    }

    @GetMapping("/find-content/{id}")
    public CommonResponse findContent(@PathVariable Long id){
        CommonResponse<String> response = new CommonResponse<>();
        String content = docService.findContent(id);
        response.setData(content);
        return response;
    }
}
