package UMC.WithYou.feature.travel.repository;

import UMC.WithYou.feature.travel.domain.Travel;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TravelRepository extends JpaRepository<Travel, Long> {
    Optional<Travel> findByInvitationCode(String invitationCode);

}
