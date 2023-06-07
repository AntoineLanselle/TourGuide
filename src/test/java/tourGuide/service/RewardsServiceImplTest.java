package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.domain.User;
import tourGuide.repositories.RewardCentralRepository;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
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


        // WHEN


        // THEN

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
