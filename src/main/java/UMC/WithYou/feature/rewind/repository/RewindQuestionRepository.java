package UMC.WithYou.feature.rewind.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import UMC.WithYou.feature.rewind.domain.RewindQuestion;

public interface RewindQuestionRepository extends JpaRepository<RewindQuestion, Long> {
}
