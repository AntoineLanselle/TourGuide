package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.DTO.TouristAttractionDetailsDTO;
import tourGuide.domain.User;

import java.util.*;

@Service
public class TourGuideServiceImpl implements TourGuideService {

    // TODO private Logger logger = LoggerFactory.getLogger(TourGuideServiceImpl.class);

    @Autowired
    private GpsUtilService gpsUtilService;
    @Autowired
    private RewardsService rewardsService;

    @Override
    public List<TouristAttractionDetailsDTO> getNearbyAttractions(User user) {
        List<TouristAttractionDetailsDTO> touristAttractionsDetailsList = new ArrayList<>();
        NavigableMap<Double, Attraction> nearbyAttractions = new TreeMap<>() ;
        Location userLocation = trackUserLocation(user).location;

        // on recupere toutes les attractions et on calcule leurs distance avec l utilisateur
        for (Attraction attraction : gpsUtilService.getAttractions()) {
            Location attractionLocation = new Location(attraction.latitude, attraction.longitude);
            nearbyAttractions.put(rewardsService.getDistance(userLocation, attractionLocation), attraction);
        }
        //on recupere les 5 plus proches et on créé un DTO pour chacun
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

    @Override
    public VisitedLocation trackUserLocation(User user) {
        VisitedLocation visitedLocation = gpsUtilService.getUserLocation(user.getUserId());
        user.addToVisitedLocations(visitedLocation);
        rewardsService.calculateRewards(user);
        return visitedLocation;
    }

    @Override
    public VisitedLocation getLastVisitedLocation(User user) {
        VisitedLocation visitedLocation = (user.getVisitedLocations().size() > 0) ? user.getLastVisitedLocation()
                : trackUserLocation(user);
        return visitedLocation;
    }

}
