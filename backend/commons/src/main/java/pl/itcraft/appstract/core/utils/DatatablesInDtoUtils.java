package pl.itcraft.appstract.core.utils;

public class DatatablesInDtoUtils {
	
	public static final int DEFAULT_DRAW = 30;
	public static final int DEFAULT_START = 0;
	public static final int DEFAULT_LENGTH = 50;
	
	public static int setDraw(String draw) {		
		return convertParamToInt(draw, DEFAULT_DRAW);
	}
	
	public static int setStart(String start) {
		return convertParamToInt(start, DEFAULT_START);
	}
	
	public static int setLength(String length) {
		return convertParamToInt(length, DEFAULT_LENGTH);
	}
	
	private static int convertParamToInt(String param, int defaultValue) {
		return param == null ? defaultValue : Integer.parseInt(param);
	}

}
