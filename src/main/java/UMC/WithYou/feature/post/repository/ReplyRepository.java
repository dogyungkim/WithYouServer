package UMC.WithYou.feature.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import UMC.WithYou.feature.post.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
