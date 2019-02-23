package pl.itcraft.appstract.admin.utils;

import org.apache.commons.lang.RandomStringUtils;

import pl.itcraft.appstract.admin.Constants;

public class PasswordGenerator {
	
	public static String generatePassword() {
		return RandomStringUtils.randomAlphanumeric(Constants.GENERATED_PASSWORD_LENGTH);
	}

}
