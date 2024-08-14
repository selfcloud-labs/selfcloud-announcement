package pl.selfcloud.announcement.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.selfcloud.security.api.detail.CustomerDetails;

@Profile("dev")
public class DevFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(
      HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
      FilterChain filterChain) throws ServletException, IOException {

    CustomerDetails<Long, String> userData = new CustomerDetails<>(12345L, "developer@gmail.com");
    Authentication authentication = new UsernamePasswordAuthenticationToken(userData, null, null);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }
}
