package com.homemanagement.repositories;

import com.homemanagement.domain.FOTA;


public interface FOTARepositoryBase {

	void addFotaUploadedPath(FOTA fota);

	void updateFota(FOTA fota);
	
	FOTA getFOTAPath( String device_id,String version);

}
