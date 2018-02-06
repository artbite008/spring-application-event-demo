package com.artbite008.spring.event.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;


@Service
public class JsonConverter {
	
	//this object is thread safe according to JACKSON documentation
	private static ObjectMapper mapper = new ObjectMapper();
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	//get instance from Spring Context 
	public static JsonConverter instance() {
		return new JsonConverter();
	}
	
	@PostConstruct
	public void init() {
		//configure the MAPPER object
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		mapper.setSerializationInclusion(Include.NON_NULL);
	}
	
	/**
	 * This method will write the object to JSON string without any configuration.
	 * Usually, this is used for developing or testing
	 * 
	 * mapper.writeValueAsString("") will return string "", instead of empty, need to be careful
	 * 
	 * TODO
	 * if passed in "obj" is JPA entity, need to be careful for lazy load attributes, because it's already out of persistence session
	 *
	 */
	public String toJson(Object obj) throws Exception{
		try {
			if(obj == null) return "";
			else return mapper.writeValueAsString(obj);
		} catch(JsonProcessingException e) {
			logger.error("Converting to Object error:", e);
			throw new Exception(e);
		}
	}
	
	/**
	 * Write object to customized JSON, using specified view
	 * Most customization would be already done by annotation in domain classes.
	 * 
	 */
	public String toJson(Object obj, Class<?> viewClz) throws Exception{
		try {
			if(obj == null) 
				return "";
			return mapper.writerWithView(viewClz).writeValueAsString(obj);
		} catch(JsonProcessingException e) {
			logger.error("Converting to Object error:", e);
			throw new Exception(e);
		}
	}
	
	
	/**
	 * Convert from string
	 */
	public <T> T toObject(String json, Class<T> cls) throws Exception {
		try {
			return mapper.readValue(json, cls);
		} catch (Exception e) {
			logger.error("Converting to Object error:", e);
			throw new Exception(e);
		}
	}
	
	/**
	 * Convert from string
	 */
	public <T> T toHashMap(String json, Class<?> keyClz, Class<?> valueClz) throws Exception {
		try {
			MapType mapType = mapper.getTypeFactory().constructMapType(HashMap.class, keyClz, valueClz);
			return mapper.readValue(json, mapType);
		} catch (Exception e) {
			logger.error("Converting to Object error:", e);
			throw new Exception(e);
		}
	}
	
	/**
	 * Convert from stream
	 */
	public <T> T toHashMap(InputStream input, Class<?> keyClz, Class<?> valueClz) throws Exception {
		try {
			MapType mapType = mapper.getTypeFactory().constructMapType(HashMap.class, keyClz, valueClz);
			return mapper.readValue(input, mapType);
		} catch (Exception e) {
			logger.error("Converting to Object error:", e);
			throw new Exception(e);
		}
	}
	
	/**
	 * Convert from string
	 */
	public <T> ArrayList<T> toArrayList(String json, Class<?> clz) throws Exception {
		try {
			CollectionType collType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, clz);
			return mapper.readValue(json, collType);
		} catch (Exception e) {
			logger.error("Converting to Object error:", e);
			throw new Exception(e);
		}
	}
	
	public <T> ArrayList<T> toArrayList(InputStream input, Class<T> clz) throws Exception {
		try {
			return mapper.readValue(input,
					TypeFactory.defaultInstance().constructCollectionType(List.class, clz));
		} catch (Exception e) {
			logger.error("Converting to Object error:", e);
			throw new Exception(e);
		} 
	}
	
	/**
	 * Convert from string
	 */
	public <T> T toHashMapArrayList(String json, Class<?> keyClz, Class<?> valueClz) throws Exception {
		try {
			MapType mapType = mapper.getTypeFactory().constructMapType(HashMap.class, keyClz, valueClz);
			CollectionType collType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, mapType);
			return mapper.readValue(json, collType);
		} catch (Exception e) {
			logger.error("Converting to Object error:", e);
			throw new Exception(e);
		}
	}

	/**
	 * Convert from InputStream
	 */
	public <T> T toObject(InputStream src, Class<T> cls) throws Exception {
		try {
			return mapper.readValue(src, cls);
		} catch (Exception e) {
			logger.error("Converting to Object error:", e);
			throw new Exception(e);
		}
	}
	
	
	public void updateObject(String json, Object target) throws Exception {
		ObjectReader or = mapper.readerForUpdating(target);
		try {
			or.readValue(json);
		} catch (Exception e) {
			logger.error("Converting to Object error:", e);
			throw new Exception(e);
		}
	}
	
	public <T> T toObject(Object src, Class<T> cls) throws Exception {
		try {
			return mapper.convertValue(src, cls);
		} catch (Exception e) {
			logger.error("Converting to Object error:", e);
			throw new Exception(e);
		}
	}


}
