package com.vishvendra.procart.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {

  @Value("${REDIS_HOST:localhost}")
  private String host;

  @Value("${REDIS_PORT:6379}")
  private int port;

  @Value("${REDIS_PASSWORD:}")
  private String password;

  @Value("${REDIS_USERNAME:}")
  private String username;

  @Value("${REDIS_TIMEOUT:5000}")
  private int timeout;

  @Bean
  public RedisConnectionFactory redisConnectionFactory() {
    RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
    redisStandaloneConfiguration.setHostName(host);
    redisStandaloneConfiguration.setUsername(username);
    redisStandaloneConfiguration.setPassword(password);
    redisStandaloneConfiguration.setPort(port);
    LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(
        redisStandaloneConfiguration);
    connectionFactory.start();
    return connectionFactory;

  }
}

