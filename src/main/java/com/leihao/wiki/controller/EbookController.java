package com.leihao.wiki.controller;

import com.leihao.wiki.domain.Ebook;
import com.leihao.wiki.request.EbookRequest;
import com.leihao.wiki.response.CommonResponse;
import com.leihao.wiki.response.EbookResponse;
import com.leihao.wiki.response.PageResponse;
import com.leihao.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Autowired
    private EbookService ebookService;


    @GetMapping("/list")
    public CommonResponse list(EbookRequest request){
        CommonResponse<PageResponse<EbookResponse>> response = new CommonResponse<>();
        PageResponse<EbookResponse> ebookList = ebookService.list(request);
        response.setData(ebookList);
        return response;
    }

}
