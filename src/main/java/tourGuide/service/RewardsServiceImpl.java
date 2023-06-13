package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tourGuide.domain.User;
import tourGuide.domain.UserReward;
import tourGuide.repositories.RewardCentralRepository;

import java.util.List;
import java.util.concurrent.*;

/**
 * Service implementation for Rewards.
 *
 * @author Antoine Lanselle
 */
@Service
public class RewardsServiceImpl implements RewardsService {

    private Logger logger = LoggerFactory.getLogger(RewardsServiceImpl.class);

    @Autowired
    private RewardCentralRepository rewardsCentral;
    @Autowired
    private GpsUtilService gpsUtil;

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    // proximity in miles
    private int defaultProximityBuffer = 10;
    private int proximityBuffer = defaultProximityBuffer;
    private int attractionProximityRange = 200;
    private final ExecutorService executorService = Executors.newFixedThreadPool(350);

    @Override
    public void calculateRewards(User user) {
        logger.atInfo().log("Calculate rewards of user " + user.getUserName());

        CopyOnWriteArrayList<VisitedLocation> userLocations = new CopyOnWriteArrayList<>(user.getVisitedLocations());
        List<Attraction> attractions = gpsUtil.getAttractions();

        CopyOnWriteArrayList<CompletableFuture> futures = new CopyOnWriteArrayList<>();

        for(VisitedLocation visitedLocation : userLocations) {
            for (Attraction attr : attractions) {
                futures.add(
                        CompletableFuture.runAsync(()-> {
                            if(user.getUserRewards().stream().filter(r -> r.attraction.attractionName.equals(attr.attractionName)).count() == 0) {
                                if(nearAttraction(visitedLocation, attr)) {
                                    logger.atInfo().log("Add reward to: " + user.getUserName() + " for attraction: " + attr.attractionName);
                                    user.addUserReward( new UserReward(visitedLocation, attr,  getRewardPoints(attr, user)));
                                }
                            }
                        },executorService)
                );
            }
        }
    }

    private boolean nearAttraction(VisitedLocation visitedLocation, Attraction attraction) {
        return getDistance(attraction, visitedLocation.location) > proximityBuffer ? false : true;
    }

    @Override
    public int getRewardPoints(Attraction attraction, User user) {
        return rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId());
    }

    @Override
    public double getDistance(Location loc1, Location loc2) {
        double lat1 = Math.toRadians(loc1.latitude);
        double lon1 = Math.toRadians(loc1.longitude);
        double lat2 = Math.toRadians(loc2.latitude);
        double lon2 = Math.toRadians(loc2.longitude);

        double angle = Math
                .acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

        double nauticalMiles = 60 * Math.toDegrees(angle);
        double statuteMiles = STATUTE_MILES_PER_NAUTICAL_MILE * nauticalMiles;
        return statuteMiles;
    }

}
