package tourGuide.service;

import tourGuide.domain.User;
import tripPricer.Provider;

import java.util.List;
import java.util.UUID;

public interface TripPricerService {

    public List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints);

    public String getProviderName(String apiKey, int adults);

    public List<Provider> getTripDeals(User user);

}
