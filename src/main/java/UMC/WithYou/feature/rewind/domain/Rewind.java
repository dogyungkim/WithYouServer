package UMC.WithYou.feature.rewind.domain;

import UMC.WithYou.common.BaseEntity;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.travel.domain.Travel;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Rewind extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    @Column
    private Integer day;

    @Column
    private Long mvpCandidateId;

    @Enumerated(EnumType.STRING)
    @Column
    private Mood mood;

    @Column(length = 100)
    private String comment;

    @OneToMany(mappedBy="rewind", cascade = CascadeType.ALL)
    private List<RewindQna> rewindQnaList = new ArrayList<>();

    public void setTravel(Travel travel) {
        if(this.travel != null)
            travel.getRewinds().remove(this);
        this.travel = travel;
        travel.getRewinds().add(this);
    }

    public void setWriter(Member member) {
        this.writer = member;
    }

    public void updateRewind(Long mvpCandidateId, Mood mood, String comment) {
        if(mvpCandidateId != null) this.mvpCandidateId = mvpCandidateId;
        if(mood != null) this.mood = mood;
        if(comment != null) this.comment = comment;
    }

}
