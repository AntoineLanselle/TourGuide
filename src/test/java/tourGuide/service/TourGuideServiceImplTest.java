package tourGuide.service;

import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.domain.User;

import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TourGuideServiceImplTest {

    @InjectMocks
    private TourGuideServiceImpl tourGuideService;

    @Test
    @Disabled
    public void getLastVisitedLocation_ShouldReturnLastVisitedLocation() throws ExecutionException, InterruptedException {
        // GIVEN
        User userWithVisitedLocation = new User(UUID.randomUUID(), "userName0", "phone0", "email0");
        VisitedLocation visitedLocation0 = new VisitedLocation(userWithVisitedLocation.getUserId(), new Location(0.0,0.0), new Date());
        userWithVisitedLocation.addToVisitedLocations(visitedLocation0);

        User userWithNoVisitedLocation = new User(UUID.randomUUID(), "userName1", "phone1", "email1");
        VisitedLocation visitedLocation1 = new VisitedLocation(userWithNoVisitedLocation.getUserId(), new Location(10.0,10.0), new Date());
        when(tourGuideService.trackUserLocation(userWithNoVisitedLocation).get()).thenReturn(visitedLocation1);

        // WHEN
        VisitedLocation resultTestWithVisitedLocation = tourGuideService.getLastVisitedLocation(userWithVisitedLocation);
        VisitedLocation resultTestWithNoVisitedLocation = tourGuideService.getLastVisitedLocation(userWithNoVisitedLocation);

        // THEN
        assertTrue(resultTestWithVisitedLocation.location.latitude == visitedLocation0.location.latitude
                    && resultTestWithVisitedLocation.location.longitude == visitedLocation0.location.longitude);
        assertTrue(resultTestWithNoVisitedLocation.location.latitude == visitedLocation1.location.latitude
                    && resultTestWithNoVisitedLocation.location.longitude == visitedLocation1.location.longitude);
    }

    @Test
    @Disabled
    public void getAllCurrentLocations_() throws ExecutionException, InterruptedException {
        // GIVEN
        List<User> allUsers = new ArrayList<User>();
        User user0 = new User(UUID.randomUUID(), "userName0", "phone", "email");
        User user1 = new User(UUID.randomUUID(), "userName1", "phone", "email");
        User user2 = new User(UUID.randomUUID(), "userName2", "phone", "email");
        User user3 = new User(UUID.randomUUID(), "userName3", "phone", "email");
        User user4 = new User(UUID.randomUUID(), "userName4", "phone", "email");
        allUsers.add(user0);
        allUsers.add(user1);
        allUsers.add(user2);
        allUsers.add(user3);
        allUsers.add(user4);
        VisitedLocation visitedLocation = new VisitedLocation(UUID.randomUUID(), new Location(0.0,0.0), new Date());
        when(tourGuideService.getLastVisitedLocation(user0)).thenReturn(visitedLocation);

        // WHEN
        Map<String, Object> allCurrentLocations = tourGuideService.getAllCurrentLocations(allUsers);

        // THEN
        assertEquals(5, allCurrentLocations.size());
        for (Map.Entry map : allCurrentLocations.entrySet()) {
            assertTrue(allCurrentLocations.containsKey(map.getKey()));
        }
    }

}
