package tourGuide.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.domain.User;
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
    public void getProviderName_ShouldReturnStringOfProviderName() {
        // GIVEN
        when(tripPricerRepo.getProviderName("test-server-api-key", 1)).thenReturn("providerName");

        // WHEN
        String testResult = tripPricerService.getProviderName("test-server-api-key", 1);

        // THEN
        assertEquals("providerName", testResult);
    }

    /*
    @Test
    public void getTripDeals_ShouldReturnListOfProvider() {
        // GIVEN
        User user = new User(UUID.randomUUID(), "userName", "phone", "email");

        // WHEN
        List<Provider> providers = tripPricerService.getTripDeals(user);

        // THEN
        assertEquals(10, providers.size());
    }
*/
}
