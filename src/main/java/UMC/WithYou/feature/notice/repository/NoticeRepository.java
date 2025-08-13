package UMC.WithYou.feature.notice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import UMC.WithYou.feature.notice.domain.Notice;


public interface NoticeRepository extends JpaRepository<Notice,Long> {
    
    @Query("""
        SELECT n 
        FROM Notice n 
        JOIN FETCH n.member 
        WHERE n.travel.id = :travelId
    """)
    List<Notice> findByTravelIdFetchJoinMember(@Param("travelId") Long travelId);
}
