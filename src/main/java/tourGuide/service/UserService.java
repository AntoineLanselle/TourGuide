package tourGuide.service;

import tourGuide.DTO.UserPreferencesDTO;
import tourGuide.domain.User;
import tourGuide.domain.UserPreferences;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service for User.
 *
 * @author Antoine Lanselle
 */
public interface UserService {

    public User getUser(String userName);

    public List<User> getAllUsers();

    public void addUser(User user);

    public void setUserPreferences(User user, UserPreferencesDTO userPreferencesDTO);

    public UserPreferencesDTO getUserPreferences(User user);

}
