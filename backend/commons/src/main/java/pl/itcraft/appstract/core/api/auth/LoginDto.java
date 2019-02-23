package pl.itcraft.appstract.core.api.auth;

public class LoginDto {

	private String email;
	private String password;

	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public void setEmail(String email) {
		this.email = email == null ? null : email.toLowerCase();
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
