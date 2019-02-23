package pl.itcraft.appstract.admin.profile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.user.UserService;

@Service
@Transactional
public class AdminProfileService {

	@PersistenceContext
	private EntityManager em;
	
	@Autowired
	private UserService userService;
	
	public void update(Long userId, AdminProfileUpdateDto dto) {
		User user = userService.findById(userId);
		if (user == null) {
			throw new ApiException(RC.RESOURCE_NOT_FOUND);
		}
		user.updateAdmin(dto.getEmail(), dto.getBankAccount(), dto.isNotifications());
	}
	
}
