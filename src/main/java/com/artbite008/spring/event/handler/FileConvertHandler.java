package com.artbite008.spring.event.handler;

import com.artbite008.spring.event.common.IEventProcessor;
import com.artbite008.spring.event.domain.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


public class FileConvertHandler implements IEventProcessor {

    private Logger logger = LoggerFactory.getLogger(this.getClass());


    @Transactional(propagation=Propagation.REQUIRES_NEW)
    public void processEvent(Event event) {

        Map<String, Object> bodyMap = event.getBody();
        String crId = (String)bodyMap.get("collaborationRoomId");
        String tenantId = (String)bodyMap.get("tenantId");
        String userName = (String)bodyMap.get("userName");
        String originalFileName = (String)bodyMap.get("originalFileName");
        String originalFileId = (String)bodyMap.get("originalFileId");
        String targetFolderId = (String)bodyMap.get("targetFolderId");

        if(logger.isDebugEnabled()){
            logger.debug("FileConvertHandler.processEvent::  collaborationRoomId = {} originalFileName = {} originalFileId = {} userName = {} tenantId ={} targetFolderId {} ",
                    crId, originalFileName, originalFileId, userName, tenantId, targetFolderId);
        }


        if(logger.isDebugEnabled()){
            logger.debug("FileConvertHandler.processEvent::  convert to zip file {} end.", originalFileName);
        }

    }

    public boolean support(Event event) {
        return true;
    }

}
