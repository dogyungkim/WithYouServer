package UMC.WithYou.feature.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import UMC.WithYou.feature.post.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
