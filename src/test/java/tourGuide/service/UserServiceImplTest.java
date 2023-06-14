package tourGuide.service;

import gpsUtil.location.Attraction;
import gpsUtil.location.Location;
import gpsUtil.location.VisitedLocation;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tourGuide.DTO.UserPreferencesDTO;
import tourGuide.domain.User;
import tourGuide.repositories.UserRepository;

import javax.money.Monetary;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserServiceImpl userService;

    private List<User> users;

    @BeforeAll
    public void init() {
        users = new ArrayList<User>();
        User user0 = new User(UUID.randomUUID(), "userName0", "phone", "email");
        User user1 = new User(UUID.randomUUID(), "userName1", "phone", "email");
        User user2 = new User(UUID.randomUUID(), "userName2", "phone", "email");
        users.add(user0);
        users.add(user1);
        users.add(user2);
    }

    @Test
    public void getUser_ShouldReturnUser() {
        // GIVEN
        //doReturn(users.get(0)).when(userRepository.getUsersList()).get("userName0");
        when(userRepository.getUsersList().get("userName0")).thenReturn(users.get(0));

        // WHEN
        User testResult = userService.getUser("userName0");

        // THEN
        assertEquals(users.get(0), testResult);
    }

    @Test
    public void getAllUsers_ShouldReturnListOfUsers() {
        // GIVEN
        // doReturn(users).when(userRepository.getUsersList()).values().stream().collect(Collectors.toList());
        when(userRepository.getUsersList().values().stream().collect(Collectors.toList())).thenReturn(users);

        // WHEN
        List<User> testResult = userService.getAllUsers();

        // THEN
        assertEquals(users, testResult);
    }
/*
    @Test
    public void addlUser_ShouldAddUserInRepository() {
        // GIVEN
        User user = new User(UUID.randomUUID(), "userName3", "phone", "email");
        when(userRepository.getUsersList().containsKey("userName3")).thenReturn(false);
        doNothing().when(userRepository.getUsersList().put(user.getUserName(), user));

        // WHEN
        userService.addUser(user);

        // THEN
        assertTrue(users.contains(user));
    }
*/
    @Test
    public void addUser_ShouldNotAddUserInRepository() {
        // GIVEN
        User user = new User(UUID.randomUUID(), "userName0", "phone", "email");
        when(userRepository.getUsersList().containsKey(user.getUserName())).thenReturn(true);

        // WHEN
        userService.addUser(user);

        // THEN
        assertEquals(1, users.stream().filter(u -> u.getUserName().equals(user.getUserName())).count());
    }

    @Test
    public void setUserPreferences_ShouldUpdateUserPreferences() {
        // GIVEN
        User user = users.get(0);
        UserPreferencesDTO userPrefrences = new UserPreferencesDTO(0, 0.0, 100.0, 7, 1, 1, 0);

        // WHEN
        userService.setUserPreferences(user, userPrefrences);

        // THEN
        assertEquals(0, user.getUserPreferences().getAttractionProximity());
        assertEquals(Money.of(BigDecimal.valueOf(0.0), Monetary.getCurrency("USD")), user.getUserPreferences().getLowerPricePoint());
        assertEquals(Money.of(BigDecimal.valueOf(100.0), Monetary.getCurrency("USD")), user.getUserPreferences().getHighPricePoint());
        assertEquals(7, user.getUserPreferences().getTripDuration());
        assertEquals(1, user.getUserPreferences().getTicketQuantity());
        assertEquals(1, user.getUserPreferences().getNumberOfAdults());
        assertEquals(0, user.getUserPreferences().getNumberOfChildren());
    }

    @Test
    public void getUserPreferences_ShouldReturnUserPreferencesDTO() {
        // GIVEN
        User user = users.get(0);
        UserPreferencesDTO userPrefrences = new UserPreferencesDTO(0, 0.0, 100.0, 7, 1, 1, 0);
        userService.setUserPreferences(user, userPrefrences);

        // WHEN
        UserPreferencesDTO testResult = userService.getUserPreferences(user);

        // THEN
        assertEquals(0, testResult.getAttractionProximity());
        assertEquals(0.0, testResult.getLowerPricePoint());
        assertEquals(100.0, testResult.getHighPricePoint());
        assertEquals(7, testResult.getTripDuration());
        assertEquals(1, testResult.getTicketQuantity());
        assertEquals(1, testResult.getNumberOfAdults());
        assertEquals(0, testResult.getNumberOfChildren());
    }

}
