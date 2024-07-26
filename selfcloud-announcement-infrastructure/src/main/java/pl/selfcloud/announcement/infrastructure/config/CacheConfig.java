package pl.selfcloud.announcement.infrastructure.config;

import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.selfcloud.announcement.infrastructure.cache.CustomKeyGenerator;

@Configuration
@EnableCaching
public class CacheConfig extends CachingConfigurerSupport {

  @Bean("customKeyGenerator")
  public KeyGenerator keyGenerator() {
    return new CustomKeyGenerator();
  }

}