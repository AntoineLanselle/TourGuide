package tourGuide.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tourGuide.domain.User;
import tourGuide.repositories.UserRepository;

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

	public User getUser(String userName) {
		return userRepository.getInternalUserMap().get(userName);
	}

	public List<User> getAllUsers() {
		return userRepository.getInternalUserMap().values().stream().collect(Collectors.toList());
	}

	public void addUser(User user) {
		if (!userRepository.getInternalUserMap().containsKey(user.getUserName())) {
			userRepository.getInternalUserMap().put(user.getUserName(), user);
		}
	}

}
