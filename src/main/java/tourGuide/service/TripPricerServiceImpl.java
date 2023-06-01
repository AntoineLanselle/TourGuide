package tourGuide.service;

import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.domain.User;
import tourGuide.domain.UserPreferences;
import tourGuide.repositories.TripPricerRepository;
import tripPricer.Provider;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service implementation for TripPricer.
 *
 * @author Antoine Lanselle
 */
@Service
public class TripPricerServiceImpl implements TripPricerService {

    // TODO private Logger logger = LoggerFactory.getLogger(TripPricerServiceImpl.class);

    @Autowired
    private TripPricerRepository tripPricer;
    private static final String tripPricerApiKey = "test-server-api-key";

    @Override
    public List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints) {
        return tripPricer.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints);
    }

    @Override
    public String getProviderName(String apiKey, int adults) {
        return tripPricer.getProviderName(apiKey, adults);
    }

    /**
     * The function gets the user's trip deals by calling the tripPricer's getPrice function with the user's id, number of
     * adults, number of children, trip duration, and cumulatative reward points
     *
     * @param user The user object that contains the user's preferences and rewards.
     * @return a list of providers.
     */
    @Override
    public List<Provider> getTripDeals(User user) {
        UserPreferences userPreferences = user.getUserPreferences();
        int cumulativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();

        List<Provider> providers = getPrice(
                tripPricerApiKey,
                user.getUserId(),
                userPreferences.getNumberOfAdults(),
                userPreferences.getNumberOfChildren(),
                userPreferences.getTripDuration(),
                cumulativeRewardPoints);

        List<Provider> providersPrefPrice = providers
                .stream()
                .filter(c -> Money.of(BigDecimal.valueOf(c.price), userPreferences.getCurrency()).isGreaterThanOrEqualTo(userPreferences.getLowerPricePoint())
                        && Money.of(BigDecimal.valueOf(c.price), userPreferences.getCurrency()).isLessThanOrEqualTo(userPreferences.getHighPricePoint()))
                .collect(Collectors.toList());

        user.setTripDeals(providersPrefPrice);
        return providersPrefPrice;
    }

}
