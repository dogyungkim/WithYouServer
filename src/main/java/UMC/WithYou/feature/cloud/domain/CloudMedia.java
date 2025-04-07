package UMC.WithYou.feature.cloud.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CloudMedia {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    //private List<String> url=new ArrayList<>();
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cloud_id")
    private Cloud cloud;

//    public void addUrl(String newUrl) {
//        this.url.add(newUrl);
//    }
//
//    public void deleteUrl(List<String> url){
//        this.url.removeAll(url);
//    }
}
