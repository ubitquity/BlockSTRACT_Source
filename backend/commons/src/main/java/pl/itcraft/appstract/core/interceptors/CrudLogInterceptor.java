package pl.itcraft.appstract.core.interceptors;

import java.io.Serializable;
import java.util.HashMap;

import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;
import org.hibernate.engine.transaction.spi.LocalStatus;
import org.hibernate.type.EntityType;
import org.hibernate.type.LiteralType;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import pl.itcraft.appstract.core.entities.BaseEntity;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.security.UserAccount;

public class CrudLogInterceptor extends EmptyInterceptor {

	private static final long serialVersionUID = 1L;

	private static final Logger logger = LoggerFactory.getLogger(CrudLogInterceptor.class);

	@Override
	public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		final int length = propertyNames.length;
		StringBuilder sb = new StringBuilder(String.format("onDelete %s#%s by %s \n", entity.getClass().getSimpleName(), id, getAuthenticatedUserInfo()));
		for (int i = 0; i < length; i++) {
			if (checkIfLogThisType(types[i])) {
				sb.append(String.format(" - %s : '%s'\n", propertyNames[i], state[i]));
			}
		}
		logger.info(sb.toString());
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
		final int length = currentState.length;
		StringBuilder sb = new StringBuilder(String.format("onUpdate %s#%s by %s \n", entity.getClass().getSimpleName(), id, getAuthenticatedUserInfo()));
		for (int i = 0; i < length; i++) {
			if (checkIfLogThisType(types[i]) && checkIfValueChanged(previousState[i], currentState[i])) {
				sb.append(String.format(" - %s : '%s' > '%s' \n", propertyNames[i], previousState[i], currentState[i]));
			}
		}
		logger.info(sb.toString());
		return true;
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
		if (!(entity.getClass().getSimpleName().startsWith("org.hibernate.envers") || (entity instanceof HashMap && ((HashMap<?, ?>) entity).containsKey("revType")))) {
			final int length = propertyNames.length;
			StringBuilder sb = new StringBuilder(String.format("onSave %s#%s by %s \n", entity.getClass().getSimpleName(), id, getAuthenticatedUserInfo()));
			for (int i = 0; i < length; i++) {
				if (checkIfLogThisType(types[i])) {
					sb.append(String.format(" - %s : '%s' \n", propertyNames[i], state[i]));
				}
			}
			logger.info(sb.toString());
		}
		return true;
	}
	
	@Override
	public void afterTransactionCompletion(Transaction tx) {
		if (!LocalStatus.COMMITTED.equals(tx.getLocalStatus())) {
			logger.info("Not committed transaction. Status "+tx.getLocalStatus());
		}
	}

	private boolean checkIfValueChanged(Object o, Object o1) {
		if (o == null && o1 == null) {
			return false;
		}
		if (o == null || o1 == null) {
			return true;
		}
		if (o instanceof BaseEntity && o1 instanceof BaseEntity) {
			Long id = ((BaseEntity) o).getId();
			Long id1 = ((BaseEntity) o1).getId();
			return checkIfValueChanged(id, id1);
		}
		return !o.equals(o1);
	}

	private boolean checkIfLogThisType(Type type) {
		return (type instanceof LiteralType || type instanceof EntityType);
	}

	private String getAuthenticatedUserInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			if (principal != null && principal instanceof UserAccount) {
				User user = ((UserAccount) principal).getUser();
				if (user != null) {
					return user.toString();
				}
			}
		}
		return "anonymous";
	}

}
