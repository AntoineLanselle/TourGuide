package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.repositories.GpsUtilRepository;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * Service implementation for GpsUtil.
 *
 * @author Antoine Lanselle
 */
@Service
public class GpsUtilServiceImpl implements GpsUtilService {

    private Logger logger = LoggerFactory.getLogger(GpsUtilServiceImpl.class);

    @Autowired
    private GpsUtilRepository gpsUtil;

    /**
     * Returns a list of all attractions in repository.
     *
     * @return a List of all attractions.
     */
    @Override
    public List<Attraction> getAttractions() {
        logger.info("Get all attractions from GpsUtilRepository");
        return gpsUtil.getAttractions();
    }

    /**
     * Returns the location of the user.
     *
     * @return the last VisitedLocation of the user.
     */
    @Override
    public VisitedLocation getUserLocation(UUID userId) {
        logger.info("Get location of user: " + userId.toString() + " from gpsUtilRepository");
        Locale.setDefault(Locale.ENGLISH);
        return gpsUtil.getUserLocation(userId);
    }

}
