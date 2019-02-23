package pl.itcraft.appstract.core.api.auth;

import pl.itcraft.appstract.core.dto.UserDto;

public class AuthenticationResultDto {

	private String accessToken;

	private UserDto user;

	public AuthenticationResultDto() {
		super();
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

}
