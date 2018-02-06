package com.artbite008.spring.event.util;

import java.util.function.BiFunction;

public class StringMerge implements BiFunction<String, String, String> {

	@Override
	public String apply(String t, String u) {
		// TODO Auto-generated method stub
		int index = t.indexOf(")");
		if(index == -1){
			t = "(" + t + "," + u +")";
			return t;
		}else {
			String result = t.substring(0, index) + "," + u + ")";
			return result;
		}
	}

}
