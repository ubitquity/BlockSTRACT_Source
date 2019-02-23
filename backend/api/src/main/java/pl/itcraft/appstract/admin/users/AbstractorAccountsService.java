package pl.itcraft.appstract.admin.users;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import pl.itcraft.appstract.admin.orders.OrdersService;
import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.user.UserService;

@Service
@Transactional
public class AbstractorAccountsService {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractorAccountsService.class);

	@PersistenceContext
	private EntityManager em;

	@Autowired
	private UserService userService;
	
	@Autowired
	private OrdersService ordersService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public Long create(AbstractorAccountDto dto) {
		String pwd = passwordEncoder.encode( dto.getPassword() );
		User user = new User(dto.getRole(), pwd, dto.getEmail(), dto.getFirstName(), dto.getLastName());
		user.setAccountActivated(false);
		user.setAccountLocked(false);
		user.setEnabled(true);
		user.setNotifications(true);
		em.persist(user);
		return user.getId();
	}
	
	public void update(AbstractorAccountDto dto) {
		User user = userService.findById( dto.getId() );
		if (user == null) {
			throw new ApiException(RC.RESOURCE_NOT_FOUND);
		}
		if (user.getRole().isReadOnly()) {
			throw new ApiException(RC.FORBIDDEN);
		}
		user.update(dto.getRole(), dto.getEmail(), dto.getFirstName(), dto.getLastName());
		
		if (StringUtils.hasLength(dto.getPassword())) {
			userService.changeUserPassword(user.getId(), dto.getPassword(), false);
		}
	}
	
	public void delete(Long userId) {
		User user = userService.findById(userId);
		if (user == null) {
			throw new ApiException(RC.RESOURCE_NOT_FOUND);
		}
		if (ordersService.abstractorHasAnyOrders(user)) {
			throw new ApiException(RC.FORBIDDEN);
		}
		user.markAsDeleted();
		LOG.info("User {} deleted", user);
	}
	
	public void activate(Long userId) {
		User user = userService.findById(userId);
		if (user == null) {
			throw new ApiException(RC.RESOURCE_NOT_FOUND);
		}
		user.activate();
		LOG.info("User {} activated", user);
	}
	
	public void deactivate(Long userId) {
		User user = userService.findById(userId);
		if (user == null) {
			throw new ApiException(RC.RESOURCE_NOT_FOUND);
		}
		user.deactivate();
		LOG.info("User {} deactivated", user);
	}

}
