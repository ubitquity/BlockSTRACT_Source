package pl.itcraft.appstract.core.entities.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.util.StringUtils;

@Converter
public class StringArrayConverter implements AttributeConverter<String[], String> {

	private static final String SEPARATOR = "\n";
	
	@Override
	public String convertToDatabaseColumn(String[] attribute) {
		return StringUtils.arrayToDelimitedString(attribute, SEPARATOR);
	}

	@Override
	public String[] convertToEntityAttribute(String dbData) {
		return StringUtils.delimitedListToStringArray(dbData, SEPARATOR);
	}

}
