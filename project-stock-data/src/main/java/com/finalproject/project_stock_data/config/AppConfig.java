package com.finalproject.project_stock_data.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.finalproject.project_stock_data.lib.RedisHelper;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class AppConfig {
  @Bean
  RestTemplate restTemplate(){
    return new RestTemplate();
  }

  @Bean
  ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  RedisHelper redisHelper(RedisConnectionFactory factory, ObjectMapper objectMapper) {
    return new RedisHelper(factory, objectMapper);
  }


}
