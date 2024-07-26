package pl.selfcloud.announcement.infrastructure.config.security;


import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import pl.selfcloud.announcement.infrastructure.security.filter.DevFilter;

@Profile("dev")
@Configuration
@EnableMethodSecurity(securedEnabled = false)
public class DevSecurityConfig {

  private final static String[] developerAccess = {
      "/h2-console/**"
  };
  @Bean
  public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {

    http.authorizeHttpRequests(auth -> auth
            .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()
            .anyRequest().permitAll()
        )
        .exceptionHandling(auth -> auth.authenticationEntryPoint(
            new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterAfter(devFilter(), UsernamePasswordAuthenticationFilter.class)
    ;
    http.csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")
        .disable());
    http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
    return http.build();
  }

  @Bean
  public WebSecurityCustomizer webSecurityCustomizer() {
    return (web) -> web.ignoring()
        .requestMatchers(new AntPathRequestMatcher("/**"));
  }

  @Bean
  public DevFilter devFilter(){
    return new DevFilter();
  }

}
