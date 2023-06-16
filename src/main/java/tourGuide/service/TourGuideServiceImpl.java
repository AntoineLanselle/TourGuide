package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.DTO.TouristAttractionDetailsDTO;
import tourGuide.domain.User;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Service implementation for TourGuide.
 *
 * @author Antoine Lanselle
 */
@Service
public class TourGuideServiceImpl extends Thread implements TourGuideService  {

    private Logger logger = LoggerFactory.getLogger(TourGuideServiceImpl.class);

    @Autowired
    private GpsUtilService gpsUtilService;
    @Autowired
    private RewardsService rewardsService;

    private ExecutorService executorService = Executors.newFixedThreadPool(70);

    /**
     * Returns the 5 attractions closest to the user in parameter.
     *
     * @param user the User whose nearby attractions you want.
     *
     * @return a List of tourist attraction details DTO of the 5 nearest attractions.
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public List<TouristAttractionDetailsDTO> getNearbyAttractions(User user) throws ExecutionException, InterruptedException {
        List<TouristAttractionDetailsDTO> touristAttractionsDetailsList = new ArrayList<>();
        NavigableMap<Double, Attraction> nearbyAttractions = new TreeMap<>() ;
        Location userLocation = trackUserLocation(user).get().location;

        // we collect all the attractions and calculate their distance from the user
        for (Attraction attraction : gpsUtilService.getAttractions()) {
            Location attractionLocation = new Location(attraction.latitude, attraction.longitude);
            nearbyAttractions.put(rewardsService.getDistance(userLocation, attractionLocation), attraction);
        }

        logger.info("Calculation of the five nearest attractions to the user");
        // we collect the 5 nearest and create a DTO for each one
        for(int i = 0; i < 5; i++) {
            Map.Entry<Double, Attraction> attractionMap = nearbyAttractions.pollFirstEntry();
            Attraction attraction = attractionMap.getValue();
            Location attractionLocation = new Location(attraction.latitude, attraction.longitude);
            int rewardPoints = rewardsService.getRewardPoints(attraction, user);
            TouristAttractionDetailsDTO touristAttractionDetails = new TouristAttractionDetailsDTO(attraction.attractionName, attractionLocation, userLocation, attractionMap.getKey(), rewardPoints);
            touristAttractionsDetailsList.add(touristAttractionDetails);
        }
        return touristAttractionsDetailsList;
    }

    /**
     * Track the location of the user specified in parameter.
     *
     * @param user the User we want to track.
     *
     * @return CompletableFuture<VisitedLocation> the location of the user as a visited location.
     */
    @Override
    public CompletableFuture<VisitedLocation> trackUserLocation(User user) {
        logger.info("Track Location of user: " + user.getUserName());
        return CompletableFuture.supplyAsync(() -> gpsUtilService.getUserLocation(user.getUserId()), executorService)
                .thenApply(visitedLocation -> {
                    user.addToVisitedLocations(visitedLocation);
                    rewardsService.calculateRewards(user);
                    return visitedLocation;
                });
    }

    /**
     * Return the last visited location of the user specified in parameter.
     *
     * @param user the User we want the last visited location.
     *
     * @return a visitedLocation of the last location of the user.
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public VisitedLocation getLastVisitedLocation(User user) throws ExecutionException, InterruptedException {
        logger.info("Get last visited location of user: " + user.getUserName());
        VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation()
                : trackUserLocation(user).get();
        return visitedLocation;
    }

    /**
     * Return the location of all users in the list in parameter.
     *
     * @param allUsers a list of Users we want to have the current location.
     *
     * @return a Map<String, object> with the kay a String for user ID
     * and value an other Map<String, Double> the Location of the user.
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    @Override
    public Map<String, Object> getAllCurrentLocations(List<User> allUsers) throws ExecutionException, InterruptedException {
        logger.info("Search for the location of all users");
        Map<String, Object> currentLocations = new HashMap<String, Object>();
        for (User user : allUsers) {
            logger.info("Get current location of user: " + user.getUserName());
            Map<String, Double> userCoord = new HashMap<String, Double>();
            VisitedLocation userLocation = getLastVisitedLocation(user);
            userCoord.put("longitude", userLocation.location.longitude);
            userCoord.put("latitude", userLocation.location.latitude);
            currentLocations.put(user.getUserId().toString(), userCoord);
        }
        return currentLocations;
    }

}
