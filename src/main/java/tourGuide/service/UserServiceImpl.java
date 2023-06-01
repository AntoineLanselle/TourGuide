package tourGuide.service;

import org.javamoney.moneta.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.DTO.UserPreferencesDTO;
import tourGuide.domain.User;
import tourGuide.domain.UserPreferences;
import tourGuide.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service implementation for User.
 *
 * @author Antoine Lanselle
 */
@Service
public class UserServiceImpl implements UserService {

    // TODO private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    @Override
    public User getUser(String userName) {
        return userRepository.getUsersList().get(userName);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getUsersList().values().stream().collect(Collectors.toList());
    }

    @Override
    public void addUser(User user) {
        if (!userRepository.getUsersList().containsKey(user.getUserName())) {
            userRepository.getUsersList().put(user.getUserName(), user);
        }
    }

    @Override
    public void setUserPreferences(User user, UserPreferencesDTO preferencesDTO) {
        UserPreferences preferences = user.getUserPreferences();

        preferences.setAttractionProximity(preferencesDTO.getAttractionProximity());
        preferences.setLowerPricePoint(Money.of(BigDecimal.valueOf(preferencesDTO.getLowerPricePoint()), preferences.getCurrency()));
        preferences.setHighPricePoint(Money.of(BigDecimal.valueOf(preferencesDTO.getHighPricePoint()), preferences.getCurrency()));
        preferences.setTripDuration(preferencesDTO.getTripDuration());
        preferences.setTicketQuantity(preferencesDTO.getTicketQuantity());
        preferences.setNumberOfAdults(preferencesDTO.getNumberOfAdults());
        preferences.setNumberOfChildren(preferencesDTO.getNumberOfChildren());
    }

    @Override
    public UserPreferencesDTO getUserPreferences(User user) {
        UserPreferences preferences = user.getUserPreferences();
        UserPreferencesDTO userPreferencesDTO =
                new UserPreferencesDTO(
                        preferences.getAttractionProximity(),
                        preferences.getLowerPricePoint().getNumber().doubleValue(),
                        preferences.getHighPricePoint().getNumber().doubleValue(),
                        preferences.getTripDuration(),
                        preferences.getTicketQuantity(),
                        preferences.getNumberOfAdults(),
                        preferences.getNumberOfChildren());
        return userPreferencesDTO;
    }

}
