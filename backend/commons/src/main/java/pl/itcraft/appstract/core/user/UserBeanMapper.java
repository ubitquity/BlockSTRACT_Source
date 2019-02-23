package pl.itcraft.appstract.core.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pl.itcraft.appstract.core.BaseAppModuleConfig;
import pl.itcraft.appstract.core.dto.UserDto;
import pl.itcraft.appstract.core.entities.User;

@Component
public class UserBeanMapper {
	
	@Autowired
	private BaseAppModuleConfig appModuleConfig;

	public UserDto map(User user) {
		if (user == null) {
			return null;
		}
		UserDto dto = new UserDto();
		dto = new UserDto();
		dto.setId(user.getId());
		dto.setEmail(user.getEmail());
		dto.setFirstName(user.getFirstName());
		dto.setLastName(user.getLastName());
		dto.setRole(user.getRole());
		dto.setAvatarUrl( appModuleConfig.getAbsoluteFileUrl(user.getRealOrDummyAvatarPath()));
		dto.setUploaded(user.getAvatar() != null);
		return dto;
	}
}
