package com.unicom.access.task;

import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * @author mrChen
 * @date 2021/2/6 14:15
 */
@Component
public class RedisLock {

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    private static final Integer MINUTES = 3 * 60 * 1000;

    /**
     * 加锁
     *
     * @param key
     * @param timeStamp
     * @return
     */
    public boolean lock(String key, String timeStamp){
        if (stringRedisTemplate.opsForValue().setIfAbsent(key, timeStamp)) {
            return true;
        }
        String currentLock = stringRedisTemplate.opsForValue().get(key);
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long lk=0L;
        try {
            lk=format.parse(currentLock).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (!Strings.isNullOrEmpty(currentLock) && System.currentTimeMillis() - lk > MINUTES) {
            String preLock = stringRedisTemplate.opsForValue().getAndSet(key, timeStamp);
            if (!Strings.isNullOrEmpty(preLock) && preLock.equals(currentLock)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 解锁
     *
     * @param key
     */
    public void unlock(String key) {
        try {
            String currentValue = stringRedisTemplate.opsForValue().get(key);
            if (!Strings.isNullOrEmpty(currentValue)) {
                stringRedisTemplate.opsForValue().getOperations().delete(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
