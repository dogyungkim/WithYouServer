package UMC.WithYou.feature.post.repository;

import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.post.domain.Post;
import UMC.WithYou.feature.post.domain.ScrapedPost;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapedPostRepository extends JpaRepository<ScrapedPost, Long> {

    Optional<ScrapedPost> findScrapedPostByMemberAndPost(Member member, Post post);

    List<ScrapedPost> findScrapedPostsByMember(Member member);
}
