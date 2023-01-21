package com.leihao.wiki.service;

import com.leihao.wiki.mapper.EbookSnapshotMapperCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EbookSnapShotService {

    @Autowired
    private EbookSnapshotMapperCustom ebookSnapshotMapperCustom;

    public void doSnapshot() {
        ebookSnapshotMapperCustom.doSnapshot();
    }
}
