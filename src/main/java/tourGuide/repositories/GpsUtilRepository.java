package tourGuide.repositories;

import gpsUtil.GpsUtil;
import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class GpsUtilRepository {

    private GpsUtil gpsUtil;
    private List<Attraction> attractionsList;

    public GpsUtilRepository() {
        gpsUtil = new GpsUtil();
        attractionsList = gpsUtil.getAttractions();
    }

    public List<Attraction> getAttractions() {
        return attractionsList;
    }

    public VisitedLocation getUserLocation(UUID userId) {
        return gpsUtil.getUserLocation(userId);
    }

}
