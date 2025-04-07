package UMC.WithYou.feature.cloud.repository;

import UMC.WithYou.feature.cloud.domain.Cloud;

public interface CloudCustomRepository {
    Cloud findByTravelFetchJoinCloud(Long cloudId);
}
