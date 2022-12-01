package com.homemanagement.service.impl;

import com.homemanagement.controller.HomeController;
import com.homemanagement.domain.DeviceNetwork;
import com.homemanagement.domain.FOTA;
import com.homemanagement.domain.HomeExpendature;
import com.homemanagement.dto.ServiceStatus;
import com.homemanagement.repositories.DeviceMasterRepository;
import com.homemanagement.repositories.FOTARepository;
import com.homemanagement.repositories.HomeExpedatureRepository;
import com.homemanagement.service.HomeService;
import com.homemanagement.service.StorageServiceImpl;
import com.homemanagement.utils.HomeManagementUtil;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class HomeServiceImpl implements HomeService {
    @Autowired
    HomeExpedatureRepository homeRepository;
    @Autowired
    FOTARepository fotaRepository;
    @Autowired
    private Environment environment;
    @Autowired
    DeviceMasterRepository deviceMasterRepository;
    @Autowired
    StorageServiceImpl storageService;
    /** The Constant logger is used to specify the . */
    static final Logger logger = Logger.getLogger(HomeController.class);
    ServiceStatus<Object> serviceStatus=new ServiceStatus<>();
    public ServiceStatus<Object> createRole(HomeExpendature homeExp) {
        if(homeExp !=null && homeExp.getItem_id()!=null && homeExp.getItem_id()!=null)
        {
            try {
                logger.info("addExpendature"+ homeExp.getItem_id());

                String itemId = UUID.randomUUID().toString();
                homeExp.setId(itemId);
                homeRepository.addDevice(homeExp);
                serviceStatus.setStatus("success");
                serviceStatus.setMessage("successfully added home expedature ");

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
            logger.debug("Invalid Device payload");
            serviceStatus.setStatus("failure");
            serviceStatus.setMessage("Invalid Device payload");
        }

        return serviceStatus;
    }
    public ServiceStatus<Object> checkHomeExp(String company_id, boolean is_active) {
        if(company_id !=null){

            try {
                logger.info("getDeviceListByCompanyIdAndActiveStatus"+ company_id);

                List<HomeExpendature> deviceList	= homeRepository.getDeviceListByCompanyIdAndActiveStatus(company_id, is_active);
                if(deviceList!=null&& deviceList.size()>0){
                    logger.info("isDeviceSeqNoExists"+deviceList);
                    serviceStatus.setStatus("success");
                    serviceStatus.setMessage("successfully fetched");
                    serviceStatus.setResult(deviceList);
                }
                else {
                    serviceStatus.setStatus("failure");
                    serviceStatus.setMessage("Devices not Exist");
                    serviceStatus.setResult(deviceList);
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
   public ServiceStatus<Object> getSingleItem(String id) {
        try {
            if(id != null){
                logger.info("getSingleItemById"+ id);
                HomeExpendature item = homeRepository.getItemeByItemId(id);
                if( null != item){
                    logger.info("Item EXIST : "+item);
                    serviceStatus.setStatus("success");
                    serviceStatus.setMessage("Item successfully fetched");
                    serviceStatus.setResult(item);
                }
                else {
                    serviceStatus.setStatus("failure");
                    serviceStatus.setMessage("Item not Exist");
                }
            }else{
                serviceStatus.setStatus("failure");
                serviceStatus.setMessage("Invalid param values ");
            }

        } catch (Exception e) {
            e.printStackTrace();
            serviceStatus.setStatus("failure");
            serviceStatus.setMessage("failure");
            if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
                serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
            }
        }

        return serviceStatus;
    }
    public ServiceStatus<Object> getHomeExp(String device_id, String version) {
        if(device_id !=null&& version !=null){

            try {
                logger.info("getFotaPath"+ device_id);

                FOTA fota	= fotaRepository.getFOTAPath(device_id, version);
                if(fota!=null){

                    serviceStatus.setStatus("success");
                    serviceStatus.setMessage("successfully fetched");
                    serviceStatus.setResult(fota);
                }
                else {
                    serviceStatus.setStatus("failure");
                    serviceStatus.setMessage("Device Id not Exist");
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
    public ServiceStatus<Object> getHomeExpById(String company_id, boolean is_active, Integer page, Integer size, String sort) {
        if(company_id !=null){

            try {


                Sort sortCriteria=null;
                if(!HomeManagementUtil.isEmptyString(sort))
                {
                    logger.info("sort"+ sort);
                    sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, sort));
                }
                else {
                    sortCriteria = new Sort(new Sort.Order(Sort.Direction.ASC, "device_name"));

                }
                Pageable pageable = PageRequest.of(page, size,sortCriteria);
                logger.info("getDeviceListByCompanyIdAndActiveStatus"+ company_id);

                Page<HomeExpendature> deviceList	= homeRepository.getDeviceListByCompanyIdAndActiveStatusPageable(company_id, is_active, pageable);
                if(deviceList!=null){
                    logger.info("deviceList"+deviceList);
                    serviceStatus.setStatus("success");
                    serviceStatus.setMessage("successfully fetched");
                    serviceStatus.setResult(deviceList);
                }
                else {
                    serviceStatus.setStatus("failure");
                    serviceStatus.setMessage("Devices not Exist");
                    serviceStatus.setResult(deviceList);
                }

            } catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus("failure");
                serviceStatus.setMessage("failure");

            }
        }else{
            serviceStatus.setStatus("failure");
            serviceStatus.setMessage("Invalid param values ");
        }

        return serviceStatus;
    }
   public ServiceStatus<Object> changeCompany(String currentCompanyId, String destCompanyId,String device_id) {
        try {
            if(currentCompanyId !=null&& destCompanyId !=null&& device_id !=null) {
                String deviceToken = HomeManagementUtil.generateDeviceToken(destCompanyId.trim(), device_id.trim());
                homeRepository.changeDeviceCompany(currentCompanyId, destCompanyId, device_id,deviceToken);
                serviceStatus.setStatus("success");
                serviceStatus.setMessage("successfully updated company name");
            }else {
                serviceStatus.setStatus("failure");
                serviceStatus.setMessage("Invalid Params");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            serviceStatus.setStatus("failure");
            serviceStatus.setMessage("failure");
        }
        return serviceStatus;
    }
    public ServiceStatus<Object> updateHomeExp(HomeExpendature homeExp) {
        try {

            if(homeExp.getItem_id()!=null&& homeExp.getItem_name()!=null&& homeExp.getItem_type()!=null) {
                HomeExpendature existItem= homeRepository.getItemeByItemId(homeExp.getId());
                existItem.setItem_id(homeExp.getItem_id());
                existItem.setItem_name(homeExp.getItem_name());
                existItem.setItem_type(homeExp.getItem_type());
                existItem.setItem_price(homeExp.getItem_price());
                existItem.setItem_purchase_date(homeExp.getItem_purchase_date());
                existItem.setItem_status(homeExp.getItem_status());
                homeRepository.updateItem(homeExp);
                serviceStatus.setStatus("success");
                serviceStatus.setMessage("successfully updated item Details");
            }else {
                serviceStatus.setStatus("failure");
                serviceStatus.setMessage("Invalid Params");
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            serviceStatus.setStatus("failure");
            serviceStatus.setMessage("failure");
        }
        return serviceStatus;
    }
    public void getHomeExpReport(String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String path=environment.getProperty("configPath");
        byte[] reportBytes = null;
        File result=new File(path+"/"+ fileName);
        System.out.println("remote Address" +   request.getRemoteAddr());
        System.out.println("remote host" +   request.getRemoteHost());
        System.out.println("remote port" +   request.getRemotePort());
        System.out.println("remote getLocalAddr" +   request.getLocalAddr());
        System.out.println("remote getLocalPort" +   request.getLocalPort());
        //System.out.println("remote port" +   request.get());


        Enumeration<?> headerNames = request.getHeaderNames();
        while(headerNames.hasMoreElements()) {
            String headerName = (String)headerNames.nextElement();
            System.out.println("name" + headerName+"value" + request.getHeader(headerName));
        }

        if(result.exists()){
            InputStream inputStream = new FileInputStream(path+"/"+ fileName);

            String type = Files.probeContentType(result.toPath());
            response.setHeader("Content-Disposition", "attachment; filename=" + result.getName());
            response.setHeader("Content-Type",type);

            reportBytes=new byte[100];//New change
            OutputStream os= response.getOutputStream();//New change
            int read=0;
            while((read=inputStream.read(reportBytes))!=-1){
                os.write(reportBytes,0,read);
            }

            os.flush();
            os.close();
            inputStream.close();
        }
    }
    public ServiceStatus<Object> getHomeWeeklyReport(String info) {
        try {
            logger.info("serverAResponse");
            System.out.println("serverAResponse"+ info);
            String url_path=environment.getProperty("gateway");
            if(info !=null){

                serviceStatus.setStatus("success");
                serviceStatus.setMessage("successfully fetched");
                Thread.sleep(5000);
                HomeManagementUtil.loadDeviceToken("serverB");
            }
            else {
                serviceStatus.setStatus("failure");
                serviceStatus.setMessage("Exist");
            }

        } catch (Exception e) {
            e.printStackTrace();
            serviceStatus.setStatus("failure");
            serviceStatus.setMessage("failure");
            if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
                serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
            }
        }

        return serviceStatus;
    }
    public ServiceStatus<Object> getProperty(String info) {
        try {
            System.out.println("serverBResponse"+ info);

            if(info !=null){

                serviceStatus.setStatus("success");
                serviceStatus.setMessage("successfully fetched");
            }
            else {
                serviceStatus.setStatus("failure");
                serviceStatus.setMessage("Exist");
            }

        } catch (Exception e) {
            e.printStackTrace();
            serviceStatus.setStatus("failure");
            serviceStatus.setMessage("failure");
            if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
                serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
            }
        }

        return serviceStatus;
    }
    public ServiceStatus<Object> getPropertyExp() {
        try {
            List<DeviceNetwork> deviceNetworsByNetworkId = homeRepository.getDeviceNetworsByNetworkId();
            serviceStatus.setStatus("success");
            serviceStatus.setMessage("successfully fetched");
            serviceStatus.setResult(deviceNetworsByNetworkId);
        }catch (Exception e) {
            e.printStackTrace();
            serviceStatus.setStatus("failure");
            serviceStatus.setMessage("failure");
            if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
                serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
            }
        }

        return serviceStatus;
    }
    public ServiceStatus<Object> getExp(String userId) {
        try {
            logger.info("getHomeExpendature method");

            List<HomeExpendature> itemList	= homeRepository.getItemByUserId(userId);
            if(itemList!=null&& itemList.size()>0){
                List<HomeExpendature> listWithoutDuplicates = itemList.stream().distinct().collect(Collectors.toList());
                logger.info("listWithoutDuplicates"+listWithoutDuplicates);
                serviceStatus.setStatus("success");
                serviceStatus.setMessage("successfully fetched");
                serviceStatus.setResult(listWithoutDuplicates);
            }
            else {
                serviceStatus.setStatus("failure");
                serviceStatus.setMessage("Item does not Exist");
            }

        } catch (Exception e) {
            e.printStackTrace();
            serviceStatus.setStatus("failure");
            serviceStatus.setMessage("failure");
            if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
                serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
            }
        }
        return serviceStatus;
    }
    public ServiceStatus<?> getPropertyReport(String device_mac_id, String device_key) {
        if(device_mac_id !=null){

            try {
                logger.info("Fetch device data"+ device_mac_id);

                boolean isDeviceSeqNoExists = homeRepository.checkDeviceExists(device_mac_id, device_key);
                if(isDeviceSeqNoExists) {
                    HomeExpendature expData = homeRepository.getItemeByItemId(device_mac_id);
                    serviceStatus.setStatus("success");
                    serviceStatus.setMessage("Device already has existed");
                    serviceStatus.setResult(expData);
                    if(expData != null) {
                        List<HomeExpendature> expList	= homeRepository.getDeviceByParentId(null);
                        logger.info("Fetch device data"+expList);
                    }

                }else {
                    serviceStatus.setMessage("Device does not exist");
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
            serviceStatus.setMessage("invalid param values ");
        }

        return serviceStatus;
    }
    public ServiceStatus<Object> addExp(DeviceNetwork deviceNetwork) {
        if(deviceNetwork !=null)
        {
            try {
                String networkId = UUID.randomUUID().toString();
                deviceNetwork.setId(networkId);

                homeRepository.addNetwork(deviceNetwork);
                serviceStatus.setStatus("success");
                serviceStatus.setMessage("successfully registered Device ");
                serviceStatus.setResult(networkId);


            }catch (Exception e) {
                e.printStackTrace();
                serviceStatus.setStatus("failure");
                serviceStatus.setMessage("failure");
                if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
                    serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
                }
            }

        }else {
            logger.debug("Invalid Device payload");
            serviceStatus.setStatus("failure");
            serviceStatus.setMessage("Invalid Device payload");
        }
        return serviceStatus;
    }
    public ServiceStatus<?> getPropertyExpById(String device_mac_id) {
        if(device_mac_id !=null){

            try {
                logger.info("Fetch device data"+ device_mac_id);

                HomeExpendature diviceData = homeRepository.getItemeByItemId(device_mac_id);
                serviceStatus.setStatus("success");
                serviceStatus.setMessage("Device already has existed");
                serviceStatus.setResult(diviceData);

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
            serviceStatus.setMessage("invalid param values ");
        }

        return serviceStatus;
    }
    public ServiceStatus<Object> getAllHomeExps(String network_id) {
        try {
            DeviceNetwork deviceNetworsByNetworkId = homeRepository.getDeviceNetworkByNetworkId(network_id);
            serviceStatus.setStatus("success");
            serviceStatus.setMessage("successfully fetched");
            serviceStatus.setResult(deviceNetworsByNetworkId);
        }catch (Exception e) {
            e.printStackTrace();
            serviceStatus.setStatus("failure");
            serviceStatus.setMessage("failure");
            if(e instanceof org.springframework.dao.DataIntegrityViolationException) {
                serviceStatus.setMessage("DATAINTGRTY_VIOLATION");
            }
        }

        return serviceStatus;
    }
}
