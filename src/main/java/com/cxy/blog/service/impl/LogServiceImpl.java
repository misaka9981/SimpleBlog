package com.cxy.blog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxy.blog.mapper.LogMapper;
import com.cxy.blog.model.Log;
import com.cxy.blog.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

 
@Slf4j
@Service
@CacheConfig(cacheNames="log")
public class LogServiceImpl extends ServiceImpl<LogMapper, Log> implements LogService {
    @Override
    public List<Log> findLatestLog(int number) {
        return baseMapper.selectLatestLog(number);
    }

    @Override
    @Cacheable(key = "targetClass + methodName")
    public Map<String,Integer> statBrowser(){
        Map<String,Integer> result=new HashMap<>();
        List<String> browsers=baseMapper.selectAllBrowser();
        for (String browser : browsers) {
            result.merge(browser, 1, Integer::sum);
        }
        return result;
    }

    @Override
    @Cacheable(key = "targetClass + methodName")
    public Map<String, Integer> statOperatingSystem() {
        Map<String,Integer> result=new HashMap<>();
        List<String> operatingSystems=baseMapper.selectAllOperatingSystem();
        for (String operatingSystem : operatingSystems) {
            result.merge(operatingSystem, 1, Integer::sum);
        }
        return result;
    }

    @Override
    @Cacheable(key = "targetClass + methodName")
    public List<Map<String,Integer>> statCity() {
        return baseMapper.selectCityCount();
    }

    @Override
    @Async
    public void addLog(Log log) {
        if(Objects.isNull(log.getIp()))return;
        save(log);
    }
}
