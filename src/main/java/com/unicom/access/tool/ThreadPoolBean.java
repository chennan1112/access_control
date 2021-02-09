package com.unicom.access.tool;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author mrChen
 * @date 2021/2/3 14:55
 */
@Configuration
public class ThreadPoolBean {

    /**
     * 定时刷新遗漏的任务
     *
     * @return
     */
    @Bean("switchTaskExecutor")
    public ThreadPoolExecutor switchTaskExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                10,
                20,
                70,
                TimeUnit.MINUTES,
                new ArrayBlockingQueue<>(1000 * 10),
                new ThreadPoolExecutor.AbortPolicy());
        return executor;
    }
}
