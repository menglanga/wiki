package com.leihao.wiki.job;

import com.leihao.wiki.service.DocService;
import com.leihao.wiki.service.EbookSnapShotService;
import com.leihao.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class EbookSnapshotJob {
    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    @Autowired
    private EbookSnapShotService ebookSnapShotService;


    @Autowired
    private SnowFlake snowFlake;

    @Scheduled(cron = "0/10 * * * * ?")
    public void doSnapshot()  {
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        LOG.info("生成今日电子书快照开始：");
        long start = System.currentTimeMillis();
        ebookSnapShotService.doSnapshot();
        LOG.info("生成今日电子书快照结束,耗时：{}ms",System.currentTimeMillis()-start);
    }
}
