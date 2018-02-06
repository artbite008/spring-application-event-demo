package com.artbite008.spring.event.controller;

import java.util.List;
import java.util.Map;

import com.artbite008.spring.event.util.JsonConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class BaseController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private JsonConverter jsonConverter;

	public JsonConverter getJsonConverter() {
		return jsonConverter;
	}

	public void setJsonConverter(JsonConverter jsonConverter) {
		this.jsonConverter = jsonConverter;
	}

	protected String toJson(Object obj) {
		String json = null;
		try {
			json = jsonConverter.toJson(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("converted json: " + json);
		}
		return json;
	}

	protected String toJson(Object obj, Class<?> viewClz) {
		String json = null;
		try {
			json = jsonConverter.toJson(obj, viewClz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (logger.isDebugEnabled()) {
			logger.debug("converted json: " + json);
		}
		return json;
	}

	@SuppressWarnings("unchecked")
	protected Map<String, Object> toMap(String json) {
		try {
			return jsonConverter.toObject(json, Map.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	protected <K, V> Map<K, V> toHashMap(String json, Class<?> keyClz, Class<?> valueClz) {
		try {
			return jsonConverter.toHashMap(json, keyClz, valueClz);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	protected <T> List<T> toArrayList(String json, Class<?> clz) {
		try {
			return jsonConverter.toArrayList(json, clz);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	protected <K, V> List<Map<K, V>> toHashMapArrayList(String json, Class<?> keyClz, Class<?> valueClz) {
		try {
			return jsonConverter.toHashMapArrayList(json, keyClz, valueClz);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	protected <T> T toObject(String json, Class<T> cls) {
		try {
			return jsonConverter.toObject(json, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;

	}

}
