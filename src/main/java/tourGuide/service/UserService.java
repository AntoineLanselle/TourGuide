package tourGuide.service;

import java.util.List;

import tourGuide.domain.User;

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
