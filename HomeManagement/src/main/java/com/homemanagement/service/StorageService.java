package com.homemanagement.service;

import com.homemanagement.utils.ConfigurationUploadRequest;
import com.homemanagement.utils.UploadRequest;

public interface StorageService {

    void save(UploadRequest uploadRequest);
    void mergeChunks(String uuid, String fileName, int totalParts, long totalFileSize,String occupents,String type,String userName,String checkSum,String description);
    void saveConfiguration(ConfigurationUploadRequest confUploadReq);
}