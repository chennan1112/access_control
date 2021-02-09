package com.unicom.access.task;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.unicom.access.entity.TimeTask;
import com.unicom.access.mapper.TimeTaskMapper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * 心跳上传
 *
 * @author mrChen
 * @date 2021/2/6 10:58
 */
@Configuration
@EnableScheduling
public class HeartbeatUploadTask {


    private Logger log = LoggerFactory.getLogger(HeartbeatUploadTask.class);

    private static Long inc = 2L;

    @Autowired
    RedisLock redisLock;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    TimeTaskMapper timeTaskMapper;
    @Autowired
    ScanScheduledTask scanScheduledTask;

    @Scheduled(cron = "0 0/1 * * * ?")
    public void HeartbeatUpload() {

        stringRedisTemplate.opsForValue().set("task_end", "0");

        Timestamp currentTimeMillis = new Timestamp(System.currentTimeMillis());
        String st = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS").format(currentTimeMillis);

        log.info("==========================开始执行任务遗漏扫描============================");
        while (stringRedisTemplate.opsForValue().get("task_end").equals("0")) {
            boolean lockKey = redisLock.lock("lock_key", st);
            if (lockKey) {
                String current_id = stringRedisTemplate.opsForValue().get("current_id");
                if (StringUtils.isEmpty(current_id)) {
                    current_id = "0";
                }
                List<TimeTask> timeTasks = timeTaskMapper.selectByLimit(Long.valueOf(current_id), inc);
                /**/
                if (timeTasks.size() == 0) {
                    current_id = "0";
                    stringRedisTemplate.opsForValue().set("current_id",current_id);
                    /*通知其他线程 结束本次任务*/
                    stringRedisTemplate.opsForValue().set("task_end", "0");
                    redisLock.unlock("lock_key");
                    break;
                } else {
                    stringRedisTemplate.opsForValue().set("current_id", String.valueOf(Long.valueOf(current_id) + inc));
                    redisLock.unlock("lock_key");
                }
                /*执行任务*/
                scanScheduledTask.task(timeTasks);
            }
        }
    }
}
