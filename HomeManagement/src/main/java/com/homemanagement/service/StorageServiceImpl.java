package com.homemanagement.service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.homemanagement.domain.FOTA;
import com.homemanagement.exception.StorageException;
import com.homemanagement.repositories.FOTARepositoryImpl;
import com.homemanagement.utils.ConfigurationUploadRequest;
import com.homemanagement.utils.UploadRequest;


@Service("storageService")
public class StorageServiceImpl implements StorageService {

    private static final Logger log = LoggerFactory.getLogger(StorageServiceImpl.class);

    
    @Autowired
	private Environment environment;
    
    @Autowired
    private FOTARepositoryImpl fotaRepository;

	
    @Override
    public void save(UploadRequest ur) {
     Path basePath=Paths.get(environment.getProperty("fota.file.path"));
        if (ur.getFile()==null) {
            throw new StorageException(String.format("File with uuid = [%s] is empty", ur.getUuid().toString()));
        }
       
        ByteArrayInputStream bis = new ByteArrayInputStream(ur.getFile());
        Path targetFile;
        if (ur.getPartIndex() > -1) {
            targetFile = basePath.resolve(ur.getUuid()).resolve(String.format("%s_%05d", ur.getUuid(), ur.getPartIndex()));
        } else {
            targetFile = basePath.resolve(ur.getUuid()).resolve(ur.getFileName());
        }
        try {
            Files.createDirectories(targetFile.getParent());
            Files.copy(bis, targetFile);
        } catch (IOException e) {
            String errorMsg = String.format("Error occurred when saving file with uuid = [%s]", e);
            log.error(errorMsg, e);
            throw new StorageException(errorMsg, e);
        }

    }

  

    @Override
    public void mergeChunks(String uuid, String fileName, int totalParts, long totalFileSize,String version,String type,String device_id,String checkSum,String description) {
    	 		Path basePath=Paths.get(environment.getProperty("fota.file.path"));
    	   		File targetFile = basePath.resolve(uuid).resolve(fileName).toFile();
    	   
    	   		try (@SuppressWarnings("resource")
				FileChannel dest = new FileOutputStream(targetFile, true).getChannel()) {
    	   			for (int i = 1; i <= totalParts; i++) {
    	   				File sourceFile = basePath.resolve(uuid).resolve(String.format("%s_%05d", uuid, i)).toFile();
    	   				try (@SuppressWarnings("resource")
						FileChannel src = new FileInputStream(sourceFile).getChannel()) {
                    dest.position(dest.size());
                    src.transferTo(0, src.size(), dest);
                }
                sourceFile.delete();
            }
    	   			if (checkSum.equals(checkSumApacheCommons(basePath+"/"+uuid+"/"+fileName))) {
                      saveToDB(fileName,type,device_id,version,basePath+"/"+uuid+"/"+fileName,description);
    	   	       }else {
     	   		String errorMsg = String.format("CheckSum not matching", uuid);
     	   		throw new StorageException(errorMsg);
         }
        	} catch (IOException e) {
            String errorMsg = String.format("Error occurred when merging chunks for uuid = [%s]", uuid);
            log.error(errorMsg, e);
            
            throw new StorageException(errorMsg, e);
        }	
    }
    
	private void saveToDB(String fileName, String type, String device_id, String version, String filePath,String description) {
		try {
			 
			  FOTA fota=new FOTA(device_id, version, description, filePath);
			  fotaRepository.addFotaUploadedPath(fota);
		    }catch (Exception e) {
             log.error(e.getMessage());
	        }
		
	}



	public  String checkSumApacheCommons(String file){
        String checksum = null;
        try {  
            checksum = DigestUtils.md5Hex(new FileInputStream(file));
        } catch (IOException ex) {
        	   ex.printStackTrace();
        }
        return checksum;
    }



	@Override
	public void saveConfiguration(ConfigurationUploadRequest confUploadReq) {
		Path basePath=Paths.get(environment.getProperty("config.file.path"));
        if (confUploadReq.getFile()==null) {
            throw new StorageException(String.format("File with uuid = [%s] is empty", confUploadReq.getUuid().toString()));
        }
       
        ByteArrayInputStream bis = new ByteArrayInputStream(confUploadReq.getFile());
        Path targetFile;
            targetFile = basePath.resolve(confUploadReq.getUuid()).resolve(confUploadReq.getFileName());
        try {
            Files.createDirectories(targetFile.getParent());
            Files.copy(bis, targetFile);
        } catch (IOException e) {
            String errorMsg = String.format("Error occurred when saving file with uuid = [%s]", e);
            log.error(errorMsg, e);
            throw new StorageException(errorMsg, e);
        }
		
	}
}