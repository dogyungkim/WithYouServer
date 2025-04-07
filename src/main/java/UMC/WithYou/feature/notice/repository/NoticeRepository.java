package UMC.WithYou.feature.notice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import UMC.WithYou.feature.notice.domain.Notice;


public interface NoticeRepository extends JpaRepository<Notice,Long> {
}
