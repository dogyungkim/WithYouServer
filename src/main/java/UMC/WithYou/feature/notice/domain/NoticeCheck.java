package UMC.WithYou.feature.notice.domain;


import UMC.WithYou.common.BaseEntity;
import UMC.WithYou.feature.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class NoticeCheck extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean isChecked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;    //체크한 사람

    @ManyToOne(fetch = FetchType.LAZY)
    private Notice notice;

    public void changeStatus() {
        if (!this.isChecked)
            this.isChecked=true;
        else
            this.isChecked=false;
    }

}

