/*
 * Copyright (c) 2014-2018 www.itgardener.cn. All rights reserved.
 */

package cn.itgardener.nefu.library.service.schedule;

import cn.itgardener.nefu.library.service.BookCaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @author : Jimi
 * @date : 2018/11/1
 * @since : Java 8
 */
@Component
public class ScheduledTask {

    private Logger logger = LoggerFactory.getLogger(ScheduledTask.class);

    private int fixedRateCount = 1;

    private final BookCaseService bookCaseService;

    @Autowired
    public ScheduledTask(BookCaseService bookCaseService) {
        this.bookCaseService = bookCaseService;
    }

    /**
     * fixedDelay = 1000表示当前方法开始执行1000ms后,Spring scheduling会再次调用该方法
     */
    @Scheduled(fixedDelay = 1000)
    public void doPopList() {
        while (true) {
            logger.debug("ScheduledTask: 第{}次查询队列", fixedRateCount++);
            String studentId = bookCaseService.popQueue();
            if (studentId == null) {
                break;
            }
            bookCaseService.boxQueue(studentId);
            logger.debug("ScheduledTask: 当前{}已经分配完毕", studentId);
        }
    }
}
