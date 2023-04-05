package tourGuide.controllers;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import tourGuide.DTO.TouristAttractionDetailsDTO;
import tourGuide.domain.User;
import tourGuide.domain.UserReward;
import tourGuide.service.RewardsService;
import tourGuide.service.TourGuideService;
import tourGuide.service.TripPricerService;
import tourGuide.service.UserService;
import tripPricer.Provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class TourGuideController {

    //TODO private Logger logger = LoggerFactory.getLogger(TourGuideController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private TourGuideService tourGuideService;
    @Autowired
    private TripPricerService tripPricerService;
    @Autowired
    private RewardsService rewardsService;

    @RequestMapping("/")
    public String index() {
        return "Greetings from TourGuide!";
    }

    @RequestMapping("/getLocation")
    public ResponseEntity<Location> getLocation(@RequestParam String userName) {
        VisitedLocation visitedLocation = tourGuideService.getLastVisitedLocation(userService.getUser(userName));
        return ResponseEntity.status(HttpStatus.OK).body(visitedLocation.location);
    }

    // TODO: Change this method to no longer return a List of Attractions.
    // Instead: Get the closest five tourist attractions to the user - no matter how
    // far away they are.
    // Return a new JSON object that contains:
    // Name of Tourist attraction,
    // Tourist attractions lat/long,
    // The user's location lat/long,
    // The distance in miles between the user's location and each of the
    // attractions.
    // The reward points for visiting each Attraction.
    // Note: Attraction reward points can be gathered from RewardsCentral
    @RequestMapping("/getNearbyAttractions")
    public ResponseEntity<List<TouristAttractionDetailsDTO>> getNearbyAttractions(@RequestParam String userName) {
        User user = userService.getUser(userName);
        List<TouristAttractionDetailsDTO> closestAttractions = tourGuideService.getFiveClosestAttractions(user);
        return ResponseEntity.status(HttpStatus.OK).body(closestAttractions);
    }

    @RequestMapping("/getRewards")
    public ResponseEntity<List<UserReward>> getRewards(@RequestParam String userName) {
        User user = userService.getUser(userName);
        return ResponseEntity.status(HttpStatus.OK).body(user.getUserRewards());
    }

    // TODO: Get a list of every user's most recent location as JSON
    // - Note: does not use gpsUtil to query for their current location,
    // but rather gathers the user's current location from their stored location
    // history.
    //
    // Return object should be the just a JSON mapping of userId to Locations
    // similar to:
    // {
    // "019b04a9-067a-4c76-8817-ee75088c3822":
    // {"longitude":-48.188821,"latitude":74.84371}
    // ...
    // }
    @RequestMapping("/getAllCurrentLocations")
    public ResponseEntity<Map<String, Object>> getAllCurrentLocations() {
        List<User> allUsers = userService.getAllUsers();
        Map<String, Object> currentLocations = new HashMap<String, Object>();
        for (User user : allUsers) {
            Map<String, Double> userCoord = new HashMap<String, Double>();
            // pas d'accord avec Clément cf note du TODO.
            VisitedLocation userLocation = tourGuideService.getLastVisitedLocation(user);
            userCoord.put("longitude", userLocation.location.longitude);
            userCoord.put("latitude", userLocation.location.latitude);
            currentLocations.put(user.getUserId().toString(), userCoord);
        }
        return ResponseEntity.status(HttpStatus.OK).body(currentLocations);
    }

    @RequestMapping("/getTripDeals")
    public ResponseEntity<List<Provider>> getTripDeals(@RequestParam String userName) {
        List<Provider> providers = tripPricerService.getTripDeals(userService.getUser(userName));
        return ResponseEntity.status(HttpStatus.OK).body(providers);
    }

}