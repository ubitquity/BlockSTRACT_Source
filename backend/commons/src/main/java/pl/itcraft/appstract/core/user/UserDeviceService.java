package pl.itcraft.appstract.core.user;

import java.util.List;

import pl.itcraft.appstract.core.entities.User;

public interface UserDeviceService {

	List<String> findDeviceRegistrationIdsForUser(User user);

	void replaceDeviceToken(String drId, String canonDrId);

	void dropDeviceToken(String drId);

}