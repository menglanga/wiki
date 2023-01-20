package com.leihao.wiki.job;

import com.leihao.wiki.service.DocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

@Component
public class DocJob {
    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    @Autowired
    private DocService docService;

    @Scheduled(cron = "*/2 * * * * ?")
    public void updateEbookInfo()  {
        LOG.info("更新电子书信息开始：");
        long start = System.currentTimeMillis();
        docService.updateEbookInfo();
        LOG.info("更新电子书信息结束,耗时：{}ms",System.currentTimeMillis()-start);
    }
}
