package pl.itcraft.appstract.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;

public class AppDateFormat extends SimpleDateFormat {
	private static final long serialVersionUID = -5752421307976780347L;
	
	public static final String DATE_FORMAT = "yyyy-MM-dd";
	public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm";
	public static final String TIME_FORMAT = "HH:mm";

	public AppDateFormat(String format) {
		super(format);
	}

	public static AppDateFormat createDate() {
		return new AppDateFormat(DATE_FORMAT);
	}

	public static AppDateFormat createDateTime() {
		return new AppDateFormat(DATETIME_FORMAT);
	}

	public static AppDateFormat createTime() {
		return new AppDateFormat(TIME_FORMAT);
	}

	public String formatDate(Date date) {
		if (date == null) {
			return null;
		}
		return format(date);
	}
	
	public Date parseDate(String dateTime) {
		if (dateTime == null || "".equals(dateTime)) {
			return null;
		}
		try {
			return parse(dateTime);
		} catch (ParseException e) {
			throw new RuntimeException("Invalid date-time ["+dateTime+"]");
		}
	}

	public DateTime parseDateTime(String dateTime) {
		Date d = parseDate(dateTime);
		return d==null ? null : new DateTime(d);
	}
}
