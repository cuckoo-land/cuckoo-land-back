package com.example.cuckoolandback.user.controller;

import com.example.cuckoolandback.user.dto.*;
import com.example.cuckoolandback.user.service.MemberService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {

    final MemberService memberService;

    @ApiOperation(value = "아이디 중복 체크")
    @PostMapping("/join/idcheck")
    public ResponseEntity<String> idCheck(@Valid @RequestBody IdRequestDto idRequestDto){
        return ResponseEntity.ok()
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(memberService.idCheck(idRequestDto));
    }

    @ApiOperation(value = "닉네임 중복 체크")
    @PostMapping("/join/nickcheck")
    public ResponseEntity<String> nickCheck(@Valid @RequestBody NickRequestDto nickRequestDto){
        return ResponseEntity.ok()
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(memberService.nickCheck(nickRequestDto));
    }

    @ApiOperation(value = "회원가입")
    @PostMapping("/join")
    public ResponseEntity<MemberResponseDto> register(@Valid @RequestBody RegisterRequestDto registerDto){
        return ResponseEntity.ok().body(memberService.register(registerDto));
    }

    @ApiOperation(value = "로그인")
    @PostMapping("/login")
    public ResponseEntity<MemberResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        MemberResponseDto responseDto = memberService.login(loginRequestDto,response);
        return ResponseEntity.ok().body(responseDto);
    }

    @ApiOperation(value = "게스트 로그인")
    @PostMapping("/guest/login")
    public ResponseEntity<MemberResponseDto> loginGuest(@Valid @RequestBody NickRequestDto nickRequestDto,HttpServletResponse response) {
        return ResponseEntity.ok().body(memberService.loginGuest(nickRequestDto,response));
    }

    @ApiOperation(value = "게스트 아웃")
    @DeleteMapping("/guest/out")
    public ResponseEntity<String> logoutGuest(HttpServletRequest request) {
        return ResponseEntity
                .ok()
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(memberService.logoutGuest(request));
    }

    @ApiOperation(value = "테스트")
    @GetMapping("/user/test")
    public ResponseEntity<String> test(HttpServletRequest request) {
        return ResponseEntity
                .ok()
                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
                .body(memberService.test(request));
    }

//    @ApiOperation(value = "토큰 재발급")
//    @GetMapping("/reissue")
//    public ResponseEntity<String> reissue(HttpServletRequest request) {
//        TokenDto tokenDto = memberService.reissue(request);
//        return ResponseEntity
//                .ok()
//                .header(Message.JWT_HEADER_NAME.getMsg(), tokenDto.getAccessToken())
//                .header(Message.REFRESH_HEADER_NAME.getMsg(), tokenDto.getRefreshToken())
//                .contentType(new MediaType("applicaton", "text", StandardCharsets.UTF_8))
//                .body(Message.REISSUE_COMPLETED_TOKEN.getMsg());
//    }
}
