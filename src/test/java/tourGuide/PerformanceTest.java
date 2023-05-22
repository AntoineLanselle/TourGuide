package tourGuide;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tourGuide.domain.User;
import tourGuide.service.*;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
	public void highVolumeTrackLocation() {
		// Users should be incremented up to 100,000, and test finishes within 15 minutes
		StopWatch stopWatch = new StopWatch();
		List<User> allUsers = userService.getAllUsers();

		stopWatch.start();
		allUsers.forEach(user -> tourGuideService.trackUserLocation(user));
		stopWatch.stop();

		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

	@Test
	public void highVolumeGetRewards() {
		// Users should be incremented up to 100,000, and test finishes within 20 minutes
		StopWatch stopWatch = new StopWatch();
		List<User> allUsers = userService.getAllUsers();
		Attraction attraction = gpsUtilService.getAttractions().get(0);

		//@Async List<CompletableFuture<Void>> tasksFutures = new ArrayList<>();
		for(User user : allUsers) {
			user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date()));
			//@Async tasksFutures.add(rewardsService.calculateRewards(user));
		}

		stopWatch.start();
		allUsers.forEach(user -> {rewardsService.calculateRewards(user); System.out.println(user.getUserName());});
		//@Async CompletableFuture<Void> allFutures = CompletableFuture.allOf(tasksFutures.toArray(new CompletableFuture[0]));
		//@Async allFutures.join();
		stopWatch.stop();

		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds.");
		allUsers.forEach(user -> {
			assertNotEquals(0, user.getUserRewards().size());
		});
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}
	
}
