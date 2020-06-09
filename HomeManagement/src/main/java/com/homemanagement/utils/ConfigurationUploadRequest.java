package com.homemanagement.utils;





public class ConfigurationUploadRequest {

    private final String uuid;
    private final byte[] file;
    private String fileName;

    public ConfigurationUploadRequest(String uuid, byte[] file) {
        this.uuid = uuid;
        this.file = file;
    }

    public String getUuid() {
        return uuid;
    }

    public byte[] getFile() {
        return file;
    }

    

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

   
}