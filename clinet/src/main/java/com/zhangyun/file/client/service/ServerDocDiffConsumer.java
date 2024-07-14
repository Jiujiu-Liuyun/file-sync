package com.zhangyun.file.client.service;

import com.zhangyun.file.common.domain.doc.DocumentDiff;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.BlockingQueue;

@Service
@Slf4j
public class ServerDocDiffConsumer implements InitializingBean {
    @Resource
    private BlockingQueue<DocumentDiff> documentDiffBlockingQueue;
    @Resource
    private ClientDocManageService clientDocManageService;

    @Override
    public void afterPropertiesSet() throws Exception {
        new Thread(() -> {
            while (true) {
                try {
                    DocumentDiff diff = documentDiffBlockingQueue.take();
                    log.info("处理文档差异, diff: {}", diff);
                    clientDocManageService.handleDocDiff(diff);
                } catch (Exception e) {
                    log.error("消费文档差异数据失败, {}", ExceptionUtils.getStackTrace(e));
                }
            }
        }).start();
    }
}
