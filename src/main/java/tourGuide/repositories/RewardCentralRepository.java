package tourGuide.repositories;

import org.springframework.stereotype.Repository;
import rewardCentral.RewardCentral;

import java.util.UUID;

/**
 * RewardCentral repository.
 *
 * @author Antoine Lanselle
 */
@Repository
public class RewardCentralRepository {

    private RewardCentral rewardCentral;

    public RewardCentralRepository() {
        this.rewardCentral = new RewardCentral();
    }

    public int getAttractionRewardPoints(UUID attractionId, UUID userId) {
        return rewardCentral.getAttractionRewardPoints(attractionId, userId);
    }

}
