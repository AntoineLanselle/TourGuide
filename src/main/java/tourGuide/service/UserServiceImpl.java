package tourGuide.service;

import org.javamoney.moneta.Money;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * Returns the User with the userName in parameter.
     *
     * @param userName a String of the userName of the User we want to found.
     *
     * @return a user with the username specified in the parameter if it exists, null otherwise.
     */
    @Override
    public User getUser(String userName) {
        return userRepository.getUsersList().get(userName);
    }

    /**
     * Returns all the Users in the respository.
     *
     * @return a List of Users containing all Users in repository.
     */
    @Override
    public List<User> getAllUsers() {
        logger.info("Get all users from userRepository");
        return userRepository.getUsersList().values().stream().collect(Collectors.toList());
    }

    /**
     * Add the user specified in parameter in repository if it is not already there.
     *
     * @param user the User we want to add in repository
     */
    @Override
    public void addUser(User user) {
        logger.info("Try to add user: " + user.getUserName() + " in userRepository");
        if (!userRepository.getUsersList().containsKey(user.getUserName())) {
            userRepository.getUsersList().put(user.getUserName(), user);
            logger.info("User: " + user.getUserName() + " has been added in userRepository");
        }
    }

    @Override
    public void setUserPreferences(User user, UserPreferencesDTO preferencesDTO) {
        logger.info("Set new user preferences for user: " + user.getUserName());
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
        logger.info("Get user preferences of user: " + user.getUserName());
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
