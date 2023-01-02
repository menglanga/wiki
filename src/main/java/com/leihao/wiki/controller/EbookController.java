package com.leihao.wiki.controller;

import com.leihao.wiki.request.EbookQueryRequest;
import com.leihao.wiki.request.EbookSaveRequest;
import com.leihao.wiki.response.CommonResponse;
import com.leihao.wiki.response.EbookQueryResponse;
import com.leihao.wiki.response.PageResponse;
import com.leihao.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private EbookService ebookService;


    @GetMapping("/list")
    public CommonResponse list(@Valid EbookQueryRequest request){
        CommonResponse<PageResponse<EbookQueryResponse>> response = new CommonResponse<>();
        PageResponse<EbookQueryResponse> ebookList = ebookService.list(request);
        response.setData(ebookList);
        return response;
    }


    @PostMapping("/save")
    public CommonResponse save(@RequestBody EbookSaveRequest request){
        CommonResponse response = new CommonResponse<>();
        ebookService.save(request);
        return response;
    }

    @DeleteMapping("/delete/{id}")
    public CommonResponse delete(@PathVariable Long id){
        CommonResponse response = new CommonResponse<>();
        ebookService.delete(id);
        return response;
    }

}
