package UMC.WithYou.feature.packingItem.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import UMC.WithYou.feature.packingItem.domain.PackingItem;

@Repository
public interface PackingItemRepository extends JpaRepository<PackingItem, Long> {
    @Query("select p from PackingItem p where p.travel.id = :travelId")
    List<PackingItem> findByTravelId(Long travelId);
}
