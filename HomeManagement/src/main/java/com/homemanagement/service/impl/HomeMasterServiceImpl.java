package com.homemanagement.service.impl;

import com.google.gson.Gson;
import com.homemanagement.domain.DeviceMaster;
import com.homemanagement.dto.ServiceStatus;
import com.homemanagement.repositories.DeviceMasterRepository;
import com.homemanagement.repositories.HomeExpedatureRepository;
import com.homemanagement.service.HomeMasterService;
import com.homemanagement.utils.HomeManagementUtil;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
@Service
public class HomeMasterServiceImpl implements HomeMasterService {
    @Autowired
    DeviceMasterRepository deviceMasterRepository;
    @Autowired
    HomeExpedatureRepository deviceRepository;
    /** The Constant logger is used to specify the . */
    static final Logger logger = Logger.getLogger(HomeMasterServiceImpl.class);
    ServiceStatus<Object> serviceStatus = new ServiceStatus<>();
    public ServiceStatus<Object> createMonthlyReport(String device) {
        DeviceMaster byDeviceSeqNum;
        String firstSubString;
        String secondSubString;
        Map<String,String> hashMap = new HashMap<String,String>();
        String[] splitDevice = device.split(",");
        for(String deviceFields : splitDevice) {
            Pattern p = Pattern.compile("(.*?)" + "=" + "(.*)");
            Matcher m = p.matcher(deviceFields);
            if (m.matches()) {
                firstSubString = m.group(1);
                secondSubString = m.group(2);
                hashMap.put(firstSubString, secondSubString);
            }
        }


        DeviceMaster deviceMaster = new DeviceMaster();
        deviceMaster.setModel_name(hashMap.get("model_name"));
        deviceMaster.setDevice_mac_id(hashMap.get("device_mac_id"));
        deviceMaster.setHardware_key(hashMap.get("hardware_key"));
        deviceMaster.setSerial_number(hashMap.get("serial_number"));
        deviceMaster.setHardware_version(hashMap.get("hardware_version"));
        String devices = new Gson().toJson(hashMap);
        deviceMaster.setMiscellaneous(devices);

        if(deviceMaster!=null && deviceMaster.getDevice_mac_id() != null || !HomeManagementUtil.isEmptyString(deviceMaster.getDevice_mac_id())) {
            try {
                DeviceMaster byDeviceSeqNumber = deviceMasterRepository.getByDeviceMacId(deviceMaster.getDevice_mac_id(), null);
                if(byDeviceSeqNumber != null) {
                    deviceMaster.setId(byDeviceSeqNumber.getId());
                    deviceMaster.setCreated_date(byDeviceSeqNumber.getCreated_date());
                    deviceMaster.setLast_updated(byDeviceSeqNumber.getLast_updated());
                    deviceMaster.setDevice_type(byDeviceSeqNumber.getDevice_type());
                }
                String deviceMasters = new Gson().toJson(deviceMaster);
                String byDeviceSeqNumbers = new Gson().toJson(byDeviceSeqNumber);
                if(deviceMasters.equals(byDeviceSeqNumbers)){
                    logger.info("isDeviceSeqNoExists"+byDeviceSeqNumber.getDevice_mac_id());
                    serviceStatus.setStatus("success");
                    serviceStatus.setMessage("Device already has existed");
                    byDeviceSeqNum = deviceMasterRepository.getByDeviceMacId(deviceMaster.getDevice_mac_id(), null);
                    serviceStatus.setResult(byDeviceSeqNum);
                }else {
                    logger.info("createMasterDevice"+deviceMaster.getDevice_mac_id());

                    boolean isDeviceSeqNoExists = deviceMasterRepository.checkDeviceExists(deviceMaster.getDevice_mac_id(),null );
                    if(isDeviceSeqNoExists) {
                        LocalDateTime localDateTime = HomeManagementUtil.convertTolocalDateTimeFrom(new Date());
                        deviceMaster.setLast_updated(localDateTime);
                        deviceMasterRepository.updatePhoneHomeDevice(deviceMaster);
                        serviceStatus.setStatus("success");
                        serviceStatus.setMessage("Device has been updated succssfully");
                        byDeviceSeqNum =  deviceMasterRepository.getByDeviceMacId(deviceMaster.getDevice_mac_id(), null);
                        serviceStatus.setResult(byDeviceSeqNum);
                    } else {
                        String deviceMasterId = UUID.randomUUID().toString(); deviceMaster.setId(deviceMasterId);
                        deviceMaster.setDevice_type("Mesh Router"); LocalDateTime localDateTime = HomeManagementUtil.convertTolocalDateTimeFrom(new Date());
                        deviceMaster.setCreated_date(localDateTime);
                        deviceMasterRepository.addDeviceMaster(deviceMaster);
                        serviceStatus.setStatus("success");
                        serviceStatus.setMessage("Device has been added succssfully");
                        byDeviceSeqNum  = deviceMasterRepository.getByDeviceMacId(deviceMaster.getDevice_mac_id(), null);
                        serviceStatus.setResult(byDeviceSeqNum);
                    }
                    return serviceStatus;
                }

            }catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus("failure"); serviceStatus.setMessage("failure");
                if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
                }
            } }else {
            logger.debug("invalid phone home Device payload");
            serviceStatus.setStatus("failure");
            serviceStatus.setMessage("invalid phone home Device payload");
        }
        return serviceStatus;
    }
    public  ServiceStatus<?> downloadMonthlyReport(String device_mac_id, String device_key) {
        if (device_mac_id != null) {

            try {
                logger.info("Fetch Phonehome data" + device_mac_id);

                boolean isDeviceSeqNoExists = deviceMasterRepository.checkDeviceExists(device_mac_id, device_key);
                if (isDeviceSeqNoExists) {
                    DeviceMaster phoneHomeData = deviceMasterRepository.getByDeviceMacId(device_mac_id, device_key);
                    serviceStatus.setStatus("success");
                    serviceStatus.setMessage("Device already has existed");
                    serviceStatus.setResult(phoneHomeData);
                    if (phoneHomeData != null) {
                        logger.info("Fetch phoneHomeData" + phoneHomeData);
                    }

                } else {
                    serviceStatus.setMessage("Device does not exist");
                }
            } catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus("failure");
                serviceStatus.setMessage("failure");
                if (e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
                }
            }
        } else {
            serviceStatus.setStatus("failure");
            serviceStatus.setMessage("invalid param values ");
        }

        return serviceStatus;
    }

}
