package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import tourGuide.domain.User;

public interface RewardsService {
	
	public void setProximityBuffer(int proximityBuffer);

	public void setDefaultProximityBuffer();

	public void calculateRewards(User user);

	public boolean isWithinAttractionProximity(Attraction attraction, Location location);

	public double getDistance(Location loc1, Location loc2);

}