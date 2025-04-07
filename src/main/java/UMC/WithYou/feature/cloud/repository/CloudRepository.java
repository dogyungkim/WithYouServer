package UMC.WithYou.feature.cloud.repository;

import UMC.WithYou.feature.cloud.domain.Cloud;
import UMC.WithYou.feature.travel.domain.Travel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CloudRepository extends JpaRepository<Cloud,Long> {
    Optional<Cloud> findByTravel(Travel travel);
}
