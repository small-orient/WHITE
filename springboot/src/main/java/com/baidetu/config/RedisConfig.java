package com.baidetu.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.net.UnknownHostException;

/**
 * Redis配置key与value的类型序列化
 */
@Configuration
public class RedisConfig {
    @Bean
    @SuppressWarnings("all")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)
            throws UnknownHostException {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        //自定义json序列化配置
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
        template.setKeySerializer(serializer);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);

        //String序列化配置
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();

        //key使用string序列化，因为是<String, Object>
        template.setKeySerializer(stringRedisSerializer);
        //hash类型的key也采用string
        template.setHashKeySerializer(stringRedisSerializer);
        //String值value使用json序列化
        template.setValueSerializer(serializer);
        //hash的value也是用json
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();//设置配置进去

        return template;
    }
}
