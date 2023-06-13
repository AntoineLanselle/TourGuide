package tourGuide.repositories;

import org.springframework.stereotype.Repository;
import tripPricer.Provider;
import tripPricer.TripPricer;

import java.util.List;
import java.util.UUID;

/**
 * TripPricer repository.
 *
 * @author Antoine Lanselle
 */
@Repository
public class TripPricerRepository {

    private TripPricer tripPricer;

    public TripPricerRepository() {
        this.tripPricer = new TripPricer();
    }

    public List<Provider> getPrice(String apiKey, UUID attractionId, int adults, int children, int nightsStay, int rewardsPoints) {
        return tripPricer.getPrice(apiKey, attractionId, adults, children, nightsStay, rewardsPoints);
    }

}
