package com.unicom.access.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.unicom.access.entity.TimeTask;
import com.unicom.access.mapper.TimeTaskMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 定时扫描没有应异常没有解锁的网络
 *
 * @author mrChen
 * @date 2021/2/2 18:08
 */
@Component
public class ScanScheduledTask {

    DateFormat sdf = new SimpleDateFormat("HH:mm:ss");

    private Logger log = LoggerFactory.getLogger(ScanScheduledTask.class);

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    /*查询状态*/
    private final static String status_url = "http://[serverip]:[port]/user-self/status";
    /*加锁*/
    private final static String lock_url = "http://[serverip]:[port]/user-self/lock";
    /*解锁*/
    private final static String unlock_url = "http://[serverip]:[port]/user-self/unlock";


    String[] weekDays = {"7", "1", "2", "3", "4", "5", "6"};

    @Autowired
    TimeTaskMapper timeTaskMapper;
    @Autowired
    ThreadPoolExecutor switchTaskExecutor;

    public void task(List<TimeTask> timeTasks_0$) {
        timeTasks_0$.forEach(x -> {
            log.info("taskId:{} ,accountId {} 任务开始",x.getId(),x.getAccountId());
            if (x.getIsDelete() == 1) {
                log.info("taskId:{} ,accountId {} 任务已删除",x.getId(),x.getAccountId());
                return;
            }
            /*判断今天有没有任务*/
            Calendar calendar = Calendar.getInstance();
            String s = weekDays[calendar.get(Calendar.DAY_OF_WEEK) - 1];
            boolean contains = Arrays.asList(x.getWeekDay().split(",")).contains(s);
            if (!contains) {
                log.info("taskId:{} ,accountId {} 本天没有任务",x.getId(),x.getAccountId());
                return;
            }
            int t_$0 = sdf.format(x.getStartTime()).compareTo(sdf.format(new Timestamp(System.currentTimeMillis())));
            int t_$1 = sdf.format(x.getEndTime()).compareTo(sdf.format(new Timestamp(System.currentTimeMillis())));
            /*关闭*/
            if (t_$0 < 0 && t_$1 > 0) {
                if (x.getStatus() != 1) {
                    switchTaskExecutor.execute(() -> {
                        log.info("taskId:{} ,accountId {} 执行关闭",x.getId(),x.getAccountId());
                        Map<String, String> lockParam = new HashMap<>();
                        lockParam.put("accountId", x.getAccountId());
                        try {
                            EntityWrapper<TimeTask> ew = new EntityWrapper<>();
                            ew.eq("id", x.getId());
                            x.setStatus(1);
                            //HttpClientUtil.doPost(lock_url, lockParam);
                            timeTaskMapper.update(x, ew);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                }
            }

            /*开启*/
            if (sdf.format(x.getEndTime()).compareTo(sdf.format(new Timestamp(System.currentTimeMillis()))) < 0) {
                if (x.getStatus() != 0) {
                    switchTaskExecutor.execute(() -> {
                        log.info("taskId:{} ,accountId {} 执行开启",x.getId(),x.getAccountId());
                        Map<String, String> lockParam = new HashMap<>();
                        lockParam.put("accountId", x.getAccountId());
                        try {
                            EntityWrapper<TimeTask> ew = new EntityWrapper<>();
                            ew.eq("id", x.getId());
                            x.setStatus(0);
                            //HttpClientUtil.doPost(unlock_url, lockParam);
                            timeTaskMapper.update(x, ew);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                }
            }
            log.info("taskId:{} ,accountId {} 时间段下没有任务",x.getId(),x.getAccountId());
        });
    }
}
