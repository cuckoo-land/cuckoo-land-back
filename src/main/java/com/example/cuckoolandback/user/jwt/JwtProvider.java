package com.example.cuckoolandback.user.jwt;

import com.example.cuckoolandback.common.exception.CustomException;
import com.example.cuckoolandback.common.exception.ErrorCode;
import com.example.cuckoolandback.user.domain.Member;
import com.example.cuckoolandback.user.repository.MemberRepository;
import com.example.cuckoolandback.user.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final UserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberRepository memberRepository;

    @Value("${jwt.secret}")
    private String JWT_SECRET;
    private Key key;
    private static final String AUTHORITIES_KEY = "role";
    @PostConstruct
    protected void keyInit() {
        key = Keys.hmacShaKeyFor(JWT_SECRET.getBytes());
    }

    public TokenDto generateTokenDto(Member member) {
        long now = new Date().getTime();

        int ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 60분
        String accessToken = Jwts.builder()
                .setSubject(member.getMemberId())
                .claim(AUTHORITIES_KEY,member.getRoleType())
                .setIssuedAt(new Date())
                .setExpiration(new Date(now + ACCESS_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();


        int REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7; // 1주일
        String refreshToken = Jwts.builder()
                .setSubject(member.getMemberId())
                .claim(AUTHORITIES_KEY,member.getRoleType())
                .setIssuedAt(new Date())
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return new TokenDto(accessToken, refreshToken);
    }
    public Boolean validateToken(String token) throws ExpiredJwtException {
        return this.getTokenClaims(token) != null;
    }

    public Claims getTokenClaims(String token){
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        } catch(ExpiredJwtException ex){
            log.error("JWT claims is expired");
        } catch(NullPointerException ex){
            log.error("JWT claims is null");
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        Claims claims = this.getTokenClaims(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(claims.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public boolean existsRefreshToken(String refreshToken) {
        return refreshTokenRepository.existsByToken(refreshToken);
    }
    public Member getMemberIdByToken(String token) {
        String memberId = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        return memberRepository.findByMemberId(memberId).orElseThrow(
                ()-> new CustomException(ErrorCode.USER_NOT_FOUND)
        );
    }
}
