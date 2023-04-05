package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;

import java.util.List;
import java.util.UUID;

public interface GpsUtilService {

    public List<Attraction> getAttractions();

    public VisitedLocation getUserLocation(UUID userId);

}
