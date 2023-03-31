package tourGuide.repositories;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import tourGuide.domain.User;

//Database connection will be used for external users, but for testing purposes internal users are provided and stored in memory
@Repository
public class UserRepository {

	private final Map<String, User> internalUserMap = new HashMap<>();

	public Map<String, User> getInternalUserMap() {
		return this.internalUserMap;
	}
	
	public void setInternalUserMap(String username, User user) {
		this.internalUserMap.put(username, user);
	}

}
