package tourGuide.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.domain.User;
import tourGuide.domain.UserPreferences;
import tourGuide.repositories.TripPricerRepository;
import tripPricer.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TripPricerServiceImplTest {

    @Mock
    private TripPricerRepository tripPricerRepo;
    @InjectMocks
    private TripPricerServiceImpl tripPricerService;

    @Test
    public void getPrice_ShouldReturnListOfProvider() {
        // GIVEN
        List<Provider> providers = new ArrayList<Provider>();
        UUID attractionId = UUID.randomUUID();
        when(tripPricerRepo.getPrice("test-server-api-key", attractionId, 1, 0, 1, 10)).thenReturn(providers);

        // WHEN
        List<Provider> testResult = tripPricerService.getPrice("test-server-api-key", attractionId, 1, 0, 1, 10);

        // THEN
        assertEquals(providers, testResult);
    }


    @Test
    public void getTripDeals_ShouldReturnListOfProvider() {
        // GIVEN
        List<Provider> tripDeals = new ArrayList<Provider>();
        tripDeals.add(new Provider(UUID.randomUUID(), "name0", 100.0));
        tripDeals.add(new Provider(UUID.randomUUID(), "name1", 150.0));
        tripDeals.add(new Provider(UUID.randomUUID(), "name2", 75.50));
        tripDeals.add(new Provider(UUID.randomUUID(), "name3", 195.68));
        tripDeals.add(new Provider(UUID.randomUUID(), "name4", 453.20));

        User user = new User(UUID.randomUUID(), "userName", "phone", "email");
        UserPreferences userPreferences = user.getUserPreferences();
        int cumulativeRewardPoints = user.getUserRewards().stream().mapToInt(i -> i.getRewardPoints()).sum();

        when(tripPricerService.getPrice(
                "test-server-api-key",
                user.getUserId(),
                userPreferences.getNumberOfAdults(),
                userPreferences.getNumberOfChildren(),
                userPreferences.getTripDuration(),
                cumulativeRewardPoints)
        ).thenReturn(tripDeals);

        // WHEN
        List<Provider> testResult = tripPricerService.getTripDeals(user);

        // THEN
        assertEquals(tripDeals, testResult);
    }

}
