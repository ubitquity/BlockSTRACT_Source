package pl.itcraft.appstract.admin.password;

import java.util.Date;

import org.springframework.stereotype.Component;

import pl.itcraft.appstract.commons.utils.StringGenerator;
import pl.itcraft.appstract.core.entities.User;
import pl.itcraft.appstract.core.entities.PasswordReset;

@Component
public class ResetPasswordMapper {	
		
	public PasswordReset createPasswordReset(User user) {
		PasswordReset pr = new PasswordReset();
		pr.setCreatedAt(new Date());
		pr.setUser(user);
		pr.setToken(StringGenerator.generate(20, 30));
		return pr;
	}

}
