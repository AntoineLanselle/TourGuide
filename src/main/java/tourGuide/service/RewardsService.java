package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import tourGuide.domain.User;

import java.util.concurrent.CompletableFuture;

/**
 * Service Rewards.
 *
 * @author Antoine Lanselle
 */
public interface RewardsService {

    public void calculateRewards(User user);

    public int getRewardPoints(Attraction attraction, User user);

    public double getDistance(Location loc1, Location loc2);

}