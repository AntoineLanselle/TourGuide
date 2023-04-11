package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tourGuide.DTO.TouristAttractionDetailsDTO;
import tourGuide.DTO.UserPreferencesDTO;
import tourGuide.domain.User;

import java.util.List;
import java.util.Map;

public interface TourGuideService {

    public List<TouristAttractionDetailsDTO> getNearbyAttractions(User user);

    public VisitedLocation trackUserLocation(User user);

    public VisitedLocation getLastVisitedLocation(User user);

}