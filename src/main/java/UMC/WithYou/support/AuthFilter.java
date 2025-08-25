package UMC.WithYou.support;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import UMC.WithYou.feature.auth.service.TokenProvider;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthFilter extends OncePerRequestFilter {
    private final TokenProvider tokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String jwtToken = tokenProvider.resolveToken(request);
        // 테스트 용 슈퍼 토큰
        if ("SUPER_DUPER_ADMIN_TOKEN".equals(jwtToken)){
            UserDetails ud = userDetailsService.loadUserByUsername("4310760394");
            Authentication authentication = new UsernamePasswordAuthenticationToken(ud,"test-id-1",ud.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (jwtToken != null){
            try{
                String payload = tokenProvider.parsePayload(jwtToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(payload);
                Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,payload, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e){
                SecurityContextHolder.clearContext();
                request.setAttribute("exceot", jwtToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
