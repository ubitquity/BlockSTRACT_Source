package pl.itcraft.appstract.admin;

import java.util.Arrays;
import java.util.List;

public final class Constants {
	
	public static final int GENERATED_PASSWORD_LENGTH = 10;	
	public static final int DEFAULT_PAGE_SIZE = 10;	
	public static final int ORDER_UNDER_REVIEW_TIME_PERIOD_IN_HOURS = 1;	
	public static final int ORDER_OVERDUE_TIME_LIMIT_IN_HOURS = 25;	
	public static final int FIRST_ABSTRACTORS_TIME_LIMIT_IN_MINUTES = 10;	
	
	public static final List<String> ALLOWED_FILE_TYPES = Arrays.asList("image/jpeg", "application/x-zip-compressed", "application/zip", "application/pdf");

	private Constants() {}
}
