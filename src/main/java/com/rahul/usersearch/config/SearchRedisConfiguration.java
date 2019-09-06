package com.rahul.usersearch.config;

import com.rahul.usersearch.model.UserListPage;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

/***
 * We are implementing redis cache on usersearch search service layer
 * The cache will store search result page for cachettl mins
 */
@Configuration
@EnableCaching
@ConditionalOnProperty(name = "spring.cache.type", havingValue = "redis")
public class SearchRedisConfiguration extends CachingConfigurerSupport {

    @Value("${cache.ttl.min}")
    private int cachettl;

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    /***
     * Jackson serializer deserializer for redis
     * @return
     */
    @Bean
    public CacheManager redisCacheManager() {
        RedisSerializationContext.SerializationPair<Object> jsonSerializer =
            RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer(
                UserListPage.class));
        return RedisCacheManager.RedisCacheManagerBuilder
            .fromConnectionFactory(redisConnectionFactory)
            .cacheDefaults(
                RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofMinutes(cachettl))
                    .serializeValuesWith(jsonSerializer)
            )
            .build();
    }

}
