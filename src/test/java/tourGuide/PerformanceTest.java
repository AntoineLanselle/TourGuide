package tourGuide;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.domain.User;
import tourGuide.service.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PerformanceTest {

	/*
	 * A note on performance improvements:
	 *     
	 *     The number of users generated for the high volume tests can be easily adjusted via this method:
	 *     
	 *     		InternalTestHelper.setInternalUserNumber(100000);
	 *
	 *     These tests can be modified to suit new solutions, just as long as the performance metrics
	 *     at the end of the tests remains consistent. 
	 * 
	 *     These are performance metrics that we are trying to hit:
	 *     
	 *     highVolumeTrackLocation: 100,000 users within 15 minutes:
	 *     		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
     *
     *     highVolumeGetRewards: 100,000 users within 20 minutes:
	 *          assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	 */

	@Autowired
	private UserService userService;
	@Autowired
	private TourGuideService tourGuideService;
	@Autowired
	private GpsUtilService gpsUtilService;
	@Autowired
	private RewardsService rewardsService;

	@Test
	@Disabled
	public void highVolumeTrackLocation() {
		StopWatch stopWatch = new StopWatch();
		List<User> allUsers = userService.getAllUsers();

		stopWatch.start();

		for(User user : allUsers) {
			tourGuideService.trackUserLocation(user);
		}

		for(User user : allUsers) {
			while(user.getVisitedLocations().size() < 4) {
				try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
				}
			}
		}

		stopWatch.stop();

		for(User user: allUsers) {
			assertTrue( user.getVisitedLocations().get(3) != null);
		}
		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

	@Test
	@Disabled
	public void highVolumeGetRewards() {
		StopWatch stopWatch = new StopWatch();
		Attraction attraction = gpsUtilService.getAttractions().get(0);
		List<User> allUsers = userService.getAllUsers();
		allUsers.forEach(u -> u.addToVisitedLocations(new VisitedLocation(u.getUserId(), attraction, new Date())));

		stopWatch.start();

		allUsers.forEach(u -> rewardsService.calculateRewards(u));

		for(User user : allUsers) {
			while (user.getUserRewards().isEmpty()) {
				try {
					TimeUnit.MILLISECONDS.sleep(200);
				} catch (InterruptedException e) {
				}
			}
		}

		stopWatch.stop();

		for(User user : allUsers) {
			assertTrue(user.getUserRewards().size() > 0);
		}
		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

}
