package pl.selfcloud.announcement.infrastructure.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.selfcloud.announcement.infrastructure.security.util.Utilities;
import pl.selfcloud.security.api.detail.CustomerDetails;

public class JWTVerifierFilter extends OncePerRequestFilter {


  @Override
  protected void doFilterInternal(
      HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
      FilterChain filterChain) throws ServletException, IOException {

    String authHeader = httpServletRequest.getHeader("Authorization");
    if(!Utilities.validString(authHeader) || !authHeader.startsWith("Bearer ")) {
      filterChain.doFilter(httpServletRequest, httpServletResponse);
      return;
    }

    String username = httpServletRequest.getHeader("username");
    String authoritiesStr = httpServletRequest.getHeader("authorities");
    Long userId = Long.parseLong(httpServletRequest.getHeader("userId"));

    Set<SimpleGrantedAuthority> simpleGrantedAuthorities = getAuthorities(authoritiesStr);
    CustomerDetails<Long, String> userData = new CustomerDetails<>(userId, username);
    Authentication authentication = new UsernamePasswordAuthenticationToken(userData, null, simpleGrantedAuthorities);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(httpServletRequest, httpServletResponse);
  }

  private Set<SimpleGrantedAuthority> getAuthorities(String authorities){
    Set<SimpleGrantedAuthority> simpleGrantedAuthorities = new HashSet<>();
    if(Utilities.validString(authorities)) {
      authorities = authorities.replace("[","");
      authorities = authorities.replace("]","");
      authorities = authorities.replace(" ","");
      simpleGrantedAuthorities= Arrays.stream(authorities.split(",")).distinct()
          .filter(Utilities::validString).map(SimpleGrantedAuthority::new).collect(Collectors.toSet());;
    }
    return simpleGrantedAuthorities;
  }

}
