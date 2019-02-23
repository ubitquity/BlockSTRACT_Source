package pl.itcraft.appstract.core.api.error;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * App Response Codes
 */
public class RC {
	
	public static final int OK         = 200;
	public static final int NO_CONTENT = 204;
	
	public static final int BAD_REQUEST         = 400;
	public static final int UNAUTHORIZED        = 401;
	public static final int FORBIDDEN           = 403;
	public static final int NOT_FOUND           = 404;
	public static final int CONFLICT            = 409;
	
	public static final int VALIDATION_ERROR    = 460;
	public static final int GENERAL_ERROR       = 461;
	public static final int BUSINESS_ERROR      = 463;
	public static final int RESOURCE_NOT_FOUND  = 464;

	public static final int INTERNAL_SERVER_ERROR = 500;
	
	public static SortedMap<Integer,String> codes;
	public static String codesDescription = "";
	
	static {
		codes = new TreeMap<>();
		for (Field field : RC.class.getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers())) {
				Object staticValue = null;
				try {
					staticValue = field.get(null);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				if (staticValue instanceof Integer) {
					Integer code = (Integer) staticValue;
					if (codes.containsKey(code)) {
						throw new RuntimeException("Response code duplicated RC:"+code);
					}
					codes.put(code, field.getName());
					codesDescription += code + ": " + field.getName() + "\n";
				}
			}
		}
	}
	
}
