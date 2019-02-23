package pl.itcraft.appstract.core.api.auth;

import io.swagger.annotations.ApiModel;

@ApiModel
public enum AccountErrorType {
	NOT_ACTIVATED,
	DISABLED,
	LOCKED;
	
}
