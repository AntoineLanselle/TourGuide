package tourGuide.service;

import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(TripPricerServiceImpl.class);

    @Autowired
    private TripPricerRepository tripPricer;
    private static final String tripPricerApiKey = "test-server-api-key";

    /**
     * Get the offers from trip pricer corresponding to the user's preferences but not its budget
     * by giving user's preferences.
     *
     * @param apiKey a String of the api keey.
     * @param attractionId the UUID of the attraction.
     * @param adults a int number of adult.
     * @param children a int number of children.
     * @param nightsStay a int number of night stay.
     * @param rewardsPoints a int number of rewards points.
     *
     * @return a list of providers which contains offers corresponding to the user's preferences but not its budget.
     */
    @Override
    public List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints) {
        logger.atInfo().log("Get offers from TripPricer corresponding to the user's preferences");
        return tripPricer.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints);
    }

    /**
     * The function gets the user's trip deals by calling the tripPricer's getPrice function with the user's id, number of
     * adults, number of children, trip duration, and cumulatative reward points
     *
     * @param user The User that contains the user's preferences and rewards.
     *
     * @return a list of providers which contains offers corresponding to the user's preferences.
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

        logger.atInfo().log("Filter trip deals within the user's budget");
        List<Provider> providersPrefPrice = providers
                .stream()
                .filter(c -> Money.of(BigDecimal.valueOf(c.price), userPreferences.getCurrency()).isGreaterThanOrEqualTo(userPreferences.getLowerPricePoint())
                        && Money.of(BigDecimal.valueOf(c.price), userPreferences.getCurrency()).isLessThanOrEqualTo(userPreferences.getHighPricePoint()))
                .collect(Collectors.toList());

        user.setTripDeals(providersPrefPrice);
        return providersPrefPrice;
    }

}
