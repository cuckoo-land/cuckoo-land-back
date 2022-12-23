package com.example.cuckoolandback.friend.service;

import com.example.cuckoolandback.majority.common.Message;
import com.example.cuckoolandback.majority.common.exception.CustomException;
import com.example.cuckoolandback.majority.common.exception.ErrorCode;
import com.example.cuckoolandback.friend.domain.Friend;
import com.example.cuckoolandback.friend.domain.FriendStatus;
import com.example.cuckoolandback.friend.dto.FriendRequestDto;
import com.example.cuckoolandback.friend.dto.FriendResponseDto;
import com.example.cuckoolandback.friend.repository.FriendRepository;
import com.example.cuckoolandback.user.domain.Member;
import com.example.cuckoolandback.user.domain.UserDetailsImpl;
import com.example.cuckoolandback.user.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

    public List<FriendResponseDto> getAllFriends() {
        List<FriendResponseDto> friendResponseDtoList = new ArrayList<>();
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Friend> friendList = friendRepository.findAllByRequest(principal.getMember());
        for (Friend friend : friendList) {
            FriendResponseDto friendResponseDto = FriendResponseDto.builder()
                    .friendName(friend.getResponse().getNickname())
                    .friendStatus(friend.getFriendStatus())
                    .build();
            friendResponseDtoList.add(friendResponseDto);
        }
        return friendResponseDtoList;

    }

    @Transactional
    public String requestFriend(FriendRequestDto friendRequestDto) {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member response = memberRepository.findByMemberId(friendRequestDto.getFriendId())
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_NOT_FOUND));
        if(friendRepository.findByRequestAndResponse(principal.getMember(), response).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_REQUESTD);
        }
        Friend friend = Friend.builder()
                .request(principal.getMember())
                .response(response)
                .friendStatus(FriendStatus.REQUESTED)
                .build();
        friendRepository.save(friend);
        return Message.REQUEST_SUCCESS.getMsg();
    }

    @Transactional
    public String deleteFriend(FriendRequestDto friendRequestDto) {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member response = memberRepository.findByMemberId(friendRequestDto.getFriendId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Friend friend = friendRepository.findByRequestAndResponse(principal.getMember(), response)
                .orElseThrow(() -> new CustomException(ErrorCode.FRIEND_NOT_FOUND));

        friendRepository.delete(friend);
        return Message.DELETE_SUCCESS.getMsg();
    }


}
