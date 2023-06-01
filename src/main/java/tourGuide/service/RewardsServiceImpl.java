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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @Autowired
    private UserService userService;

    private static final double STATUTE_MILES_PER_NAUTICAL_MILE = 1.15077945;
    // proximity in miles
    private int defaultProximityBuffer = 10;
    private int proximityBuffer = defaultProximityBuffer;
    private int attractionProximityRange = 200;
    private final ExecutorService executorService = Executors.newFixedThreadPool(1000);

    @Override
    public void setProximityBuffer(int proximityBuffer) {
        this.proximityBuffer = proximityBuffer;
    }

    @Override
    public void setDefaultProximityBuffer() {
        proximityBuffer = defaultProximityBuffer;
    }

    @Override
    public void calculateRewards(User user) {
        //logger.atInfo().log("Calculating rewards of user " + user.getUserId());
        List<VisitedLocation> userLocations = user.getVisitedLocations();
        List<Attraction> attractions = gpsUtil.getAttractions();

        // pour chaque localisation de l utilisateur on regarde si chaque attractions etait proche ou non
        for (VisitedLocation visitedLocation : userLocations) {
            for (Attraction attraction : attractions) {
                // on regarde si la localisation est proche de l attraction
                if (nearAttraction(visitedLocation, attraction)) {
                    //logger.atInfo().log("Visited location found for user " + user.getUserId() + ", " + attraction.attractionName);
                    user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)));
                }
            }
        }
    }
 /*       return CompletableFuture.runAsync(() -> {
            for (VisitedLocation visitedLocation : userLocations) {
                attractions.stream().filter(attraction -> nearAttraction(visitedLocation, attraction))
                        .forEach(attraction ->
                                user.addUserReward(new UserReward(visitedLocation, attraction, getRewardPoints(attraction, user)))
                        );
            }
        }, executorService);
    }
*/
/*
    //TODO faire appelle à cette methode pour le test de performance
    @Override
    public List<UserReward> calculateAllRewards() {
        List<Attraction> attractions = gpsUtil.getAttractions();
        List<UserReward> result = new ArrayList<>();

        List<CompletableFuture<UserReward>> allUsersRewardsFutures =
                userService.getAllUsers().stream().map(
                        user -> CompletableFuture.supplyAsync( () -> {
                            calculateRewards(user);
                        })
                );
        return result;
    }*/

    @Override
    public boolean isWithinAttractionProximity(Attraction attraction, Location location) {
        return getDistance(attraction, location) > attractionProximityRange ? false : true;
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
