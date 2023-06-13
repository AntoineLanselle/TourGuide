package tourGuide.service;

import tourGuide.domain.User;
import tripPricer.Provider;

import java.util.List;
import java.util.UUID;

/**
 * Service for TripPricer.
 *
 * @author Antoine Lanselle
 */
public interface TripPricerService {

    public List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints);

    public List<Provider> getTripDeals(User user);

}
