package com.unicom.access.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * redis配置类
 *
 * @author zcc ON 2018/3/19
 **/
public class RedisTemplateConfig {
//    @Bean("redisTem")
//    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
//        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
//        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
//        stringRedisTemplate.setConnectionFactory(factory);
//        return stringRedisTemplate;
//    }
//    @Bean
//    public ListRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
//        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
//        stringRedisTemplate.setKeySerializer(new StringRedisSerializer());
//        stringRedisTemplate.setConnectionFactory(factory);
//        return stringRedisTemplate;
//    }
}