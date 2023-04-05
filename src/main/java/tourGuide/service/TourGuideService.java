package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import tourGuide.DTO.TouristAttractionDetailsDTO;
import tourGuide.domain.User;

import java.util.List;
import java.util.Map;

public interface TourGuideService {

    public List<TouristAttractionDetailsDTO> getFiveClosestAttractions(User user);

    public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation);

    public VisitedLocation trackUserLocation(User user);

    public VisitedLocation getLastVisitedLocation(User user);

}