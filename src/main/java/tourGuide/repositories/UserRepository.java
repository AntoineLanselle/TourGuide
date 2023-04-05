package tourGuide.repositories;

import org.springframework.stereotype.Repository;
import tourGuide.domain.User;
import tourGuide.helper.InternalTestHelper;

import java.util.HashMap;
import java.util.Map;

//Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
@Repository
public class UserRepository {

    private final Map<String, User> usersList;

    public UserRepository() {
        usersList = new HashMap<>();
        InternalTestHelper testHelper = new InternalTestHelper();
        testHelper.initializeInternalUsers(this.usersList);
    }

    public Map<String, User> getUsersList() {
        return this.usersList;
    }

}
