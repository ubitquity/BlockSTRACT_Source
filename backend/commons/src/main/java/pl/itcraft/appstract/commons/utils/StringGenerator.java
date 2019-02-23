package pl.itcraft.appstract.commons.utils;

import java.util.Random;

import org.apache.commons.lang.RandomStringUtils;

public class StringGenerator {

	public static String generate(int minLength, int maxLength) {
		int charactersNumber = new Random().nextInt((maxLength - minLength) + 1) + minLength;
		String value = RandomStringUtils.random(charactersNumber, true, true);
		return value;
	}

}
