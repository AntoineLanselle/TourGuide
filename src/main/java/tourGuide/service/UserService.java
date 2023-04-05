package tourGuide.service;

import tourGuide.domain.User;

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

}
