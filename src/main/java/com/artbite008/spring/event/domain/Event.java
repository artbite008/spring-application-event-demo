package com.artbite008.spring.event.domain;

import com.artbite008.spring.event.util.IdUtil;
import com.artbite008.spring.event.util.JsonConverter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name="EVENT")
public class Event {
	
	public static String CONTEXT_AUTH = "auth";
	
	@Id
	private String id;
	
	//source object type, like Document, CollaboraitonRoom, BP, etc.
	private String sourceType;
	
	//reference id of source object
	private String sourceRef;
	
	//event type, like CREATE, UPDATE, DELETE, etc.
	private String eventType;
	
	//creator of the event, tenant id or user id
	private String originator;

	private String additionalInfo;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;

	private String bodyJson;
	
	//map of event parameters, Transient attribute
	@Transient
	private Map<String, Object> body;
	
	//context associated with this event
	@Transient
	private Map<String, Object> context;
	
	public Event() {
		//default constructor
		this.id = IdUtil.getUniqueId("Event");
		this.createdTime = new Date();
	}
	
	public Event(String originator, String sourceType,  String sourceRef, String eventType) {
		this.originator = originator;
		this.sourceRef = sourceRef;
		this.sourceType = sourceType;
		this.eventType = eventType;
		
		//generate UUID for id
		this.id = IdUtil.getUniqueId("Event");
		
		//populate created time
		this.createdTime = new Date();
	}
	
	@PrePersist
	public void prePersist() throws Exception{
		if(this.id == null) {
			this.id = IdUtil.getUniqueId("Event");
		}
		if(this.createdTime == null) {
			this.createdTime = new Date();
		}
		
		//convert body to jsonBody
		if(this.body != null)
			this.bodyJson = JsonConverter.instance().toJson(this.body);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getSourceRef() {
		return sourceRef;
	}

	public void setSourceRef(String sourceRef) {
		this.sourceRef = sourceRef;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}


	public String getAdditionalInfo() {
		return additionalInfo;
	}

	public void setAdditionalInfo(String additionalInfo) {
		this.additionalInfo = additionalInfo;
	}


	public String getOriginator() {
		return originator;
	}

	public void setOriginator(String originator) {
		this.originator = originator;
	}

	public Map<String, Object> getContext() {
		return context;
	}

	public void setContext(Map<String, Object> context) {
		this.context = context;
	}

	public String getBodyJson() {
		return bodyJson;
	}

	public void setBodyJson(String bodyJson) {
		this.bodyJson = bodyJson;
	}

	public Object getParameter(String key) {
		if(body == null) {
			return null;
		} else {
			return body.get(key);
		}
	}
	
	public Map<String, Object> getBody(){
		if(body == null && bodyJson != null && bodyJson.length() > 0) {
			try {
				this.body = JsonConverter.instance().toHashMap(this.bodyJson, String.class, Object.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return body;
	}

	public void setBody(Map<String, Object> body) {
		this.body = body;
	}
	
	public void addParameter(String key, Object value) {
		if(body == null) {
			body = new HashMap<String, Object>();
		}
		body.put(key, value);
	}
	
	@Override
	public String toString() {
		return String.format("%s-%s-%s-%s", id, sourceType, sourceRef, eventType);
	}

}
