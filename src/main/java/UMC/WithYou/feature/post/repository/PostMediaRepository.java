package UMC.WithYou.feature.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import UMC.WithYou.feature.post.domain.PostMedia;

public interface PostMediaRepository extends JpaRepository<PostMedia, Long> {
}
