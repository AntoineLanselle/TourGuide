package tourGuide.service;

import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.domain.User;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TourGuideServiceImplTest {

    @Mock
    private GpsUtilService gpsUtilService;
    @Mock
    private RewardsService rewardsService;
    @InjectMocks
    private TourGuideServiceImpl tourGuideService;

    private User user;

    @BeforeAll
    public void init() {
        user = new User(UUID.randomUUID(), "userNme", "phone", "email");
    }
/*
	@Test
	public void getNearbyAttractions_() {
        // GIVEN
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

        // WHEN
        List<Attraction> attractions = tourGuideService.getNearByAttractions(visitedLocation);

        // THEN
		assertEquals(5, attractions.size());
	}
 */
	@Test
	public void trackUserLocation_() {
        // GIVEN

        // WHEN
        CompletableFuture<VisitedLocation> visitedLocation = tourGuideService.trackUserLocation(user);

        // THEN
		assertEquals(user.getUserId(), visitedLocation.userId);
	}
/*
	@Test
	public void getLastVisitedLocation_() {
        // GIVEN

        // WHEN
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

        // THEN
		assertTrue(visitedLocation.userId.equals(user.getUserId()));
	}

    @Test
	public void getAllCurrentLocations_() {
        // GIVEN

        // WHEN
        VisitedLocation visitedLocation = tourGuideService.trackUserLocation(user);

        // THEN
		assertTrue(visitedLocation.userId.equals(user.getUserId()));
	}
*/
}
