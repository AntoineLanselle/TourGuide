package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import tourGuide.domain.User;
import tourGuide.domain.UserReward;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Service Rewards.
 *
 * @author Antoine Lanselle
 */
public interface RewardsService {

    public void setProximityBuffer(int proximityBuffer);

    public void setDefaultProximityBuffer();

    public void calculateRewards(User user);

    //public List<UserReward> calculateAllRewards();

    public boolean isWithinAttractionProximity(Attraction attraction, Location location);

    public int getRewardPoints(Attraction attraction, User user);

    public double getDistance(Location loc1, Location loc2);

}