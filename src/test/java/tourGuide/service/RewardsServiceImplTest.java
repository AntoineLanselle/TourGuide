package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.w3c.dom.Attr;
import tourGuide.domain.User;
import tourGuide.domain.UserReward;
import tourGuide.repositories.RewardCentralRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RewardsServiceImplTest {

    @Mock
    private RewardCentralRepository rewardsCentral;
    @Mock
    private GpsUtilService gpsUtil;
    @InjectMocks
    private RewardsServiceImpl rewardsService;

    @Test
    public void calculateRewards_ShouldAddRewardToUser() {
        // GIVEN
        List<Attraction> attractions = new ArrayList<Attraction>();
        Attraction attraction0 = new Attraction("attraction0", "city0", "state0", 0.0, 0.0);
        Attraction attraction1 = new Attraction("attraction1", "city1", "state1", 0.0, 0.0);
        Attraction attractionTooFar = new Attraction("attraction2", "city2", "state2", 10.0, 10.0);
        Attraction attractionAlreadyVisited = new Attraction("attraction3", "city3", "state3", 0.0, 0.0);
        attractions.add(attraction0);
        attractions.add(attraction1);
        attractions.add(attractionTooFar);
        attractions.add(attractionAlreadyVisited);

        User user = new User(UUID.randomUUID(), "userName", "phone", "email");
        VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(), new Location(0.0,0.0), new Date());
        user.addToVisitedLocations(visitedLocation);
        user.addUserReward(new UserReward(visitedLocation, attractionAlreadyVisited));

        when(gpsUtil.getAttractions()).thenReturn(attractions);

        // WHEN
        rewardsService.calculateRewards(user);

        while (user.getUserRewards().size() < 3) {
            try {
                System.out.println("Loading...");
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
            }
        }

        // THEN
        List<String> results = new ArrayList<String>();
        results.add(user.getUserRewards().get(0).attraction.attractionName);
        results.add(user.getUserRewards().get(1).attraction.attractionName);
        results.add(user.getUserRewards().get(2).attraction.attractionName);

        assertTrue(user.getUserRewards().size() == 3);
        assertTrue(results.contains("attraction3"));
        assertTrue(results.contains("attraction1"));
        assertTrue(results.contains("attraction0"));
    }

    @Test
    public void getRewardPoints_ShouldReturnInt() {
        // GIVEN
        User user = new User(UUID.randomUUID(), "userName", "phone", "email");
        Attraction attraction = new Attraction("attractionName", "city", "state", 0.0, 0.0);
        when(rewardsCentral.getAttractionRewardPoints(attraction.attractionId, user.getUserId())).thenReturn(10);

        // WHEN
        int testResult = rewardsService.getRewardPoints(attraction, user);

        // THEN
        assertEquals(10, testResult);
    }

    @Test
    public void getDistance_ShouldReturnDistanceBetweenLocations() {
        // GIVEN
        Location firstLocation = new Location(0.0, 0.0);
        Location secondLocation = new Location(1.0, 1.0);

        // WHEN
        double testResult1 = rewardsService.getDistance(firstLocation, firstLocation);
        double testResult2 = rewardsService.getDistance(secondLocation, secondLocation);
        double testResult3 = rewardsService.getDistance(firstLocation, secondLocation);

        // THEN
        assertEquals(0.0, testResult1);
        assertEquals(0.0, testResult2);
        assertEquals(97.64439545235415, testResult3);
    }

}
