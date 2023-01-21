package com.leihao.wiki.job;

import com.leihao.wiki.service.DocService;
import com.leihao.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class DocJob {
    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    @Autowired
    private DocService docService;

    @Autowired
    private SnowFlake snowFlake;

    @Scheduled(cron = "*/30 * * * * ?")
    public void updateEbookInfo()  {
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        LOG.info("更新电子书信息开始：");
        long start = System.currentTimeMillis();
        docService.updateEbookInfo();
        LOG.info("更新电子书信息结束,耗时：{}ms",System.currentTimeMillis()-start);
    }
}
