package com.example.cuckoolandback.user.jwt;

import com.example.cuckoolandback.majority.common.Message;
import com.example.cuckoolandback.majority.common.util.HeaderUtil;
import com.example.cuckoolandback.user.domain.Member;
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

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

            String accessToken = HeaderUtil.getAccessToken(request);
            String refreshToken = HeaderUtil.getRefreshToken(request);

            if (jwtProvider.validateToken(accessToken)) {
                Authentication auth = jwtProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }// 엑세스 토큰이 만료
            else if (refreshToken != null) {
                /// 리프레시 토큰 검증
                boolean validateRefreshToken = jwtProvider.validateToken(refreshToken);
                boolean isRefreshToken = jwtProvider.existsRefreshToken(refreshToken);
                if (validateRefreshToken && isRefreshToken) {
                    /// 리프레시 토큰으로 멤버 정보 가져오기
                    Member member = jwtProvider.getMemberIdByToken(refreshToken);
                    /// 토큰 발급 후 헤더로 응답
                    TokenDto tokenDto = jwtProvider.generateTokenDto(member);
                    response.setHeader(Message.JWT_HEADER_NAME.getMsg(), "Bearer "+tokenDto.getAuthorization());
                    response.setHeader(Message.REFRESH_HEADER_NAME.getMsg(), tokenDto.getRefreshToken());
                    /// 컨텍스트 반영
                    Authentication auth = jwtProvider.getAuthentication(tokenDto.getRefreshToken());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        filterChain.doFilter(request, response);
    }
}
