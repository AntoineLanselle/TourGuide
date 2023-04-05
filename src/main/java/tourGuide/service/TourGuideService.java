package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tourGuide.domain.User;

import java.util.List;

public interface TourGuideService {

    public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation);

    public VisitedLocation trackUserLocation(User user);

    public VisitedLocation getLastVisitedLocation(User user);

}