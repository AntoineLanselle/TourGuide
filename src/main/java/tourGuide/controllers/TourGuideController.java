package tourGuide.controllers;

import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tourGuide.DTO.TouristAttractionDetailsDTO;
import tourGuide.DTO.UserPreferencesDTO;
import tourGuide.domain.User;
import tourGuide.domain.UserReward;
import tourGuide.service.TourGuideService;
import tourGuide.service.TripPricerService;
import tourGuide.service.UserService;
import tripPricer.Provider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Controller for TourGuide.
 *
 * @author Antoine Lanselle
 */
@RestController
public class TourGuideController {

    private Logger logger = LoggerFactory.getLogger(TourGuideController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private TourGuideService tourGuideService;
    @Autowired
    private TripPricerService tripPricerService;

    /**
     * Returns a welcome message.
     *
     * @return String a message to welcome user.
     */
    @GetMapping("/")
    public String index() {
        logger.info("GET request - index");

        return "Greetings from TourGuide!";
    }

    /**
     * Returns the location of the user.
     *
     * @param userName String of the user name.
     * @return a ResponseEntity with status OK and a Location as body.
     */
    @GetMapping("/getLocation")
    public ResponseEntity<Location> getLocation(@RequestParam String userName) throws ExecutionException, InterruptedException {
        logger.info("GET request - getLocation of user: " + userName);

        VisitedLocation visitedLocation = tourGuideService.getLastVisitedLocation(userService.getUser(userName));
        return ResponseEntity.status(HttpStatus.OK).body(visitedLocation.location);
    }

    /**
     * Returns a list of attractions near the user.
     *
     * @param userName String of the user name.
     * @return a ResponseEntity with status OK and a list of nearby attractions as body.
     */
    @GetMapping("/getNearbyAttractions")
    public ResponseEntity<List<TouristAttractionDetailsDTO>> getNearbyAttractions(@RequestParam String userName) throws ExecutionException, InterruptedException {
        logger.info("GET request - getNearbyAttractions of user: " + userName);

        User user = userService.getUser(userName);
        List<TouristAttractionDetailsDTO> NearbyAttractions = tourGuideService.getNearbyAttractions(user);
        return ResponseEntity.status(HttpStatus.OK).body(NearbyAttractions);
    }

    /**
     * Returns the rewards that a user has earned.
     *
     * @param userName String of the user name.
     * @return a ResponseEntity with status OK and a list UserReward as body.
     */
    @GetMapping("/getRewards")
    public ResponseEntity<List<UserReward>> getRewards(@RequestParam String userName) {
        logger.info("GET request - getRewards of user: " + userName);

        User user = userService.getUser(userName);
        return ResponseEntity.status(HttpStatus.OK).body(user.getUserRewards());
    }

    /**
     * Returns all the current locations of all the tour guides.
     *
     * @return a ResponseEntity with status OK and a Map<String, Object> as body where the String is the userName and Object is the location.
     */
    @GetMapping("/getAllCurrentLocations")
    public ResponseEntity<Map<String, Object>> getAllCurrentLocations() throws ExecutionException, InterruptedException {
        logger.info("GET request - getAllCurrentLocations");

        List<User> allUsers = userService.getAllUsers();
        Map<String, Object> currentLocations = tourGuideService.getAllCurrentLocations(allUsers);
        return ResponseEntity.status(HttpStatus.OK).body(currentLocations);
    }

    /**
     * Returns a list of providers that have deals for the user.
     *
     * @param userName String of the user name.
     * @return a ResponseEntity with status OK and a list of provider as body.
     */
    @GetMapping("/getTripDeals")
    public ResponseEntity<List<Provider>> getTripDeals(@RequestParam String userName) {
        logger.info("GET request - getTripDeals for user: " + userName);

        List<Provider> providers = tripPricerService.getTripDeals(userService.getUser(userName));
        return ResponseEntity.status(HttpStatus.OK).body(providers);
    }

    /**
     * Update the user preferences for the given user.
     *
     * @param userName String of the user name.
     * @param userPreferencesDTO UserPreferencesDTO object that contains the user preferences.
     * @return a ResponseEntity with status OK.
     */
    @PutMapping("/setUserPreferences")
    public ResponseEntity setUserPreferences(@RequestParam String userName, @RequestBody UserPreferencesDTO userPreferencesDTO) {
        logger.info("PUT request - setUserPreferences of user: " + userName);

        userService.setUserPreferences(userService.getUser(userName), userPreferencesDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    /**
     * Get the user preferences for the given user.
     *
     * @param userName String of the user name.
     * @return a ResponseEntity with status OK and UserPreferencesDTO of the user as body.
     */
    @GetMapping("/getUserPreferences")
    public ResponseEntity<UserPreferencesDTO> getUserPreferences(@RequestParam String userName) {
        logger.info("GET request - getUserPreferences of user: " + userName);

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserPreferences(userService.getUser(userName)));
    }

}