package com.example.cuckoolandback.user.domain;

import com.example.cuckoolandback.majority.common.domain.BaseTime;
import com.example.cuckoolandback.friend.domain.Friend;
import com.example.cuckoolandback.majority.dto.MajorRank;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private String memberId;

    private String nickname;

    private String password;

    //랭킹 관련
    private String mafiaWinNum;
    private int mafiaWinScore;
    private int mafiaTier;
    private int mafiaTotal;
    private String majorWinNum;
    private int majorWinScore;
    private int majorTier;
    private int majorTotal;

    private String memo;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @OneToMany(mappedBy = "response", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Friend> friendList;

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }
    public void updateMemo(String memo){
        this.memo=memo;
    }

    public void updateMajorScore(MajorRank majorRank) {
        String[] str=this.majorWinNum.split(" ");
        if (majorRank == MajorRank.FIRST) {
            this.majorWinScore += 3;

            int n=Integer.parseInt(str[0])+1;
            str[0]=String.valueOf(n);
            this.majorWinNum=String.join(" ",str);

        } else if (majorRank == MajorRank.SECOND) {
          this.majorWinScore+=2;
        }else if (majorRank == MajorRank.THIRD) {
            this.majorWinScore+=1;
        }else if (majorRank == MajorRank.LAST) {
            this.majorWinScore+=1;
            int n=Integer.parseInt(str[1])+1;
            str[1]=String.valueOf(n);
            this.majorWinNum=String.join(" ",str);
        }
        checkMajorTier();
    }
    public void checkMajorTier(){
        if(this.majorWinScore<=10){
            this.majorTier=0;
        } else if (this.majorWinScore<=20) {
            this.majorTier=1;
        } else if (this.majorWinScore<=40) {
            this.majorTier=2;
        }else {
            this.majorTier=3;
        }
    }


}
