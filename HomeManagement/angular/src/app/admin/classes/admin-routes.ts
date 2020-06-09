import { environment as env } from '../../../environments/environment';
import { UserSettings } from '../../shared/classes/user-settings';
import { UserTypeEnum } from '../../shared/enums/userType';

const HOMEMANAGEMENT_API_URL = `${env.apiUrl}`;

export class AdminRoutes {
  
  // #Users Management
  public static USERS_LIST(page, size) {
    return `${HOMEMANAGEMENT_API_URL}user/getAll?page=${page}&size=${size}`;
  }
  public static USER_INFO(userId, active, page, size) {
    return `${HOMEMANAGEMENT_API_URL}user/getCompanyUsersByIdPageable?Id=${userId}&status=${active}&page=${page}&size=${size}`;
  }
  public static USER_INFO_BY_ID(userId) {
    return `${HOMEMANAGEMENT_API_URL}user/getUserBYID?id=${userId}`;
  }
  public static COMPANY_DETAILS_BY_NAME(userName) {
    return `${HOMEMANAGEMENT_API_URL}user/getCompanyByUserName?username=${userName}`;
  }
  public static COMPANY_USERS_BY_ID(companyId) {
    return `${HOMEMANAGEMENT_API_URL}user/getCompanyUsersById?Id=${companyId}`;
  }
  public static EMAIL_TOKEN(emailToken) {
    return `${HOMEMANAGEMENT_API_URL}user/getEmailToken?id=${emailToken}`;
  }  
  public static SENT_EMAIL() {
    return `${HOMEMANAGEMENT_API_URL}user/sentMail`;
  }
  public static CREATE_USERS() {
    return `${HOMEMANAGEMENT_API_URL}user/createUser`;
  }
  public static CREATE_MULTIPLE_USERS() {
    return `${HOMEMANAGEMENT_API_URL}user/createMultipleUsers`;
  }
  public static UPDATE_USER() {
    return `${HOMEMANAGEMENT_API_URL}user/updateUser`;
  }
  public static UPDATE_MULTIPLE_USER() {
    return `${HOMEMANAGEMENT_API_URL}user/updateMutlipleUser`;
  }
  public static GET_ROLES() {
    return `${HOMEMANAGEMENT_API_URL}auth/getRoles?page=0&size=10`;
  }
  public static GET_PRIVILEGES() {
    return `${HOMEMANAGEMENT_API_URL}auth/getPrivileges?page=0&size=10`;
  }
  public static GET_PRIVILIEGES_BY_ROLEID_USERID(roleId, userId) {
    return `${HOMEMANAGEMENT_API_URL}auth/getPrivilegesByRole?roleId=${roleId}&userId=${userId}`;
  }
  public static UPDATE_PRIVILIEGES() {
    return `${HOMEMANAGEMENT_API_URL}auth/updatePrivileges`;
  }
  // #end Users Management

  // Device Management
  public static ITEM_LIST(user_id) {
    return `${HOMEMANAGEMENT_API_URL}homeExp/getHomeExpendature?userId=${user_id}`;
  }

  public static GET_PHONE_HOME_DATA(device_mac_id, hardware_key) {
    return `${HOMEMANAGEMENT_API_URL}phoneHome/getPhoneHomeData?device_mac_id=${device_mac_id}&hardware_key=${hardware_key}`;
  }

  public static CHECK_DEVICE_EXISTS(device_mac_id, hardware_key) {
    return `${HOMEMANAGEMENT_API_URL}device/checkDeviceExists?device_id=${device_mac_id}&hardware_key=${hardware_key}`;
  }

  public static GET_DEVICE_NETWORKS() {
    return `${HOMEMANAGEMENT_API_URL}device/getdeviceNetworks`;
  }

  public static UPDATE_DEVICE() {
    return `${HOMEMANAGEMENT_API_URL}device/updateDevice`;
  }

  public static DEVICES_INFO(id) {
    return `${HOMEMANAGEMENT_API_URL}device/getDeviceInfo?id=${id}`;
  }

  public static GET_COUNTRIES() {
    return `assets/json/countries_list.json`;
  }

  public static ADD_DEVICE_NETWORK() {
    return `${HOMEMANAGEMENT_API_URL}device/addDeviceNetwork`;
  }

  public static ADD_DEVICE_TO_COMPANY() {
    return `${HOMEMANAGEMENT_API_URL}device/addDeviceToCompany`;
  }

  public static ADD_EXPENDATURE() {
    return `${HOMEMANAGEMENT_API_URL}homeExp/addExpendature`;
  }
  // #end Device Management
}
