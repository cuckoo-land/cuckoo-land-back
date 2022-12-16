package com.example.cuckoolandback.majority.dto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MajorityRequestDto {
    public enum ROUND {
        THIRTYTWO, SIXTYFOUR
    }
    private MajorityRequestDto.ROUND round;

    //-1 이면 random, 아니면 설정한 gameId
    private long gameType;
    private long roomId;
}
