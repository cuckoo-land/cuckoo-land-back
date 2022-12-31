package com.example.cuckoolandback.user.service;

import com.example.cuckoolandback.majority.common.Message;
import com.example.cuckoolandback.majority.common.exception.CustomException;
import com.example.cuckoolandback.majority.common.exception.ErrorCode;
import com.example.cuckoolandback.user.domain.Member;
import com.example.cuckoolandback.user.domain.RefreshToken;
import com.example.cuckoolandback.user.domain.RoleType;
import com.example.cuckoolandback.user.domain.UserDetailsImpl;
import com.example.cuckoolandback.user.dto.IdRequestDto;
import com.example.cuckoolandback.user.dto.LoginRequestDto;
import com.example.cuckoolandback.user.dto.MemberResponseDto;
import com.example.cuckoolandback.user.dto.MemoRequestDto;
import com.example.cuckoolandback.user.dto.NickRequestDto;
import com.example.cuckoolandback.user.dto.RegisterRequestDto;
import com.example.cuckoolandback.user.jwt.JwtProvider;
import com.example.cuckoolandback.user.jwt.TokenDto;
import com.example.cuckoolandback.user.repository.MemberRepository;
import com.example.cuckoolandback.user.repository.RefreshTokenRepository;
import java.util.Optional;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public MemberResponseDto register(RegisterRequestDto registerDto) {
        //이메일 중복
        dupleIdCheck(registerDto.getMemberId());
        //닉네임 중복
        dupleNickCheck(registerDto.getNickname());

        Member member = Member.builder()
                .memberId(registerDto.getMemberId())
                .nickname(registerDto.getNickname())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .roleType(RoleType.USER)
                .mafiaWinNum("0 0 0 0")
                .mafiaWinScore(0)
                .mafiaTier(1)
                .majorTotal(0)
                .majorWinNum("0 0")
                .majorWinScore(0)
                .majorTier(1)
                .majorTotal(0)
                .build();


        memberRepository.save(member);

        return MemberResponseDto.builder()
            .memberId(member.getMemberId())
            .nickname(member.getNickname())
            .roleType(member.getRoleType())
            .build();
    }

    private void dupleIdCheck(String memberId) {
        if (memberRepository.findByMemberId(memberId).isPresent()) {
            throw new CustomException(ErrorCode.DUPLE_ID);
        }
    }

    private void dupleNickCheck(String nickname) {
        if (memberRepository.findByNickname(nickname).isPresent()) {
            throw new CustomException(ErrorCode.DUPLE_NICKNAME);
        }
    }

    @Transactional
    public MemberResponseDto login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
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
            refreshTokenRepository.save(new RefreshToken(member, tokenDto.getRefreshToken()));
        } else {
            // DB에 refresh 토큰 업데이트
            refreshToken.get().updateToken(tokenDto.getRefreshToken());
        }
        response.addHeader(Message.JWT_HEADER_NAME.getMsg(),
            "Bearer " + tokenDto.getAuthorization());
        response.addHeader(Message.REFRESH_HEADER_NAME.getMsg(), tokenDto.getRefreshToken());

        return MemberResponseDto.builder()
            .memberId(member.getMemberId())
            .nickname(member.getNickname())
            .roleType(member.getRoleType())
            .build();
    }

    @Transactional
    public MemberResponseDto loginGuest(NickRequestDto nickRequestDto,
        HttpServletResponse response) {
        dupleNickCheck(nickRequestDto.getNickname());
        Member guest = Member.builder()
            .memberId("Guest" + System.currentTimeMillis())
            .nickname(nickRequestDto.getNickname())
            .password(passwordEncoder.encode("pwd" + UUID.randomUUID()))
            .roleType(RoleType.GUEST)
            .build();
        memberRepository.save(guest);
        TokenDto tokenDto = jwtProvider.generateTokenDto(guest);
        refreshTokenRepository.saveAndFlush(new RefreshToken(guest, tokenDto.getRefreshToken()));

        response.addHeader(Message.JWT_HEADER_NAME.getMsg(),
            "Bearer " + tokenDto.getAuthorization());
        response.addHeader(Message.REFRESH_HEADER_NAME.getMsg(), tokenDto.getRefreshToken());

        return MemberResponseDto.builder()
            .memberId(guest.getMemberId())
            .nickname(guest.getNickname())
            .roleType(guest.getRoleType())
            .build();
    }

    @Transactional
    public String logoutGuest() {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        memberRepository.deleteById(principal.getMember().getSeq());
        refreshTokenRepository.deleteByMemberId(principal.getMember().getMemberId());
        return Message.LOGOUT_SUCCESS.getMsg();
    }

    public String test(HttpServletRequest request) {
        return "접근가능";
    }

    @Transactional
    public String updateNickname(NickRequestDto requestDto) {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        Member member = memberRepository.findById(principal.getMember().getSeq())
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        member.updateNickname(requestDto.getNickname());
        memberRepository.save(member);
        return Message.UPDATE_NICKNAME_SUCCESS.getMsg();
    }
    @Transactional
    public String updateMemo(MemoRequestDto memoRequestDto) {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext()
            .getAuthentication().getPrincipal();
        Member member = memberRepository.findById(principal.getMember().getSeq())
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        member.updateMemo(memoRequestDto.getMemo());
        memberRepository.save(member);
        return Message.UPDATE_MEMO_SUCCESS.getMsg();
    }

}
