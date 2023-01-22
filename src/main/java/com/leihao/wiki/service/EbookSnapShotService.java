package com.leihao.wiki.service;

import com.leihao.wiki.mapper.EbookSnapshotMapperCustom;
import com.leihao.wiki.response.StatisticResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EbookSnapShotService {

    @Autowired
    private EbookSnapshotMapperCustom ebookSnapshotMapperCustom;

    public void doSnapshot() {
        ebookSnapshotMapperCustom.doSnapshot();
    }

    public List<StatisticResponse> getStatistic() {
       return ebookSnapshotMapperCustom.getStatistic();
    }

    public List<StatisticResponse> get30Statistic() {
        return ebookSnapshotMapperCustom.get30Statistic();

    }
}
