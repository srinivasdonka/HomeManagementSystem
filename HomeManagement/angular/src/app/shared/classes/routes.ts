import { environment as env, environment } from '../../../environments/environment';
import { UserSettings } from './user-settings';
import { UserTypeEnum } from '../enums/userType';

const HOMEMANAGE_API_URL = env.apiUrl;

export class Routes {
  public static HOMEMANAGE_API_URL = env.apiUrl;

  public static OAUTHTOKEN() {
    return `${HOMEMANAGE_API_URL}oauth/token`;
  }

  public static LOGIN() {
    return `${HOMEMANAGE_API_URL}authenticate?deviceType=1&deviceId=&fcmToken=`;
  }

  public static LOGOUT(providerId, userId) {
    return `${HOMEMANAGE_API_URL}provider/${providerId}/user/${userId}/logout?deviceType=1&deviceId=&fcmToken=`;
  }

  public static REGISTER() {
    return `${HOMEMANAGE_API_URL}user/createUser`;
  }

  public static REFRESH_TOKEN(clientId, refreshToken) {
    return `${HOMEMANAGE_API_URL}freshtoken?clientId=${clientId}&refreshToken=${refreshToken}`;
  }

  public static CHECK_SESSION(accessToken) {
    return `${HOMEMANAGE_API_URL}verifyToken?token=${accessToken}`;
  }
  
  public static CLIENT_DETAILS(clientId) {
    return `${HOMEMANAGE_API_URL}user/getOAuthClientDetails?clientId=${clientId}`;
  }

  public static USER_PROFILE_BY_USERNAME(userName) {
    return `${HOMEMANAGE_API_URL}user/get?userName=${userName}`;
  }

  public static VERIFY_EMAIL(email) {
    return `${HOMEMANAGE_API_URL}user/checkEmail?email=${email}`;
  }

  public static GET_ROLES() {
    return `${HOMEMANAGE_API_URL}auth/getRoles?page=0&size=10`;
  }

  public static UPDATE_USER_LAST_LOGIN(userName, lastLogin) {
    return `${HOMEMANAGE_API_URL}user/lastLogin?userName=${userName}&lastLogin=${lastLogin}`;
  }

  public static UPDATE_USER_ACTIVATION(userId) {
    return `${HOMEMANAGE_API_URL}user/statusForRegistrationUser?id=${userId}`;
  }
}
