package pl.itcraft.appstract.core.enums;

import java.util.Arrays;
import java.util.List;

public enum UserRole {
	SYSTEM,
	ROOT,
	ADMIN,
	ABSTRACTOR;
	
	public static List<UserRole> readOnly = Arrays.asList(SYSTEM, ADMIN);

	public boolean isReadOnly() {
		return readOnly.contains(this);
	}
	
}
