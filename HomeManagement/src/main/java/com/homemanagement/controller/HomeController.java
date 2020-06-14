package com.homemanagement.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.homemanagement.domain.DeviceNetwork;
import com.homemanagement.domain.FOTA;
import com.homemanagement.domain.HomeExpendature;
import com.homemanagement.dto.ServiceStatus;
import com.homemanagement.exception.StorageException;
import com.homemanagement.repositories.DeviceMasterRepository;
import com.homemanagement.repositories.FOTARepository;
import com.homemanagement.repositories.HomeExpedatureRepository;
import com.homemanagement.service.StorageServiceImpl;
import com.homemanagement.utils.HomeManagementUtil;
import com.homemanagement.utils.UploadRequest;
import com.homemanagement.utils.UploadResponse;


@RestController
@RequestMapping("/homeExp")
public class HomeController {


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
	static final Logger logger = Logger.getLogger(HomeController .class);


	@PostMapping("/addExpendature")
	ServiceStatus<Object> createRole(@RequestBody HomeExpendature homeExp){

		ServiceStatus<Object> serviceStatus=new ServiceStatus<Object>();
		if(homeExp!=null && homeExp.getItem_id()!=null && homeExp.getItem_id()!=null)
		{
			try {
				logger.info("addExpendature"+homeExp.getItem_id());

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

	@RequestMapping(value="/getDeviceListByCompanyIdAndActiveStatus",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<List<HomeExpendature>> checkDeviceMaster(@RequestParam("company_id")String company_id,@RequestParam("is_active")boolean is_active){

		ServiceStatus<List<HomeExpendature>> serviceStatus=new ServiceStatus<List<HomeExpendature>>();


		if(company_id!=null){

			try {
				logger.info("getDeviceListByCompanyIdAndActiveStatus"+company_id);

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

	@RequestMapping(value="/getDeviceByDeviceIdAndActiveStatus",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<HomeExpendature> getDeviceByDeviceIdAndActive(@RequestParam("device_id")String device_id,@RequestParam("is_active")boolean is_active){

		ServiceStatus<HomeExpendature> serviceStatus=new ServiceStatus<HomeExpendature>();


		if(device_id!=null){

			try {
				logger.info("getDeviceByDeviceIdAndActive"+device_id);

				HomeExpendature device	= homeRepository.getDeviceByDeviceIdAndActive(device_id, is_active);
				if(device!=null){
					logger.info("Dvice Exists"+device);
					serviceStatus.setStatus("success");
					serviceStatus.setMessage("successfully fetched");
					serviceStatus.setResult(device);
				}
				else {
					serviceStatus.setStatus("failure");
					serviceStatus.setMessage("Device Id not Exist");
					//serviceStatus.setResult(deviceList);
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

	@RequestMapping(value = "/uploads",produces = { "application/json"},method = RequestMethod.POST) 
	public ResponseEntity<UploadResponse> upload(
			@RequestParam("qqfile") String file,
			@RequestParam("qquuid") String uuid,
			@RequestParam("qqfilename") String fileName,
			@RequestParam(value = "qqpartindex", required = false, defaultValue = "-1") int partIndex,
			@RequestParam(value = "qqtotalparts", required = false, defaultValue = "-1") int totalParts,
			@RequestParam(value = "qqtotalfilesize", required = false, defaultValue = "-1") long totalFileSize,@RequestParam(value = "version") String version,
			@RequestParam(value = "type") String type,@RequestParam(value = "device_id") String device_id,@RequestParam(value = "checkSum") String checkSum,@RequestParam(value = "description") String description) {

		byte[] file_path=Base64.decodeBase64(file);
		UploadRequest request = new UploadRequest(uuid, file_path);
		request.setFileName(fileName);
		request.setTotalFileSize(totalFileSize);
		request.setPartIndex(partIndex);
		request.setTotalParts(totalParts);

		storageService.save(request);

		if(partIndex==totalParts) {
			storageService.mergeChunks(uuid, fileName, totalParts,totalFileSize,version,type,device_id,checkSum,description);
			return ResponseEntity.ok().body(new UploadResponse("Successfully Uploaded",true));
		}else {
			return ResponseEntity.ok().body(new UploadResponse(true));
		}
	}

	@ExceptionHandler(StorageException.class)
	public ResponseEntity<UploadResponse> handleException(StorageException ex) {
		UploadResponse response = new UploadResponse(false, ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	@RequestMapping(value="/getFotaPath",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<FOTA> getFotaPath(@RequestParam("device_id")String device_id,@RequestParam("version")String version){

		ServiceStatus<FOTA> serviceStatus=new ServiceStatus<FOTA>();


		if(device_id!=null&&version!=null){

			try {
				logger.info("getFotaPath"+device_id);

				FOTA fota	= fotaRepository.getFOTAPath(device_id, version);
				if(fota!=null){

					serviceStatus.setStatus("success");
					serviceStatus.setMessage("successfully fetched");
					serviceStatus.setResult(fota);
				}
				else {
					serviceStatus.setStatus("failure");
					serviceStatus.setMessage("Device Id not Exist");
					//serviceStatus.setResult(deviceList);
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
	@SuppressWarnings("deprecation")
	@RequestMapping(value="/getDeviceListByCompanyIdAndActiveStatusPageable",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Page<HomeExpendature>> getDeviceListByCompanyIdAndActiveStatusPageable(@RequestParam("company_id")String company_id,@RequestParam("is_active")boolean is_active,@RequestParam("page")Integer page,
			@RequestParam("size") Integer size,@RequestParam("sort") String sort){

		ServiceStatus<Page<HomeExpendature>> serviceStatus=new ServiceStatus<Page<HomeExpendature>>();


		if(company_id!=null){

			try {


				Sort sortCriteria=null;	
				if(!HomeManagementUtil.isEmptyString(sort))
				{
					logger.info("sort"+sort);
					sortCriteria = new Sort(new Sort.Order(Direction.ASC, sort));
				}
				else {
					sortCriteria = new Sort(new Sort.Order(Direction.ASC, "device_name"));

				}
				Pageable pageable = PageRequest.of(page, size,sortCriteria);
				logger.info("getDeviceListByCompanyIdAndActiveStatus"+company_id);

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
	@RequestMapping(value = "/changeDeviceCompany",produces = { "application/json"},method = RequestMethod.POST) 
	public ServiceStatus<Object> changeDeviceCompany(
			@RequestParam("currentCompanyId") String currentCompanyId,
			@RequestParam("destCompanyId") String destCompanyId,@RequestParam(value = "device_id") String device_id) {
		ServiceStatus<Object> serviceStatus=new ServiceStatus<Object>();
		try {
			if(currentCompanyId!=null&&destCompanyId!=null&&device_id!=null) {
				String deviceToken = HomeManagementUtil.generateDeviceToken(destCompanyId.trim(),device_id.trim());
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
	@PutMapping("/updateHomeExpedature")
	public ServiceStatus<Object> updateHomeExpedature(@RequestBody HomeExpendature homeExp) {
		ServiceStatus<Object> serviceStatus=new ServiceStatus<Object>();
		try {

			if(homeExp.getItem_id()!=null&&homeExp.getItem_name()!=null&&homeExp.getItem_type()!=null) {
				HomeExpendature existdevice= homeRepository.getItemeByItemId(homeExp.getItem_id());

				existdevice.setItem_name(homeExp.getItem_name());
				existdevice.setItem_type(homeExp.getItem_type());
				existdevice.setItem_status(homeExp.getItem_status());

				homeRepository.updateDevice(homeExp);
				serviceStatus.setStatus("success");
				serviceStatus.setMessage("successfully updated Device Details");
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
	@CrossOrigin(origins = "*")
	@RequestMapping(value = "/getFile", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE}, method = RequestMethod.GET)
	public void getFile(@RequestParam(value="fileName", required=false) String fileName, 
			HttpServletRequest request,HttpServletResponse response) throws IOException, Exception
	{

		String path=environment.getProperty("configPath");
		byte[] reportBytes = null;
		File result=new File(path+"/"+fileName);
		System.out.println("remote Address" +   request.getRemoteAddr());
		System.out.println("remote host" +   request.getRemoteHost());
		System.out.println("remote port" +   request.getRemotePort());
		System.out.println("remote getLocalAddr" +   request.getLocalAddr());
		System.out.println("remote getLocalPort" +   request.getLocalPort());
		//System.out.println("remote port" +   request.get());


		Enumeration<?> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String headerName = (String)headerNames.nextElement();
			System.out.println("name" + headerName+"value" +request.getHeader(headerName));
		}

		if(result.exists()){
			InputStream inputStream = new FileInputStream(path+"/"+fileName); 

			String type = Files.probeContentType(result.toPath());
			response.setHeader("Content-Disposition", "attachment; filename=" + result.getName());
			response.setHeader("Content-Type",type);

			reportBytes=new byte[100];//New change
			OutputStream os=response.getOutputStream();//New change
			int read=0;
			while((read=inputStream.read(reportBytes))!=-1){
				os.write(reportBytes,0,read);
			}

			os.flush();
			os.close();
			inputStream.close();
		}
	}
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/serverAResponse",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> serverAResponse(@RequestParam("info")String info){

		ServiceStatus<Object> serviceStatus=new ServiceStatus<Object>();
		try {
			logger.info("serverAResponse");
			System.out.println("serverAResponse"+info);
			String url_path=environment.getProperty("gateway");
			if(info!=null){

				serviceStatus.setStatus("success");
				serviceStatus.setMessage("successfully fetched");
				Thread.sleep(5000);
				HomeManagementUtil.loadDeviceToken("serverB",url_path);
			}
			else {
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("Exist");
				//serviceStatus.setResult(deviceList);
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
	@CrossOrigin(origins = "*")
	@RequestMapping(value="/serverBResponse",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> serverBResponse(@RequestParam("info")String info){

		ServiceStatus<Object> serviceStatus=new ServiceStatus<Object>();
		try {

			System.out.println("serverBResponse"+info);

			if(info!=null){

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

	@GetMapping("/getHomeExpendature")
	ServiceStatus<List<HomeExpendature>> getHomeExpendature(@RequestParam("userId") String userId){

		ServiceStatus<List<HomeExpendature>> serviceStatus=new ServiceStatus<List<HomeExpendature>>();

		try {
			logger.info("getHomeExpendature method");

			List<HomeExpendature> itemList	= homeRepository.getItemByUserId(userId);
			if(itemList!=null&& itemList.size()>0){
				logger.info("isDeviceSeqNoExists"+itemList);
				serviceStatus.setStatus("success");
				serviceStatus.setMessage("successfully fetched");
				serviceStatus.setResult(itemList);
			}
			else {
				serviceStatus.setStatus("failure");
				serviceStatus.setMessage("Devices are not Exist");
				serviceStatus.setResult(itemList);
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

	@RequestMapping(value="/addDeviceNetwork",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> AddNetwork(@RequestBody DeviceNetwork deviceNetwork){

		ServiceStatus<Object> serviceStatus=new ServiceStatus<Object>();

		if(deviceNetwork!=null)
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

	@RequestMapping(value="/getdeviceNetworks",method=RequestMethod.GET,consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> getDeviceNetwork(){

		ServiceStatus<Object> serviceStatus=new ServiceStatus<Object>();
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

	@RequestMapping(value="/checkDeviceExists",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<?> getDeviceMaster(@RequestParam("device_id")String device_mac_id, @RequestParam("hardware_key")String device_key){


		ServiceStatus<Object> serviceStatus=new ServiceStatus<Object>();


		if(device_mac_id!=null){

			try {
				logger.info("Fetch device data"+device_mac_id);

				boolean isDeviceSeqNoExists = homeRepository.checkDeviceExists(device_mac_id,device_key);
				if(isDeviceSeqNoExists) {
					HomeExpendature diviceData = homeRepository.getItemeByItemId(device_mac_id);
					serviceStatus.setStatus("success");
					serviceStatus.setMessage("Device already has existed");
					serviceStatus.setResult(diviceData);
					if(diviceData != null) {
						List<HomeExpendature> deviceList	= homeRepository.getDeviceByParentId(null);
						logger.info("Fetch device data"+diviceData);
						serviceStatus.setDeviceList(deviceList);
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

	@RequestMapping(value="/deviceByDeviceId",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<?> getDeviceByDeviceId(@RequestParam("device_id")String device_mac_id){


		ServiceStatus<Object> serviceStatus=new ServiceStatus<Object>();


		if(device_mac_id!=null){

			try {
				logger.info("Fetch device data"+device_mac_id);

				HomeExpendature diviceData = homeRepository.getItemeByItemId(device_mac_id);
				serviceStatus.setStatus("success");
				serviceStatus.setMessage("Device already has existed");
				serviceStatus.setResult(diviceData);
				/*if(diviceData != null) {
						List<Device> deviceList	= deviceRepository.getDeviceByParentId(null);
						logger.info("Fetch device data"+diviceData);
						serviceStatus.setDeviceList(deviceList);
					}*/


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

	@RequestMapping(value="/deviceNetworkByNetworkId",method=RequestMethod.GET,consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> getDeviceNetworkByNetworkId(@RequestParam("network_id")String network_id){

		ServiceStatus<Object> serviceStatus=new ServiceStatus<Object>();
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
