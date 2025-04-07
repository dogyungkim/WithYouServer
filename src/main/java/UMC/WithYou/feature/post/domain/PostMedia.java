package UMC.WithYou.feature.post.domain;

import UMC.WithYou.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_media")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor

public class PostMedia extends BaseEntity implements Comparable<PostMedia>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    private int position;
    private String url;
    private String fineName;

    public PostMedia(Post post, int position, String url, String fineName){
        this.url = url;
        this.post = post;
        this.position = position;
        this.fineName = fineName;
    }


    public void setPosition(int position){
        this.position = position;
    }
    @Override
    public int compareTo(PostMedia postMedia) {
        return Integer.compare(this.getPosition(), postMedia.getPosition());
    }

}
