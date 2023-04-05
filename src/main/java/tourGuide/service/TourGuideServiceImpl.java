package tourGuide.service;

import com.google.common.collect.ComparisonChain;
import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.DTO.TouristAttractionDetailsDTO;
import tourGuide.domain.User;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TourGuideServiceImpl implements TourGuideService {

    // TODO private Logger logger = LoggerFactory.getLogger(TourGuideServiceImpl.class);

    @Autowired
    private GpsUtilService gpsUtilService;
    @Autowired
    private RewardsService rewardsService;

    @Override
    public List<TouristAttractionDetailsDTO> getFiveClosestAttractions(User user) {
        List<TouristAttractionDetailsDTO> attractionDistance = new ArrayList<>();
        Location userLocation = getLastVisitedLocation(user).location;
        for (Attraction attraction : gpsUtilService.getAttractions()) {
            Location attractionLocation = new Location(attraction.latitude, attraction.longitude);
            TouristAttractionDetailsDTO attractionDetailsDTO = new TouristAttractionDetailsDTO(
                    attraction.attractionName,
                    attractionLocation,
                    userLocation,
                    rewardsService.getDistance(userLocation, attractionLocation),
                    rewardsService.getRewardPoints(attraction, user));
            attractionDistance.add(attractionDetailsDTO);
        }

        Collections.sort(attractionDistance, (a1, a2) -> ComparisonChain.start()
                .compare(a2.getDistance(), a1.getDistance()).result());
        return attractionDistance.subList(0, 5);
    }

    @Override
    public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation) {
        List<Attraction> nearbyAttractions = new ArrayList<>();
        for (Attraction attraction : gpsUtilService.getAttractions()) {
            if (rewardsService.isWithinAttractionProximity(attraction, visitedLocation.location)) {
                nearbyAttractions.add(attraction);
            }
        }

        return nearbyAttractions;
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
