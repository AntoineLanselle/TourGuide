package tourGuide;

import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.time.StopWatch;
import org.junit.Ignore;

import gpsUtil.location.Attraction;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tourGuide.domain.User;
import tourGuide.service.*;

@SpringBootTest
public class PerformanceTest {

	/*
	 * A note on performance improvements:
	 *     
	 *     The number of users generated for the high volume tests can be easily adjusted via this method:
	 *     
	 *     		InternalTestHelper.setInternalUserNumber(100000);
	 *     
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
		stopWatch.start();

		List<User> allUsers = userService.getAllUsers();
		//int i = 0;

		for(User user : allUsers) {
			tourGuideService.trackUserLocation(user);
			//System.out.println("Utilisateur numéro " + i);
			//i++;
		}

		stopWatch.stop();

		System.out.println("highVolumeTrackLocation: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
		assertTrue(TimeUnit.MINUTES.toSeconds(15) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}

	@Test
	public void highVolumeGetRewards() throws InterruptedException {
		// Users should be incremented up to 100,000, and test finishes within 20 minutes
		StopWatch stopWatch = new StopWatch();
		stopWatch.start();
		
	    Attraction attraction = gpsUtilService.getAttractions().get(0);
		List<User> allUsers = userService.getAllUsers();

		allUsers.forEach(user -> user.addToVisitedLocations(new VisitedLocation(user.getUserId(), attraction, new Date())));
	    allUsers.forEach(user -> rewardsService.calculateRewards(user));
		int i=0;
		for(User user : allUsers) {
			i++;
			System.out.println("\n" + i + " user:" + user.getUserName() + "\nrewards:" + user.getUserRewards() + "\n");
			//assertTrue(user.getUserRewards().size() > 0);
		}
		stopWatch.stop();

		System.out.println("highVolumeGetRewards: Time Elapsed: " + TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()) + " seconds."); 
		assertTrue(TimeUnit.MINUTES.toSeconds(20) >= TimeUnit.MILLISECONDS.toSeconds(stopWatch.getTime()));
	}
	
}
