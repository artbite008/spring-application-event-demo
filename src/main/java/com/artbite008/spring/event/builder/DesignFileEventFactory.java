package com.artbite008.spring.event.builder;


import com.artbite008.spring.event.domain.DurableEvent;

public class DesignFileEventFactory {

	public static final String ST_DF = "DesignFile";

	public static final String DESIGN_FILE_UNZIP = "Unzip";

	public static final String DESIGN_FILE_CONVERT = "Convert";

	public static DurableEvent fileConvertEvent(String tenantId,
												String userName,
												String originalFileId,
												String targetFolderId,
												String originalFileName,
												String crId,
												String organizationBpId) {

		DurableEvent e = new DurableEvent(organizationBpId, ST_DF, crId, DESIGN_FILE_CONVERT);
		e.addParameter("collaborationRoomId", crId);
		e.addParameter("tenantId", tenantId);
		e.addParameter("userName", userName);
		e.addParameter("originalFileId", originalFileId);
		e.addParameter("targetFolderId", targetFolderId);
		e.addParameter("originalFileName", originalFileName);
		return e;
	}


}
