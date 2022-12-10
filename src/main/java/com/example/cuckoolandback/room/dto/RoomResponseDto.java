package com.example.cuckoolandback.room.dto;

import com.example.cuckoolandback.room.domain.GameType;
import com.example.cuckoolandback.room.domain.RoomStatus;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;


@ApiModel(value = "게임 방 객체", description = "게임 룸 생성을 위한 객체")
@Getter
@Setter
@Builder
public class RoomResponseDto extends SpringDataWebProperties.Pageable {
    @ApiModelProperty(value="게임 방 아이디", example = "0", required = true)
    Long id;
    @ApiModelProperty(value="제목", example = "같이 게임 해요", required = true)
    String title;
    @ApiModelProperty(value="참여코드", example = "2f48f241-9d64-4d16-bf56-70b9d4e0e79a", required = true)
    String code;
    @ApiModelProperty(value="방장", example = "bird1", required = true)
    String hostId;
    @ApiModelProperty(value="게임종류", example = "MAFIA", required = true)
    GameType type;
    @ApiModelProperty(value="공개유무", example = "true", required = true)
    boolean visibility;
    @ApiModelProperty(value="최대인원", example = "4", required = true)
    int maximum;
    @ApiModelProperty(value="현재인원", example = "1", required = true)
    int numOfPeople;
    @ApiModelProperty(value="게임상태", example = "WAITING", required = true)
    RoomStatus state;
}
