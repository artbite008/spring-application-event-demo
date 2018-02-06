package com.artbite008.spring.event.util;

import java.util.UUID;

public class IdUtil {
	
	public static String getUniqueId(String prefix) {
		if(prefix != null && prefix.length() > 0)
			return prefix + UUID.randomUUID().toString().replaceAll("-", "");
		else 
			return UUID.randomUUID().toString().replaceAll("-", "");
	}

}
