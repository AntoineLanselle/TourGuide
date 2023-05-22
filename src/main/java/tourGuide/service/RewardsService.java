package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import tourGuide.domain.User;

import java.util.concurrent.CompletableFuture;

public interface RewardsService {

    public void setProximityBuffer(int proximityBuffer);

    public void setDefaultProximityBuffer();

    public CompletableFuture<Void> calculateRewards(User user);

    public boolean isWithinAttractionProximity(Attraction attraction, Location location);

    public int getRewardPoints(Attraction attraction, User user);

    public double getDistance(Location loc1, Location loc2);

}