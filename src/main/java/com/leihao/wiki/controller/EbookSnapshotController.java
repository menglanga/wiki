package com.leihao.wiki.controller;

import com.leihao.wiki.response.CommonResponse;
import com.leihao.wiki.response.StatisticResponse;
import com.leihao.wiki.service.EbookSnapShotService;
import com.sun.org.glassfish.external.statistics.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ebook-snapshot")
public class EbookSnapshotController {

    @Autowired
    private EbookSnapShotService ebookSnapShotService;

    @GetMapping("/get-statistic")
    public CommonResponse getStatistic(){
        List<StatisticResponse> statisticResponseList=ebookSnapShotService.getStatistic();
        CommonResponse<List<StatisticResponse>> commonResponse=new CommonResponse<>();
        commonResponse.setData(statisticResponseList);
        return commonResponse;
    }

    @GetMapping("/get-30statistic")
    public CommonResponse get30Statistic(){
        List<StatisticResponse> statisticResponseList=ebookSnapShotService.get30Statistic();
        CommonResponse<List<StatisticResponse>> commonResponse=new CommonResponse<>();
        commonResponse.setData(statisticResponseList);
        return commonResponse;
    }
}
