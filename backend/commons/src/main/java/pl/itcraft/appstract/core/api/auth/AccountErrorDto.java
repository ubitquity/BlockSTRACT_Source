package pl.itcraft.appstract.core.api.auth;

import pl.itcraft.appstract.core.dto.BusinessErrorDto;

public class AccountErrorDto extends BusinessErrorDto<AccountErrorType> {

	public AccountErrorDto(AccountErrorType errorType) {
		super(errorType);
	}
}
