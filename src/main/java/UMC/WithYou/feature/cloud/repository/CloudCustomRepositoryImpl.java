package UMC.WithYou.feature.cloud.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import UMC.WithYou.feature.cloud.domain.Cloud;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import static UMC.WithYou.feature.cloud.domain.QCloud.cloud;
import static UMC.WithYou.feature.cloud.domain.QCloudMedia.cloudMedia;

@Repository
@AllArgsConstructor
public class CloudCustomRepositoryImpl implements CloudCustomRepository{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Cloud findByTravelFetchJoinCloud(Long cloudId){
        return jpaQueryFactory
                .selectFrom(cloud)
                .where(cloud.id.eq(cloudId))
                .leftJoin(cloud.pictureDate, cloudMedia).fetchJoin()
                .fetchOne();
    }

}
