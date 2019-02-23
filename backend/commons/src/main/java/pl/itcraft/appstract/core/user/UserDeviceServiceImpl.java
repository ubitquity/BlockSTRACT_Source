package pl.itcraft.appstract.core.user;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pl.itcraft.appstract.core.entities.User;

@Service
@Transactional
public class UserDeviceServiceImpl implements UserDeviceService {
	
	private final Logger logger = LoggerFactory.getLogger(UserDeviceServiceImpl.class);
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public List<String> findDeviceRegistrationIdsForUser(User user) {
		if (user == null) {
			return Collections.<String>emptyList();
		}
		String queryString = "SELECT e.registrationId FROM ApiLoginEntry e"
				+ " WHERE e.user = :user"
				+ " AND e.tokenExpirationDate > :now"
				+ " AND e.logoutTime IS NULL"
				+ " AND e.registrationId IS NOT NULL";
		return em.createQuery(queryString, String.class)
			.setParameter("user", user)
			.setParameter("now", new Date())
			.getResultList();
	}
	
	@Override
	public void replaceDeviceToken(String drId, String canonDrId) {
		logger.info("Replacing device token from: {} to: {}", drId, canonDrId);
		String queryString = "UPDATE ApiLoginEntry e SET registrationId = :canon, WHERE registrationId = :id";
		em.createQuery(queryString)
			.setParameter("canon", canonDrId)
			.setParameter("id", drId)
			.executeUpdate();
		
	}

	@Override
	public void dropDeviceToken(String drId) {
		logger.info("Dropping device token: {}", drId);
		String queryString = "UPDATE ApiLoginEntry e SET registrationId = NULL, logoutTime = :now WHERE registrationId = :id";
		em.createQuery(queryString)
			.setParameter("now", new Date())
			.setParameter("id", drId)
			.executeUpdate();
	}
	
	
}
