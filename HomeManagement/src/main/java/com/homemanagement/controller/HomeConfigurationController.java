package com.homemanagement.controller;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.homemanagement.domain.DeviceConfiguration;
import com.homemanagement.dto.ServiceStatus;
import com.homemanagement.repositories.DeviceConfigurationRepository;
import com.homemanagement.service.StorageServiceImpl;
import com.homemanagement.utils.ConfigurationUploadRequest;
import com.homemanagement.utils.HomeManagementUtil;
import com.homemanagement.utils.UploadResponse;

@RestController
@RequestMapping("/deviceconfiguration")
public class HomeConfigurationController {
	@Autowired
	DeviceConfigurationRepository deviceConfigurationRepository;
	@Autowired 
	StorageServiceImpl storageService;

	@Autowired
	private Environment environment;

	static final Logger logger = Logger.getLogger(HomeConfigurationController.class);


	/**
	 * This method is use to add the device configuration.
	 * @return the service status class object with response status and payload
	 *         .
	 */
	@RequestMapping(value="/addDeviceConfiguration",method=RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> addDeviceConfiguration(@RequestBody DeviceConfiguration deviceconfiguration){

		ServiceStatus<Object> serviceStatus=new ServiceStatus<Object>();

		if(deviceconfiguration!=null && deviceconfiguration.getDevice_id()!=null && deviceconfiguration.getConfig_property_name()!=null && deviceconfiguration.getConfig_property_value()!=null)
		{
			try {
				logger.info("addDeviceConfiguration"+deviceconfiguration.getDevice_id());
				DeviceConfiguration isDeviceConfigExists= deviceConfigurationRepository.getDeviceConfigurationByDeviceIdAndConfigPropName(deviceconfiguration.getDevice_id(),deviceconfiguration.getConfig_property_name());
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

	/**
	 * This method is use to update the device configuration.
	 * @return the service status class object with response status and payload
	 *         .
	 */
	@RequestMapping(value="/updateDeviceConfiguration",method=RequestMethod.PUT,consumes=MediaType.APPLICATION_JSON_VALUE,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> updateDeviceConfiguration(@RequestBody DeviceConfiguration deviceconfiguration){

		ServiceStatus<Object> serviceStatus=new ServiceStatus<Object>();

		if(deviceconfiguration!=null && deviceconfiguration.getDevice_id()!=null && deviceconfiguration.getConfig_property_name()!=null && deviceconfiguration.getConfig_property_value()!=null)
		{
			try {
				logger.info("updateDeviceConfiguration"+deviceconfiguration.getDevice_id());
				DeviceConfiguration isDeviceConfigExists= deviceConfigurationRepository.getDeviceConfigurationByDeviceIdAndConfigPropName(deviceconfiguration.getDevice_id(),deviceconfiguration.getConfig_property_name());
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




	@RequestMapping(value="/getDeviceConfigurationListByDeviceId",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<List<DeviceConfiguration>> getDeviceConfigurationListByDeviceId(@RequestParam("device_id")String device_id){

		ServiceStatus<List<DeviceConfiguration>> serviceStatus=new ServiceStatus<List<DeviceConfiguration>>();


		if(device_id!=null){

			try {
				logger.info("getDeviceConfigurationListByDeviceId"+device_id);

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
	/**
	 * This method is used to add the network .
	 * @return the service status class object with response status and payload
	 *         .
	 */
	@RequestMapping(value = "/setupMeshdeviceNetwork", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> setupMeshdeviceNetwork(@RequestBody List<DeviceConfiguration> deviceconfiguration) {

		ServiceStatus<Object> serviceStatus = new ServiceStatus<Object>();
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

	/**
	 * This method is use to update the device configuration.
	 * @return the service status class object with response status and payload
	 *         .
	 */
	@RequestMapping(value = "/updateMeshdeviceNetworkSetup", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> updateMeshdeviceNetworkSetup(@RequestBody List<DeviceConfiguration> deviceconfiguration) {

		ServiceStatus<Object> serviceStatus = new ServiceStatus<Object>();
		boolean deviceconfigExist = false;

		if (deviceconfiguration != null) {
			try {
				for (DeviceConfiguration deviceConfig : deviceconfiguration) {
					logger.info("updateDeviceConfiguration" + deviceConfig.getDevice_id());
					DeviceConfiguration isDeviceConfigExists = deviceConfigurationRepository
							.getDeviceConfigurationByDeviceIdAndConfigPropName(deviceConfig.getDevice_id(),
									deviceConfig.getConfig_property_name());

					if (isDeviceConfigExists != null) {
						deviceconfigExist = true;

					} else {
						serviceStatus.setStatus("failure");
						serviceStatus.setMessage("Device Configuration  Not Exist");
						return serviceStatus;

					}
				}
				if (deviceconfigExist) {

					List<DeviceConfiguration> deviceConfigList = new ArrayList<DeviceConfiguration>();
					for (DeviceConfiguration deviceConfig : deviceconfiguration) {

						LocalDateTime localDateTime = HomeManagementUtil.convertTolocalDateTimeFrom(new Date());

						deviceConfig.setLast_updated(localDateTime);
						deviceConfigList.add(deviceConfig);
					}

					deviceConfigurationRepository.updateDeviceConfigurationList(deviceConfigList);

					serviceStatus.setStatus("success");
					serviceStatus.setMessage("Successfully updated Device Configuration ");

				} else {
					serviceStatus.setStatus("failure");
					serviceStatus.setMessage("Device Configuration Not Exist");

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

	@RequestMapping(value = "/getDeviceConfigurationListByDeviceIdConfigtype", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<List<DeviceConfiguration>> getDeviceConfigurationListByDeviceIdConfigtype(@RequestParam("device_id") String device_id,@RequestParam("config_property_type") String config_property_type) {

		ServiceStatus<List<DeviceConfiguration>> serviceStatus = new ServiceStatus<List<DeviceConfiguration>>();

		if (device_id != null && config_property_type != null ) {

			try {
				logger.info("getDeviceConfigurationListByDeviceId" + device_id);

				List<DeviceConfiguration> deviceConfigList = deviceConfigurationRepository.getDeviceConfigurationByDeviceIdAndConfigPropType(device_id, config_property_type);
				if (deviceConfigList != null && deviceConfigList.size() > 0) {
					serviceStatus.setStatus("success");
					serviceStatus.setMessage("successfully fetched");
					serviceStatus.setResult(deviceConfigList);
				} else {
					serviceStatus.setStatus("failure");
					serviceStatus.setMessage("Device configuration not Exist");
					serviceStatus.setResult(deviceConfigList);
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
			serviceStatus.setMessage("Invalid param values ");
		}

		return serviceStatus;
	}
	/**
	 * This method is used to create the access point .
	 * @return the service status class object with response status and payload
	 *         .
	 */
	@RequestMapping(value = "/setupAccessPoint", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> setupAccessPoint(@RequestBody List<DeviceConfiguration> deviceconfiguration) {

		ServiceStatus<Object> serviceStatus = new ServiceStatus<Object>();
		boolean deviceConfigExist = false;
		String config_prop_type="AP";
		Integer is_Sync=0;

		if (deviceconfiguration != null) {

			try {
				for (DeviceConfiguration deviceConfig : deviceconfiguration) {

					logger.info("Create Access Point" + deviceConfig.getDevice_id());
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
					serviceStatus.setMessage("Successfully created Access Point ");

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

	/**
	 * This method is used to update the Access Point configuration.
	 * @return the service status class object with response status and payload
	 *         .
	 */
	@RequestMapping(value = "/updateAccessPointSetup", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> updateAccessPointSetup(@RequestBody List<DeviceConfiguration> deviceconfiguration) {

		ServiceStatus<Object> serviceStatus = new ServiceStatus<Object>();
		boolean deviceconfigExist = false;

		if (deviceconfiguration != null) {
			try {
				for (DeviceConfiguration deviceConfig : deviceconfiguration) {
					logger.info("updateAccessPointSetup" + deviceConfig.getDevice_id());
					DeviceConfiguration isDeviceConfigExists = deviceConfigurationRepository
							.getDeviceConfigurationByDeviceIdAndConfigPropName(deviceConfig.getDevice_id(),
									deviceConfig.getConfig_property_name());

					if (isDeviceConfigExists != null) {
						deviceconfigExist = true;

					} else {
						serviceStatus.setStatus("failure");
						serviceStatus.setMessage("Device Configuration  Not Exist");
						return serviceStatus;
					}
				}
				if (deviceconfigExist) {

					List<DeviceConfiguration> deviceConfigList = new ArrayList<DeviceConfiguration>();
					for (DeviceConfiguration deviceConfig : deviceconfiguration) {

						LocalDateTime localDateTime = HomeManagementUtil.convertTolocalDateTimeFrom(new Date());

						deviceConfig.setLast_updated(localDateTime);
						deviceConfigList.add(deviceConfig);
					}

					deviceConfigurationRepository.updateDeviceConfigurationList(deviceConfigList);

					serviceStatus.setStatus("success");
					serviceStatus.setMessage("Successfully updated Access Point Configuration ");

				} else {
					serviceStatus.setStatus("failure");
					serviceStatus.setMessage("Device Configuration Not Exist");

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


	/**
	 * This method is used to add the network .
	 * @return the service status class object with response status and payload
	 *         .
	 */
	@RequestMapping(value = "/createSSIDConfig", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> createSSIDConfig(@RequestBody List<DeviceConfiguration> deviceconfiguration) {

		ServiceStatus<Object> serviceStatus = new ServiceStatus<Object>();
		boolean deviceConfigExist = false;
		Integer is_Sync=0;

		if (deviceconfiguration != null) {

			try {
				for (DeviceConfiguration deviceConfig : deviceconfiguration) {

					logger.info("createSSIDConfig" + deviceConfig.getDevice_id());
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
						deviceConfig.setConfig_property_type("SSID");
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



	@RequestMapping(value = "/getSSIDConfiguration", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<List<DeviceConfiguration>> getSSIDConfiguration(@RequestParam("device_id") String device_id,@RequestParam("config_property_type") String config_property_type) {

		ServiceStatus<List<DeviceConfiguration>> serviceStatus = new ServiceStatus<List<DeviceConfiguration>>();

		if (device_id != null && config_property_type != null ) {

			try {
				logger.info("getDeviceConfigurationListByDeviceId" + device_id);

				List<DeviceConfiguration> deviceConfigList = deviceConfigurationRepository.getDeviceConfigurationByDeviceIdAndConfigPropType(device_id, config_property_type);
				if (deviceConfigList != null && deviceConfigList.size() > 0) {
					logger.info("deviceConfigList" + deviceConfigList);
					serviceStatus.setStatus("success");
					serviceStatus.setMessage("successfully fetched");
					serviceStatus.setResult(deviceConfigList);
				} else {
					serviceStatus.setStatus("failure");
					serviceStatus.setMessage("Device configuration not Exist");
					serviceStatus.setResult(deviceConfigList);
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
			serviceStatus.setMessage("Invalid param values ");
		}

		return serviceStatus;
	}
	/**
	 * This method is use to update the device SSID configuration.
	 * @return the service status class object with response status and payload
	 *         .
	 */
	@RequestMapping(value = "/updateSSIDDetails", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<Object> updateSSIDDetails(@RequestBody List<DeviceConfiguration> deviceconfiguration) {

		ServiceStatus<Object> serviceStatus = new ServiceStatus<Object>();
		boolean deviceconfigExist = false;

		if (deviceconfiguration != null) {
			try {
				for (DeviceConfiguration deviceConfig : deviceconfiguration) {
					DeviceConfiguration isDeviceConfigExists = deviceConfigurationRepository
							.getDeviceConfigurationByDeviceIdAndConfigPropName(deviceConfig.getDevice_id(),
									deviceConfig.getConfig_property_name());

					if (isDeviceConfigExists != null) {
						deviceconfigExist = true;

					} else {
						serviceStatus.setStatus("failure");
						serviceStatus.setMessage("Device Configuration  Not Exist");
						return serviceStatus;

					}
				}
				if (deviceconfigExist) {

					List<DeviceConfiguration> deviceConfigList = new ArrayList<DeviceConfiguration>();
					for (DeviceConfiguration deviceConfig : deviceconfiguration) {

						LocalDateTime localDateTime = HomeManagementUtil.convertTolocalDateTimeFrom(new Date());

						deviceConfig.setLast_updated(localDateTime);
						deviceConfigList.add(deviceConfig);
					}

					deviceConfigurationRepository.updateDeviceConfigurationList(deviceConfigList);

					serviceStatus.setStatus("success");
					serviceStatus.setMessage("Successfully updated Device Configuration ");

				} else {
					serviceStatus.setStatus("failure");
					serviceStatus.setMessage("Device Configuration Not Exist");

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

	@RequestMapping(value = "/deviceConfigUpload",produces = { "application/json"},method = RequestMethod.POST) 
	public ResponseEntity<UploadResponse> deviceConfigUpload(
			@RequestParam("qqfile") String file,
			@RequestParam("qquuid") String uuid,
			@RequestParam("qqfilename") String fileName,
			@RequestParam(value = "version") String version,
			@RequestParam(value = "device_id") String device_id,
			@RequestParam(value = "checkSum") String checkSum) {

		byte[] file_path=file.getBytes();
		ConfigurationUploadRequest request = new ConfigurationUploadRequest(uuid, file_path);
		request.setFileName(fileName);

		storageService.saveConfiguration(request);


		return ResponseEntity.ok().body(new UploadResponse(true));

	}
	@RequestMapping(value = "/getConfiguration", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE}, method = RequestMethod.GET)
	public void getFile(@RequestParam(value="fileName", required=true) String fileName, 
			HttpServletRequest request,HttpServletResponse response) throws FileNotFoundException {

		String path=environment.getProperty("config.file.path");
		byte[] reportBytes = null;
		File result=new File(path+"/"+fileName);

		Enumeration<?> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String headerName = (String)headerNames.nextElement();
		}

		if(result.exists()){
			try(InputStream inputStream = new FileInputStream(path+"/"+fileName)) {
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
	@RequestMapping(value="/getDeviceConfifg",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_VALUE)
	ServiceStatus<List<DeviceConfiguration>> getDeviceConfifg(@RequestParam("device_id")String device_id){

		ServiceStatus<List<DeviceConfiguration>> serviceStatus=new ServiceStatus<List<DeviceConfiguration>>();


		if(device_id!=null){

			try {
				logger.info("getDeviceConfigurationListByDeviceId"+device_id);

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
