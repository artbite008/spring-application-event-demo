package com.artbite008.spring.event.util;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class NamingValidator {
	
	private Pattern patternNormalName;
	
//	start with letter, only letter, digital, _ and -, length 4 - 31
//	private static final String NORMALNAME_PATTERN = "^[a-zA-Z][a-zA-Z0-9_-]{3,30}$";
	
	private static final String NORMALNAME_PATTERN = "[a-zA-Z0-9_-]+";

	public NamingValidator() {

		patternNormalName = Pattern.compile(NORMALNAME_PATTERN);
	}
	
	public boolean validateNormalName(final String name) {
		
		return patternNormalName.matcher(name).matches();
		
	}

}
