package UMC.WithYou.feature.cloud.repository;

import UMC.WithYou.feature.cloud.domain.Cloud;
import UMC.WithYou.feature.cloud.domain.CloudMedia;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CloudMediaRepository extends JpaRepository<CloudMedia,Long> {
    List<CloudMedia> findAllByCloud(Cloud cloud);
}
