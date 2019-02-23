package pl.itcraft.appstract.core.enums;

public enum Permission {
	
	UPLOADED_FILE_ACCESS,
	IS_MY_ORDER;
	
	public boolean isA(Object permission) {
		return name().equals(permission);
	}

}
