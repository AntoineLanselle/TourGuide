package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tourGuide.DTO.TouristAttractionDetailsDTO;
import tourGuide.DTO.UserPreferencesDTO;
import tourGuide.domain.User;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Service for TourGuide.
 *
 * @author Antoine Lanselle
 */
public interface TourGuideService {

    public List<TouristAttractionDetailsDTO> getNearbyAttractions(User user) throws ExecutionException, InterruptedException;

    public CompletableFuture<VisitedLocation> trackUserLocation(User user);

    public VisitedLocation getLastVisitedLocation(User user) throws ExecutionException, InterruptedException;

    public Map<String, Object> getAllCurrentLocations(List<User> allUsers) throws ExecutionException, InterruptedException;

}