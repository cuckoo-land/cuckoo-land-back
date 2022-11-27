package com.example.cuckoolandback.user.service;

import com.example.cuckoolandback.common.Message;
import com.example.cuckoolandback.common.exception.CustomException;
import com.example.cuckoolandback.common.exception.ErrorCode;
import com.example.cuckoolandback.user.domain.Member;
import com.example.cuckoolandback.user.domain.RoleType;
import com.example.cuckoolandback.user.domain.UserDetailsImpl;
import com.example.cuckoolandback.user.dto.*;
import com.example.cuckoolandback.user.jwt.JwtAuthFilter;
import com.example.cuckoolandback.user.jwt.JwtProvider;
import com.example.cuckoolandback.user.jwt.RefreshToken;
import com.example.cuckoolandback.user.jwt.TokenDto;
import com.example.cuckoolandback.user.repository.MemberRepository;
import com.example.cuckoolandback.user.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public String idCheck(IdRequestDto idRequestDto) {
        //중복 아이디 체크
        dupleIdCheck(idRequestDto.getMemberId());
        return Message.AVAILABLE_ID.getMsg();
    }
    public String nickCheck(NickRequestDto registerDto) {
        //중복 닉네임 체크
        dupleNickCheck(registerDto.getNickname());
        return Message.AVAILABLE_NICK.getMsg();
    }

    @Transactional
    public String register(RegisterRequestDto registerDto) {
        //이메일 중복
        dupleIdCheck(registerDto.getMemberId());
        //닉네임 중복
        dupleNickCheck(registerDto.getNickname());

        Member member = Member.builder()
                .memberId(registerDto.getMemberId())
                .nickname(registerDto.getNickname())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .roleType(RoleType.USER)
                .build();

        memberRepository.save(member);

        return Message.REGISTER_SUCCESS.getMsg();
    }

    private void dupleIdCheck(String memberId) {
        if(memberRepository.findByMemberId(memberId).isPresent()){
            throw new CustomException(ErrorCode.DUPLE_ID);
        }
    }

    private void dupleNickCheck(String nickname) {
        if(memberRepository.findByNickname(nickname).isPresent()){
            throw new CustomException(ErrorCode.DUPLE_NICKNAME);
        }
    }

    public TokenDto login(LoginRequestDto loginRequestDto) {
        // 가입 회원 여부 체크
        Member member = memberRepository.findByMemberId(loginRequestDto.getMemberId())
                .orElseThrow(() -> new CustomException(ErrorCode.CONFIRM_ID_PWD));

        // 비밀번호 체크
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.CONFIRM_ID_PWD);
        }
        // 엑세스 토큰과 리프레시 토큰 발급
        TokenDto tokenDto = jwtProvider.generateTokenDto(member);

        // userId refresh token 으로 DB 확인
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(member.getMemberId());
        if (refreshToken.isEmpty()) {
            // 없는 경우 새로 등록
            refreshTokenRepository.saveAndFlush(new RefreshToken(member.getMemberId(), tokenDto.getRefreshToken()));
        } else {
            // DB에 refresh 토큰 업데이트
            refreshToken.get().updateToken(tokenDto.getRefreshToken());
        }
        return tokenDto;
    }

    @Transactional
    public GuestResponseDto loginGuest() {
        Member guest = Member.builder()
                .memberId("Guest"+System.currentTimeMillis())
                .nickname("익명새"+System.currentTimeMillis())
                .password(passwordEncoder.encode("password"))
                .roleType(RoleType.GUEST)
                .build();
        memberRepository.save(guest);
        TokenDto tokenDto = jwtProvider.generateTokenDto(guest);
        refreshTokenRepository.saveAndFlush(new RefreshToken(guest.getMemberId(), tokenDto.getRefreshToken()));
        return GuestResponseDto.builder()
                .tokenDto(tokenDto)
                .memberId(guest.getMemberId())
                .nickname(guest.getNickname())
                .password("password")
                .build();
    }

    @Transactional
    public String logoutGuest(HttpServletRequest request) {
        String refreshToken = JwtAuthFilter.getRefreshToken(request);
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        memberRepository.deleteById(principal.getMember().getSeq());
        refreshTokenRepository.deleteByToken(refreshToken);
        return Message.LOGOUT_SUCCESS.getMsg();
    }

//    @Transactional
//    public TokenDto reissue(HttpServletRequest request) {
//
//    }
}
