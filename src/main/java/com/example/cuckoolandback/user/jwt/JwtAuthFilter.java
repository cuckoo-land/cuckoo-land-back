package com.example.cuckoolandback.user.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "";
    private final static String HEADER_REFRESH = "Refresh";
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        try {
            String accessToken = getAccessToken(request);
            if (jwtProvider.validateToken(accessToken)) {
                Authentication auth = jwtProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
            } else {
                request.setAttribute("INVALID_JWT", "INVALID_JWT");
            }
        } catch (ExpiredJwtException e) {
            request.setAttribute("EXPIRED_JWT", "EXPIRED_JWT");
            logger.error("Could not set user authentication in security context", e);
        } catch (NullPointerException e) {
            logger.error("Could not set user authentication in security context", e);
        }
        filterChain.doFilter(request, response);
    }

    public static String getAccessToken(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_AUTHORIZATION);

        if (headerValue == null) {
            return null;
        }
        if (headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

    public static String getRefreshToken(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_REFRESH);

        if (headerValue == null) {
            return null;
        }
        if (headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
        }
        return null;
    }

}
