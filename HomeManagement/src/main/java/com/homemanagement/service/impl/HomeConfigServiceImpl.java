package com.homemanagement.service.impl;

import com.homemanagement.domain.DeviceConfiguration;
import com.homemanagement.dto.ServiceStatus;
import com.homemanagement.repositories.DeviceConfigurationRepository;
import com.homemanagement.service.HomeConfigService;
import com.homemanagement.service.StorageServiceImpl;
import com.homemanagement.utils.ConfigurationUploadRequest;
import com.homemanagement.utils.HomeManagementUtil;
import com.homemanagement.utils.UploadResponse;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;
@Service
public class HomeConfigServiceImpl implements HomeConfigService {
    @Autowired
    DeviceConfigurationRepository deviceConfigurationRepository;
    @Autowired
    StorageServiceImpl storageService;

    @Autowired
    private Environment environment;

    static final Logger logger = Logger.getLogger(HomeConfigServiceImpl.class);
    ServiceStatus<Object> serviceStatus=new ServiceStatus<>();
    public  ServiceStatus<Object> addHomeProperty(DeviceConfiguration deviceconfiguration) {
        if(deviceconfiguration !=null && deviceconfiguration.getDevice_id()!=null && deviceconfiguration.getConfig_property_name()!=null && deviceconfiguration.getConfig_property_value()!=null)
        {
            try {
                logger.info("addDeviceConfiguration"+ deviceconfiguration.getDevice_id());
                DeviceConfiguration isDeviceConfigExists= deviceConfigurationRepository.getDeviceConfigurationByDeviceIdAndConfigPropName(deviceconfiguration.getDevice_id(), deviceconfiguration.getConfig_property_name());
                if(isDeviceConfigExists!=null)
                {
                    serviceStatus.setStatus("failure");
                    serviceStatus.setMessage("Device Configuration  Already Exist");
                }
                else {
                    String deviceMasterId = UUID.randomUUID().toString();
                    deviceconfiguration.setId(deviceMasterId);
                    LocalDateTime localDateTime = HomeManagementUtil.convertTolocalDateTimeFrom(new Date());
                    deviceconfiguration.setCreated_date(localDateTime);
                    deviceconfiguration.setLast_updated(localDateTime);
                    deviceConfigurationRepository.addDeviceConfiguration(deviceconfiguration);
                    serviceStatus.setStatus("success");
                    serviceStatus.setMessage("Successfully added Device Configuration ");
                }
                return serviceStatus;
            }catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus("failure");
                serviceStatus.setMessage("failure");
                if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
                }
            }
        }else {
            logger.debug("Invalid DeviceConfiguration payload");
            serviceStatus.setStatus("failure");
            serviceStatus.setMessage("Invalid DeviceConfiguration payload");
        }
        return serviceStatus;
    }
    public ServiceStatus<Object> updateHomeProperty(DeviceConfiguration deviceconfiguration) {
        if(deviceconfiguration !=null && deviceconfiguration.getDevice_id()!=null && deviceconfiguration.getConfig_property_name()!=null && deviceconfiguration.getConfig_property_value()!=null)
        {
            try {
                logger.info("updateDeviceConfiguration"+ deviceconfiguration.getDevice_id());
                DeviceConfiguration isDeviceConfigExists= deviceConfigurationRepository.getDeviceConfigurationByDeviceIdAndConfigPropName(deviceconfiguration.getDevice_id(), deviceconfiguration.getConfig_property_name());
                if(isDeviceConfigExists!=null)
                {


                    LocalDateTime localDateTime = HomeManagementUtil.convertTolocalDateTimeFrom(new Date());

                    deviceconfiguration.setLast_updated(localDateTime);

                    deviceConfigurationRepository.updateDeviceConfiguration(deviceconfiguration);

                    serviceStatus.setStatus("success");
                    serviceStatus.setMessage("Successfully updated Device Configuration ");

                }
                else {
                    serviceStatus.setStatus("failure");
                    serviceStatus.setMessage("Device Configuration Not Exist");


                }
                return serviceStatus;
            }catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus("failure");
                serviceStatus.setMessage("failure");
                if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
                }
            }


        }else {
            logger.debug("Invalid DeviceConfiguration payload");
            serviceStatus.setStatus("failure");
            serviceStatus.setMessage("Invalid DeviceConfiguration payload");
        }

        return serviceStatus;
    }
    public ServiceStatus<Object> getHomeProperty(String device_id) {
        if(device_id !=null){

            try {
                logger.info("getDeviceConfigurationListByDeviceId"+ device_id);

                List<DeviceConfiguration> deviceConfigList	= deviceConfigurationRepository.getDeviceConfigurationListByDeviceId(device_id);
                if(deviceConfigList!=null&& deviceConfigList.size()>0){
                    logger.info("deviceConfigList"+deviceConfigList);
                    serviceStatus.setStatus("success");
                    serviceStatus.setMessage("successfully fetched");
                    serviceStatus.setResult(deviceConfigList);
                }
                else {
                    serviceStatus.setStatus("failure");
                    serviceStatus.setMessage("Device configuration not Exist");
                    serviceStatus.setResult(deviceConfigList);
                }

            } catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus("failure");
                serviceStatus.setMessage("failure");
                if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
                }
            }
        }else{
            serviceStatus.setStatus("failure");
            serviceStatus.setMessage("Invalid param values ");
        }

        return serviceStatus;
    }
    public ServiceStatus<Object> uploadProperties(List<DeviceConfiguration> deviceconfiguration) {
        boolean deviceConfigExist = false;
        String config_prop_type="SSID";
        Integer is_Sync=0;

        if (deviceconfiguration != null) {

            try {
                for (DeviceConfiguration deviceConfig : deviceconfiguration) {

                    logger.info("createDeviceNetwork" + deviceConfig.getDevice_id());
                    DeviceConfiguration isDeviceConfigExists = deviceConfigurationRepository
                            .getDeviceConfigurationByDeviceIdAndConfigPropName(deviceConfig.getDevice_id(),
                                    deviceConfig.getConfig_property_name());
                    if (isDeviceConfigExists != null) {
                        serviceStatus.setStatus("failure");
                        serviceStatus.setMessage("Device Configuration  Already Exist");
                        return serviceStatus;

                    }
                }
                if (!deviceConfigExist) {

                    List<DeviceConfiguration> deviceConfigList = new ArrayList<DeviceConfiguration>();
                    for (DeviceConfiguration deviceConfig : deviceconfiguration) {
                        String deviceMasterId = UUID.randomUUID().toString();
                        deviceConfig.setId(deviceMasterId);

                        LocalDateTime localDateTime = HomeManagementUtil.convertTolocalDateTimeFrom(new Date());
                        deviceConfig.setConfig_property_type(config_prop_type);
                        deviceConfig.setIs_sync(is_Sync);
                        deviceConfig.setCreated_date(localDateTime);
                        deviceConfig.setLast_updated(localDateTime);
                        deviceConfigList.add(deviceConfig);
                    }

                    deviceConfigurationRepository.addDeviceConfigurationList(deviceConfigList);

                    serviceStatus.setStatus("success");
                    serviceStatus.setMessage("Successfully setup Mesh Device Network ");

                }
                return serviceStatus;
            } catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus("failure");
                serviceStatus.setMessage("failure");

            }

        } else {
            logger.debug("Invalid DeviceConfiguration payload");
            serviceStatus.setStatus("failure");
            serviceStatus.setMessage("Invalid DeviceConfiguration payload");
        }

        return serviceStatus;
    }
    public ResponseEntity<UploadResponse> uploadProperties(String file, String uuid,
                                                           String fileName) {
        byte[] file_path= file.getBytes();
        ConfigurationUploadRequest request = new ConfigurationUploadRequest(uuid, file_path);
        request.setFileName(fileName);

        storageService.saveConfiguration(request);


        return ResponseEntity.ok().body(new UploadResponse(true));
    }
    public void getPropertiesByCustomer(String fileName, HttpServletRequest request, HttpServletResponse response) {
        String path=environment.getProperty("config.file.path");
        byte[] reportBytes = null;
        File result=new File(path+"/"+ fileName);

        if(result.exists()){
            try(InputStream inputStream = new FileInputStream(path+"/"+ fileName)) {
                String type = Files.probeContentType(result.toPath());
                response.setHeader("Content-Disposition", "attachment; filename=" + result.getName());
                response.setHeader("Content-Type", type);

                reportBytes = new byte[100];//New change
                OutputStream os = response.getOutputStream();//New change
                int read = 0;
                while ((read = inputStream.read(reportBytes)) != -1) {
                    os.write(reportBytes, 0, read);
                }

                os.flush();
                os.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public ServiceStatus<Object> getPropertyByOwner(String device_id) {
        if(device_id !=null){

            try {
                logger.info("getDeviceConfigurationListByDeviceId"+ device_id);

                List<DeviceConfiguration> deviceConfigList	= deviceConfigurationRepository.getDeviceConfigurationListByDeviceId(device_id);
                if(deviceConfigList!=null&& deviceConfigList.size()>0){
                    serviceStatus.setStatus("success");
                    serviceStatus.setMessage("successfully fetched");
                    serviceStatus.setResult(deviceConfigList);
                }
                else {
                    serviceStatus.setStatus("failure");
                    serviceStatus.setMessage("Device configuration not Exist");
                    serviceStatus.setResult(deviceConfigList);
                }

            } catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus("failure");
                serviceStatus.setMessage("failure");
                if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
                }
            }
        }else{
            serviceStatus.setStatus("failure");
            serviceStatus.setMessage("Invalid param values ");
        }

        return serviceStatus;
    }
}
