package pl.selfcloud.announcement.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude= {UserDetailsServiceAutoConfiguration.class})
@ComponentScan(basePackages = {
    "pl.selfcloud.announcement.web",
    "pl.selfcloud.announcement.domain",
    "pl.selfcloud.announcement.infrastructure",
    "pl.selfcloud.announcement.api"
})
@EnableJpaRepositories(basePackages = "pl.selfcloud.announcement.domain.repository")
@EntityScan(basePackages = "pl.selfcloud.announcement.domain.model")
@EnableDiscoveryClient
public class SelfcloudAnnouncementApplication {
  public static void main(String[] args) {
    SpringApplication.run(SelfcloudAnnouncementApplication.class, args);
  }
}
