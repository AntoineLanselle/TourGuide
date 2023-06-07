package tourGuide.controllers;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tourGuide.DTO.TouristAttractionDetailsDTO;
import tourGuide.DTO.UserPreferencesDTO;
import tourGuide.domain.User;
import tourGuide.domain.UserReward;
import tourGuide.service.TourGuideService;
import tourGuide.service.TripPricerService;
import tourGuide.service.UserService;
import tripPricer.Provider;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TourGuideControllerTest {

    @Mock
    private UserService userService;
    @Mock
    private TourGuideService tourGuideService;
    @Mock
    private TripPricerService tripPricerService;
    @InjectMocks
    private TourGuideController tourGuideController;

    private List<User> users;

    @BeforeAll
    public void init() {
        users = new ArrayList<User>();
        User user0 = new User(UUID.randomUUID(), "userName0", "phone", "email");
        User user1 = new User(UUID.randomUUID(), "userName1", "phone", "email");
        User user2 = new User(UUID.randomUUID(), "userName2", "phone", "email");
        user0.addToVisitedLocations(new VisitedLocation(user0.getUserId(), new Location(1.1, 1.1), new Date()));
        user1.addToVisitedLocations(new VisitedLocation(user1.getUserId(), new Location(1.1, 1.1), new Date()));
        user2.addToVisitedLocations(new VisitedLocation(user2.getUserId(), new Location(1.1, 1.1), new Date()));
        users.add(user0);
        users.add(user1);
        users.add(user2);
    }

    @Test
    public void index_ShouldReturnString() {
        // GIVEN

        // WHEN
        String testResult = tourGuideController.index();

        // THEN
        assertEquals("Greetings from TourGuide!", testResult);
    }

    @Test
    public void getLocation_ShouldReturnLocationWithOKStatus() throws ExecutionException, InterruptedException {
        // GIVEN
        when(userService.getUser("userName0")).thenReturn(users.get(0));
        when(tourGuideService.getLastVisitedLocation(users.get(0))).thenReturn(users.get(0).getVisitedLocations().get(0));

        // WHEN
        ResponseEntity<Location> testResult = tourGuideController.getLocation("userName0");

        // THEN
        assertEquals(1.1, testResult.getBody().latitude);
        assertEquals(1.1, testResult.getBody().longitude);
        assertEquals(HttpStatus.OK, testResult.getStatusCode());
    }

    @Test
    public void getNearbyAttractions_ShouldReturnListOfTouristAttractionsDetailsDTOWithOKStatus() throws ExecutionException, InterruptedException {
        // GIVEN
        List<TouristAttractionDetailsDTO> nearbyAttractions = new ArrayList<TouristAttractionDetailsDTO>();
        nearbyAttractions.add(new TouristAttractionDetailsDTO("name", new Location(1.1, 1.1), new Location(1.1, 1.1), 0.0, 10));
        when(userService.getUser("userName0")).thenReturn(users.get(0));
        when(tourGuideService.getNearbyAttractions(users.get(0))).thenReturn(nearbyAttractions);

        // WHEN
        ResponseEntity<List<TouristAttractionDetailsDTO>> testResult = tourGuideController.getNearbyAttractions("userName0");

        // THEN
        assertEquals(1, testResult.getBody().size());
        assertEquals(nearbyAttractions.get(0), testResult.getBody().get(0));
        assertEquals(HttpStatus.OK, testResult.getStatusCode());
    }

    @Test
    public void getRewards_ShouldReturnListOfUserRewardWithOKStatus() {
        // GIVEN
        Attraction attraction = new Attraction("attractionName", "city", "state", 0.0, 0.0);
        UserReward userReward = new UserReward(users.get(0).getLastVisitedLocation(), attraction);
        users.get(0).getUserRewards().add(userReward);
        when(userService.getUser("userName0")).thenReturn(users.get(0));

        // WHEN
        ResponseEntity<List<UserReward>> testResult = tourGuideController.getRewards("userName0");

        // THEN
        assertEquals(1, testResult.getBody().size());
        assertEquals(userReward, testResult.getBody().get(0));
        assertEquals(HttpStatus.OK, testResult.getStatusCode());
    }

    @Test
    public void getAllCurrentLocations_ShouldReturnMapStringObjectWithOKStatus() throws ExecutionException, InterruptedException {
        // GIVEN
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(users.get(0).getUserName(), users.get(0).getLastVisitedLocation().location);
        map.put(users.get(1).getUserName(), users.get(1).getLastVisitedLocation().location);
        map.put(users.get(2).getUserName(), users.get(2).getLastVisitedLocation().location);
        when(userService.getAllUsers()).thenReturn(users);
        when(tourGuideService.getAllCurrentLocations(users)).thenReturn(map);

        // WHEN
        ResponseEntity<Map<String, Object>> testResult = tourGuideController.getAllCurrentLocations();

        // THEN
        assertEquals(map, testResult.getBody());
        assertEquals(HttpStatus.OK, testResult.getStatusCode());
    }

    @Test
    public void getTripDeals_ShouldReturnListOfProviderWithOKStatus() {
        // GIVEN
        List<Provider> deals = new ArrayList<Provider>();
        when(userService.getUser("userName0")).thenReturn(users.get(0));
        when(tripPricerService.getTripDeals(users.get(0))).thenReturn(deals);

        // WHEN
        ResponseEntity<List<Provider>> testResult = tourGuideController.getTripDeals("userName0");

        // THEN
        assertEquals(deals, testResult.getBody());
        assertEquals(HttpStatus.OK, testResult.getStatusCode());
    }

    @Test
    public void setUserPreferences_ShouldReturnOKStatus() {
        // GIVEN
        when(userService.getUser("userName0")).thenReturn(users.get(0));

        // WHEN
        ResponseEntity<List<Provider>> testResult = tourGuideController.setUserPreferences("userName0", new UserPreferencesDTO());

        // THEN
        assertEquals(HttpStatus.OK, testResult.getStatusCode());
    }

    @Test
    public void getUserPreferences_ShouldReturnUserPreferencesDTOWithOKStatus() {
        // GIVEN
        UserPreferencesDTO userPreferencesDTO = new UserPreferencesDTO();
        when(userService.getUser("userName0")).thenReturn(users.get(0));
        when(userService.getUserPreferences(users.get(0))).thenReturn(userPreferencesDTO);

        // WHEN
        ResponseEntity<UserPreferencesDTO> testResult = tourGuideController.getUserPreferences("userName0");

        // THEN
        assertEquals(userPreferencesDTO, testResult.getBody());
        assertEquals(HttpStatus.OK, testResult.getStatusCode());
    }

}
