package com.airbnb.bookingservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisLockService
{

    @Autowired
    private StringRedisTemplate redisTemplate;

    public boolean acquireLock(String key) {
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, "locked", Duration.ofSeconds(10));
        return Boolean.TRUE.equals(success);
    }

    public void releaseLock(String key) {
        redisTemplate.delete(key);
    }
}
