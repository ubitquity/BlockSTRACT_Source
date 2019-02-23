package pl.itcraft.appstract.admin.fw.security;

import java.io.Serializable;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;

import pl.itcraft.appstract.core.enums.Permission;
import pl.itcraft.appstract.core.security.BasePermissionEvaluator;

public class AppPermissionEvaluator extends BasePermissionEvaluator implements PermissionEvaluator {
	
	/**
	 * hasPermission(#uploadedFile, 'UPLOADED_FILE_ACCESS')
	 */
	@Override
	public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
		if (Permission.UPLOADED_FILE_ACCESS.isA(permission)) {
			return hasAccessToFile(authentication, targetDomainObject);
		}
		return true;
	}
	
	@Override
	public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
		return true;
	}
	
}
