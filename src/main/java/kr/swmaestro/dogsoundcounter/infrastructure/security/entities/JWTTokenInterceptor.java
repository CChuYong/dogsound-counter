package kr.swmaestro.dogsoundcounter.infrastructure.security.entities;

import com.google.common.net.HttpHeaders;
import kr.swmaestro.dogsoundcounter.infrastructure.security.TokenUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JWTTokenInterceptor extends OncePerRequestFilter {
    private final String TOKEN_PREFIX = "Bearer";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!request.getServletPath().startsWith("/auth")) {
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
                String token = authorizationHeader.substring(TOKEN_PREFIX.length());
                try {
                    if(TokenUtils.isValidToken(token)){
                        String username = TokenUtils.getUsernameFromToken(token);
                        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, null);
                        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }
                } catch (BadCredentialsException e) {
                    response.sendError(404);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
