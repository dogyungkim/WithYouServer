package UMC.WithYou.feature.post.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import UMC.WithYou.feature.post.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
    @Query("select p from Post p where p.travel.id = :travelId")
    List<Post> findByTravelId(Long travelId);

}
