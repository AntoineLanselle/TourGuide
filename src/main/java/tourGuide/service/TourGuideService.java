package tourGuide.service;

import java.util.List;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import tourGuide.domain.User;
import tripPricer.Provider;

public interface TourGuideService {

	public VisitedLocation getLastUserLocation(User user);

	public List<Provider> getTripDeals(User user);

	public VisitedLocation trackUserLocation(User user);

	public List<Attraction> getNearByAttractions(VisitedLocation visitedLocation);

}