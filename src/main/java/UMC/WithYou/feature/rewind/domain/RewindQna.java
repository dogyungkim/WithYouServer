package UMC.WithYou.feature.rewind.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class RewindQna {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rewind_id")
    private Rewind rewind;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rewind_question_id")
    private RewindQuestion rewindQuestion;

    @Column(length = 255)
    private String answer;

    public void setRewind(Rewind rewind) {
        if(this.rewind != null)
            rewind.getRewindQnaList().remove(this);
        this.rewind = rewind;
        rewind.getRewindQnaList().add(this);
    }

    public void updateRewindQna(String answer) {
        this.answer = answer;
    }

}
