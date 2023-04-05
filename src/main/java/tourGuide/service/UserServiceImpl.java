package tourGuide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourGuide.domain.User;
import tourGuide.repositories.UserRepository;

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

}
