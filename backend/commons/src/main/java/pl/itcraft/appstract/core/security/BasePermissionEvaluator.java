package pl.itcraft.appstract.core.security;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import pl.itcraft.appstract.core.api.error.ApiException;
import pl.itcraft.appstract.core.api.error.RC;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.entities.UploadedFile;

public abstract class BasePermissionEvaluator {
	
	private static final Logger LOG = LoggerFactory.getLogger(BasePermissionEvaluator.class);

	@PersistenceContext
	protected EntityManager em;
	
	protected User getUser(Authentication authentication) {
		if (authentication.getPrincipal() instanceof UserAccount) {
			return ((UserAccount) authentication.getPrincipal()).getUser();
		} else {
			return null;
		}
	}

	protected boolean hasAccessToFile(Authentication authentication, Object targetDomainObject) {
		if (targetDomainObject != null && targetDomainObject instanceof UploadedFile) {
			UploadedFile uf = (UploadedFile) targetDomainObject;
			String entityType = uf.getEntityType();
			Long entityId = uf.getEntityId();
			User user = getUser(authentication);
			if (entityType == null && entityId == null) {
				return true;
			} else {
				// TODO Tutaj mozesz zaimplementowac sprawdzenie czy 'user' ma dostep do 'entityType'+'entityId'
				// Np w taki sposob:
				List<?> results = em.createQuery("SELECT e FROM "+entityType+" e WHERE e.id = :id")
					.setParameter("id", entityId)
					.getResultList();
				if (!results.isEmpty()) {
					Object relatedObject = results.get(0);
					LOG.info("TODO: Has {} access to file {} related to {}?", user, uf, relatedObject);
					return true;
				} else {
					throw new ApiException(RC.INTERNAL_SERVER_ERROR, String.format("File %s has corrupted relation to %s.%s", uf, entityType, entityId));
				}
			}
		}
		return true;
	}

}
