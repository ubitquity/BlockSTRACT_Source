package pl.itcraft.appstract.commons.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	private static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
	
	public static String formatToDateTime(Date date) {
		DateFormat df = new SimpleDateFormat(DATETIME_FORMAT);
		return df.format(date);
	}

}
