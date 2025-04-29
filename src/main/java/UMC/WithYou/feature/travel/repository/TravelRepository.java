package UMC.WithYou.feature.travel.repository;

import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.travel.domain.Travel;
import UMC.WithYou.feature.travel.domain.Traveler;
import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {
    Optional<Travel> findByInvitationCode(String invitationCode);

   @Query("SELECT t FROM Traveler t JOIN FETCH t.travel WHERE t.member = :member")
   List<Traveler> findAllWithTravelerByMember(@Param("member") Member member);

   @Query("SELECT DISTINCT t FROM Travel t JOIN FETCH t.travelers tr WHERE tr.member = :member")
    List<Travel> findTravelsByMemberWithFetch(@Param("member") Member member);
}
