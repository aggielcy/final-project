package com.finalproject.project_stock_data.lib;

import java.time.Duration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import tools.jackson.databind.ObjectMapper;

public class RedisHelper {

  private RedisTemplate<String, String> redisTemplate;
  private ObjectMapper objectMapper;

  // new RedisHelper().get("tutor")
  // new RedisHelper().set("tutor", "vincent", Duration.Minute)
  public RedisHelper(RedisConnectionFactory factory,
      ObjectMapper objectMapper) {
    RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(factory);
    redisTemplate.setKeySerializer(RedisSerializer.string());
    redisTemplate.setValueSerializer(RedisSerializer.json());
    redisTemplate.afterPropertiesSet();
    this.redisTemplate = redisTemplate;
    this.objectMapper = objectMapper;
  }

  // LineStationEntity[] lineStationEntities =
  // this.objectMapper.readValue(stationsJson, LineStationEntity[].class);

  public <T> T get(String key, Class<T> valueType) {
    // Step 1: redisTemplate read redis
    String json = this.redisTemplate.opsForValue().get(key);
    if (json == null)
      return null;
    // Step 2: convert json string to target object type
    return this.objectMapper.readValue(json, valueType);
  }

  public void set(String key, Object object, Duration duration) {
    String json = this.objectMapper.writeValueAsString(object);
    this.redisTemplate.opsForValue().set(key, json, duration);
  }
}
