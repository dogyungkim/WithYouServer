package UMC.WithYou.feature.travel.domain;

import UMC.WithYou.common.BaseEntity;
import UMC.WithYou.feature.member.domain.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Traveler extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;


    @ManyToOne(fetch = FetchType.LAZY)
    private Travel travel;

    public Traveler(Travel travel, Member member){
        this.member = member;
        this.travel = travel;
    }
    public boolean isMember(Member member){
        return this.member.isSameId(member.getId());
    }
}
