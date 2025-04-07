package UMC.WithYou.feature.cloud.domain;

import UMC.WithYou.common.BaseEntity;
import UMC.WithYou.feature.travel.domain.Travel;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.List;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Cloud extends BaseEntity {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="travel_id")
    private Travel travel;

    @OneToMany(mappedBy = "cloud",cascade = CascadeType.ALL)
    private List<CloudMedia> pictureDate;
}
