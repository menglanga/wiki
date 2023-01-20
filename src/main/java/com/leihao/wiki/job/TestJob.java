//package com.leihao.wiki.job;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//@Component
//public class TestJob {
//
//    private static final Logger LOG = LoggerFactory.getLogger(TestJob.class);
//
//    @Scheduled(fixedRate = 1000)
//    public void simple() throws InterruptedException {
//        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
//        String dateStr = sdf.format(new Date());
////        Thread.sleep(2000);
//        LOG.info("每隔1秒执行一次：{}", dateStr);
//    }
//
//    @Scheduled(cron = "*/2 * * * * ?")
//    public void cron() throws InterruptedException {
//        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
//        String dateStr = sdf.format(new Date());
//        Thread.sleep(2000);
//        LOG.info("每隔2秒执行一次：{}", dateStr);
//    }
//
//
//}
