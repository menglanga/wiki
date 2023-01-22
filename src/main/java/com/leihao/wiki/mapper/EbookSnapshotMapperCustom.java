package com.leihao.wiki.mapper;

import com.leihao.wiki.domain.EbookSnapshot;
import com.leihao.wiki.domain.EbookSnapshotExample;
import com.leihao.wiki.response.StatisticResponse;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface EbookSnapshotMapperCustom {

    void doSnapshot();

    List<StatisticResponse> getStatistic();

    List<StatisticResponse> get30Statistic();

}