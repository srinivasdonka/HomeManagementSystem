package com.homemanagement.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.homemanagement.constant.HomeManagementConstant;
import com.homemanagement.dto.EmailVo;


public class HomeManagementUtil {

	private final static Gson gson = new GsonBuilder().create();
	private static final Logger LOGGER = LoggerFactory.getLogger(HomeManagementUtil.class);
	private static TokenStore tokenStore;
	public static final String KEY = "3b3c714572df4c6b91473d6f425a2d87";
	public static final String INITVECTOR = "714572df4c6b9147";
	/**
	 * This method is used to validate the empty or null string
	 * 
	 * @param str
	 *            specify the string that will be validated
	 * @return {@link Boolean} class's object with <code>true</code> or
	 *         <code>false</code> value
	 **/
	public static Boolean isEmptyString(String str) {
		Boolean flag = true;
		if (str != null) {
			String trimedStr = str.trim();

			if (trimedStr.length() > 0) {
				flag = false;
			}
		}
		return flag;
	}

	public static String get_SHA_512_SecurePassword(String passwordToHash, String salt)
	{
		String generatedPassword = null;
		try {

			MessageDigest md = MessageDigest.getInstance("SHA-512");
			if(salt != null)
				md.update(salt.getBytes("UTF-8"));
			byte[] bytes = md.digest(passwordToHash.getBytes("UTF-8"));
			StringBuilder sb = new StringBuilder();
			for(int i=0; i< bytes.length ;i++)
			{
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			generatedPassword = sb.toString();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return generatedPassword;
	}

	/**
	 * This method is used to convert an object to a JsonElement
	 * 
	 * @param object	The object is require to convert JsonElement
	 * @param type		The type of object	
	 * 
	 * @return returns converted JsonElement for a given object
	 */
	public static JsonElement convertToJsonElement(Object object, Type type) {
		JsonElement element = gson.toJsonTree(object, type);
		return element;
	}

	public static <T> T convertJsonStringToJavaObject(String jsonString, Type objectType) {
		return gson.fromJson(jsonString, objectType);
	}

	public static void removeUserOAuthTokens(Long clientId, String userName) {
		String webClientId = clientId + "@web";
		String mobileClientId = clientId + "@mobile";

		Collection<OAuth2AccessToken> webTokens = 
				tokenStore.findTokensByClientIdAndUserName(webClientId, userName);
		for(OAuth2AccessToken webToken :webTokens){
			OAuth2RefreshToken refreshToken = webToken.getRefreshToken();
			tokenStore.removeAccessToken(webToken);
			tokenStore.removeRefreshToken(refreshToken);
		}

		Collection<OAuth2AccessToken> mobileTokens = 
				tokenStore.findTokensByClientIdAndUserName(mobileClientId, userName);

		for(OAuth2AccessToken mobileToken :mobileTokens){
			OAuth2RefreshToken refreshToken = mobileToken.getRefreshToken();
			tokenStore.removeAccessToken(mobileToken);
			tokenStore.removeRefreshToken(refreshToken);
		}
	}

	public static OAuth2Authentication getOAuth2AuthenticationByUser(Long clientId, String userName) {
		String webClientId = clientId + "@web";
		OAuth2Authentication oAuth2Authentication = null;

		OAuth2AccessToken oaAuth2AccessToken = null;

		Collection<OAuth2AccessToken> webTokens = 
				tokenStore.findTokensByClientIdAndUserName(webClientId, userName);

		if(webTokens != null && webTokens.size() > 0) {
			for(OAuth2AccessToken webToken :webTokens){
				oaAuth2AccessToken = webToken;
			}
		} 

		if(oaAuth2AccessToken != null) {
			OAuth2RefreshToken oaAuth2RefreshToken = oaAuth2AccessToken.getRefreshToken();
			oAuth2Authentication = tokenStore.readAuthenticationForRefreshToken(oaAuth2RefreshToken);
		}

		return oAuth2Authentication;
	}

	public static void setTokenStore(TokenStore tokenStore) {
		HomeManagementUtil.tokenStore = tokenStore;
	}

	public static Date convertToDateFrom(final LocalDateTime localDateTime) {
		return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
	}

	public static LocalDateTime convertTolocalDateTimeFrom(final Date date) {
		return Instant.ofEpochMilli(date.getTime())
				.atZone(ZoneId.systemDefault())
				.toLocalDateTime();
	}

	public static String encrypt(String key, String initVector, String value) {
		CryptLib cryptLib;    
		try {
			cryptLib = new CryptLib();
			String encryptedString = cryptLib.encrypt(value, key, initVector);
			return encryptedString;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static String decrypt(String key, String initVector, String encrypted) {
		CryptLib cryptLib;
		try {
			cryptLib = new CryptLib();
			String decrytedString = cryptLib.decrypt(encrypted, key, initVector);
			return decrytedString;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}


	public static String generateDeviceToken(String companyId,String deviceId)
	{
		String deviceTokenEncrypt=null;
		if((companyId!=null)&&(deviceId!=null))
		{
			String devicetokenvalue=companyId.trim()+deviceId.trim();
			deviceTokenEncrypt = encrypt(KEY, INITVECTOR,devicetokenvalue);
		}
		return deviceTokenEncrypt;

	}
	public static void loadDeviceToken(String info) {
		String uri = " http://localhost:10020/device/serverBResponse?info=" + info;
		try {
			HttpURLConnection httpConnection = getHttpUrlConnection(uri);
			httpConnection.setRequestMethod("GET");
			BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			if(httpConnection.getResponseCode() == 200) {
				LOGGER.info("Success code" +httpConnection.getResponseCode());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static HttpURLConnection getHttpUrlConnection(String url) {
		HttpURLConnection httpConnection = null;
		try {
			URL obj = new URL(url);
			httpConnection = (HttpURLConnection) obj.openConnection();
			httpConnection.setRequestProperty("User-Agent", "Mozilla/5.0");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return httpConnection;
	}
	public static void main(String[] args) {
		loadDeviceToken("");
	}
	public static final Map<String, String> RECAPTCHA_ERROR_CODE = new HashMap<>();

	static {
		RECAPTCHA_ERROR_CODE.put("missing-input-secret", 
				"The secret parameter is missing");
		RECAPTCHA_ERROR_CODE.put("invalid-input-secret", 
				"The secret parameter is invalid or malformed");
		RECAPTCHA_ERROR_CODE.put("missing-input-response", 
				"The response parameter is missing");
		RECAPTCHA_ERROR_CODE.put("invalid-input-response", 
				"The response parameter is invalid or malformed");
		RECAPTCHA_ERROR_CODE.put("bad-request", 
				"The request is invalid or malformed");
	}

	public static List<String[]>  parseFile(String delimiter,String filePaht) {

		List<String[]> result = new ArrayList<>();
		try {
			File f = new File(filePaht);
			@SuppressWarnings("resource")
			Scanner sc = new Scanner(f);
			while(sc.hasNextLine()){
				String line = sc.nextLine();
				String[] details = line.split(delimiter);
				result.add(details);
			}
			return result;
		} catch (FileNotFoundException e) {         
			e.printStackTrace();
		}
		return result;
	}


	public static String decrypt(String fileName, byte[] keyBytes) {
		String content="";

		try (FileInputStream fileIn = new FileInputStream(fileName)) {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
			byte[] fileIv = new byte[16];
			fileIn.read(fileIv);
			cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(fileIv));
			try (
					CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
					InputStreamReader inputReader = new InputStreamReader(cipherIn);
					BufferedReader reader = new BufferedReader(inputReader)
					) {

				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					sb.append(line);
				}
				content = sb.toString();
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}
	public static void encrypt(String content, String fileName, byte[] keyBytes) {
		try(FileOutputStream fileOut = new FileOutputStream(fileName)) {
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			byte[] iv = cipher.getIV();
			@SuppressWarnings("resource")
			CipherOutputStream cipherOut = new CipherOutputStream(fileOut, cipher);
			fileOut.write(iv);
			cipherOut.write(content.getBytes());

		}catch (Exception e) {
			LOGGER.error(e.getMessage());
		}
	}
	public static String generateURI(String companyId,String deviceId,String filename){
		String uid=null;
		final String uuid = UUID.randomUUID().toString().replace("-", "");
		if((companyId!=null)&&(deviceId!=null)&&filename!=null){
			uid= uuid+"filename&="+filename+"&deviceID="+deviceId;
		}
		return uid;

	}

	public static  String parseHtmlTemplate(EmailVo emailVo) {
		String htmlTemp = null;
		String	firstHtmlTemp=null;
		String secHtmlTemp=null;
		try {
			if(ObjectUtils.anyNotNull(emailVo)) {
				for(HomeManagementConstant templateItems : emailVo.getTemplateReplaceItems()) {
					switch(templateItems) {
					case TO_USER_NAME:
						htmlTemp =  emailVo.getTemplate().replace(templateItems.getKey(), emailVo.getToUser());
						break;
					case FROM_USER_NAME:
						firstHtmlTemp =  htmlTemp.replace(templateItems.getKey(), emailVo.getFromUser());
						break;
					case ACCEPT_INVITATION_LINK:
						case RESET_MY_PASSWORD:
							secHtmlTemp =  firstHtmlTemp.replace(templateItems.getKey(), emailVo.getEmail_url());
						break;
					case CONFIRM_REGISTRATION_LINK:
						secHtmlTemp =  htmlTemp.replace(templateItems.getKey(),emailVo.getEmail_url());
						break;
						default:
					}
				}
			}
		}catch(Exception eMsg) {
			eMsg.getStackTrace();
		}
		return secHtmlTemp;
	}

	public static String fileName(String filePath) {
		File folder = new File(filePath);
		File[] listOfFiles = folder.listFiles();
		String name = null;
		for (int i = 0; i < listOfFiles.length; i++) {
			if (listOfFiles[i].isFile()) {
				name = listOfFiles[i].getName();
			} 
		}
		return name;
	}
}


