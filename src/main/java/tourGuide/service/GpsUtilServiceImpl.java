package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.repositories.GpsUtilRepository;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Service
public class GpsUtilServiceImpl implements GpsUtilService {

    @Autowired
    private GpsUtilRepository gpsUtil;

    @Override
    public List<Attraction> getAttractions() {
        return gpsUtil.getAttractions();
    }

    @Override
    public VisitedLocation getUserLocation(UUID userId) {
        Locale.setDefault(Locale.ENGLISH);
        return gpsUtil.getUserLocation(userId);
    }

}
