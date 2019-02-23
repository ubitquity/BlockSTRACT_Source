package pl.itcraft.appstract.core;

import java.util.regex.Pattern;

public class CoreConstants {

	public static final String DS = System.getProperty("file.separator");
	
	public static final String AUTH_TOKEN_HEADER = "Authorization";
	public static final String AUTH_TOKEN_COOKIE = "authorization";

	public static final String DUMMY_IMAGES_PREFIX = "resources-commons";
	
	// Nazwa domyslnego awatara uzytkownika
	public static final String DUMMY_AVATAR = DUMMY_IMAGES_PREFIX + "/user-avatar.png";
	
	public static final Pattern EMAIL_PATTERN = Pattern.compile(
		"(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])",
		Pattern.CASE_INSENSITIVE
	);
	
	// Czas w sekundach po jakim tokeny do resetu hasla zostaja usuniete z bazy danych
	public static final int PASSWORD_TOKEN_RESET_VALIDITY = 60 * 60;
	
	// Czas w minutach po jakim tymczasowe pliki maja zostac usuniete
	public static final int MAX_MINUTES_FOR_TEMP_FILES = 24*60;
	
	// Ile ostatnich hasel musi byc roznych od nowego hasla
	public static final Integer PASSWORD_HISTORY_SIZE = 6;
	
	// Liczba dni po ktorych haslo wygasa (0 = nigdy)
	public static final Integer PASSWORD_EXPIRATION_TIME_IN_DAYS = 0;
	
}
