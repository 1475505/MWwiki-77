package com.liul2566.wiki.job;

import com.liul2566.wiki.service.DocService;
import com.liul2566.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

//定时任务只有一个线程
@Component
public class DocJob {

    private static final Logger LOG = LoggerFactory.getLogger(DocJob.class);

    @Resource
    private DocService docService;

    @Resource
    private SnowFlake snowFlake;

    /**
     * 每30秒更新电子书信息
     */
    //@Scheduled(cron = "0 5/20 * * * ?")//cron表达式
    @Scheduled(cron = "5/40 * * * * ?")//cron表达式
    public void cron() {
        // 增加日志流水号，方便分开不同业务的日志
        MDC.put("LOG_ID", String.valueOf(snowFlake.nextId()));
        LOG.info("更新电子书下的文档数据开始");
        long start = System.currentTimeMillis();
        docService.updateEbookInfo();
        LOG.info("更新电子书下的文档数据结束，耗时：{}毫秒", System.currentTimeMillis() - start);
    }

}
