package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.domain.User;
import tourGuide.repositories.GpsUtilRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GpsUtilServiceImplTest {

    @Mock
    private GpsUtilRepository gpsUtil;
    @InjectMocks
    private GpsUtilServiceImpl gpsUtilService;

    @Test
    public void getAttractions_ShouldReturnListOfAttractions() {
        // GIVEN
        List<Attraction> attractions = new ArrayList<Attraction>();
        when(gpsUtil.getAttractions()).thenReturn(attractions);

        // WHEN
        List<Attraction> testResult = gpsUtilService.getAttractions();

        // THEN
        assertEquals(attractions, testResult);
    }

    @Test
    public void getUserLocation_ShouldReturnVisitedLocation() {
        // GIVEN
        User user = new User(UUID.randomUUID(), "userName", "phone", "email");
        VisitedLocation visitedLocation = new VisitedLocation(user.getUserId(), new Location(1.1, 1.1), new Date());
        when(gpsUtil.getUserLocation(user.getUserId())).thenReturn(visitedLocation);

        // WHEN
        VisitedLocation testResult = gpsUtilService.getUserLocation(user.getUserId());

        // THEN
        assertEquals(visitedLocation, testResult);
    }

}
