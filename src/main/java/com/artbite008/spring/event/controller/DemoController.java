package com.artbite008.spring.event.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.artbite008.spring.event.builder.DesignFileEventFactory;
import com.artbite008.spring.event.domain.DurableEvent;
import com.artbite008.spring.event.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("${mvc.prefix}/demo")
public class DemoController extends BaseController {


	@Autowired
	private EventService eventService;
	
	
	@RequestMapping(value="/publish", method=RequestMethod.GET)
	@ResponseBody
	public String demo(HttpServletRequest request, HttpServletResponse response) {
		DurableEvent event = DesignFileEventFactory.fileConvertEvent("test", "", "", "", "", "", (new Date()).toString());
		eventService.publishDurableEvent(event);
		return "ok";
	}

	
}
