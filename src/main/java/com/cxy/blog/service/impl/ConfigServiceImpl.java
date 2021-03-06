package com.cxy.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cxy.blog.common.exception.BlogException;
import com.cxy.blog.common.util.JsonUtils;
import com.cxy.blog.mapper.ConfigMapper;
import com.cxy.blog.model.Config;
import com.cxy.blog.model.enums.ConfigTypeEnum;
import com.cxy.blog.service.ConfigService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

 
@Service
@CacheConfig(cacheNames = "config")
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper, Config> implements ConfigService {

    @Override
    @Cacheable(key = "targetClass + methodName")
    public Map<String, String> findAllGlobal() {
        Map<String, String> result = new HashMap<>();
        List<Config> configs = list(new QueryWrapper<Config>().eq("type", ConfigTypeEnum.GLOBAL_OPTION.getValue()));
        configs.forEach(variable -> result.put(variable.getName(),variable.getValue()));
        return result;
    }

    @Override
    @CacheEvict(allEntries = true)
    public void clearCache() {
    }

    @Override
    @Cacheable(key = "#name")
    public String findByName(String name) {
        return baseMapper.selectConfigByName(name);
    }

    @Override
    @Cacheable(key = "#name + #clazz")
    public <T> T getConfigObject(String name, Class<T> clazz) {
        String value = findByName(name);
        if(StringUtils.isNotBlank(value)){
            return JsonUtils.fromJson(value, clazz);
        }
        try {
            return clazz.newInstance();
        } catch (Exception e) {
            throw new BlogException("获取参数失败");
        }
    }
}
