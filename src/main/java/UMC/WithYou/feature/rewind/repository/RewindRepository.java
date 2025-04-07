package UMC.WithYou.feature.rewind.repository;

import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.rewind.domain.Rewind;
import UMC.WithYou.feature.travel.domain.Travel;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RewindRepository extends JpaRepository<Rewind, Long> {
    Optional<Rewind> findByTravelAndWriterAndDay(Travel travel, Member member, Integer day);
    List<Rewind> findAllByTravel(Travel travel);
    List<Rewind> findAllByTravelAndDay(Travel travel, Integer day);
}
