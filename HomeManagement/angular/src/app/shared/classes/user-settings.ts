import { UserTypeEnum } from '../enums/userType';

export class UserSettings {
  
  private static _token: string;
  private static _refreshedToken: string;
  private static _userName: string;
  private static _userId: number;
  private static _providerId: number;
  private static _userType: number;
  private static _clientId: string;
  private static _clientSecret: string;
  private static _timeZome: string;
  private static _permissions: any;
  private static _userProfile: any;
  private static _cookieEnabled: any;
  private static _deviceHardwareIdKey: any;
  private static itemInfo: any;
  
  public static get accessToken(): string {
    const storedToken = localStorage.getItem('access_token');
    if (storedToken) {
      this._token = storedToken;
    }
    return this._token;
  }

  public static set accessToken(newToken: string) {
    localStorage.setItem('access_token', newToken);
    this._token = newToken;
  }

  public static get refreshToken(): string {
    const storedToken = localStorage.getItem('refresh_token');
    if (storedToken) {
      this._refreshedToken = storedToken;
    }
    return this._refreshedToken;
  }

  public static set refreshToken(newToken: string) {
    localStorage.setItem('refresh_token', newToken);
    this._refreshedToken = newToken;
  }

  public static setToken(accessToken: string, refreshToken: string) {
    UserSettings.accessToken = accessToken;
    // UserSettings.refreshToken = refreshToken;
  }

  public static get clientId(): string {
    const clientId = localStorage.getItem('clientId');
    if (clientId) {
      this._clientId = clientId;
    }
    return this._clientId;
  }

  public static set clientId(clientId: string) {
    localStorage.setItem('clientId', clientId);
    this._clientId = clientId;
  }

  public static get clientSecret(): string {
    const clientSecret = localStorage.getItem('clientSecret');
    if (clientSecret) {
      this._clientSecret = clientSecret;
    }
    return this._clientSecret;
  }

  public static set clientSecret(clientSecret: string) {
    localStorage.setItem('clientSecret', clientSecret);
    this._clientSecret = clientSecret;
  }

  public static setClientDetails(clientId: string, clientSecret: string) {
    UserSettings.clientId = clientId;
    UserSettings.clientSecret = clientSecret;
  }

  public static removeAllUserDetails() {
    const _cookieEnabled = localStorage.getItem('cookieEnabled');
    const _deviceHardwareIdKey = localStorage.getItem('deviceHardwareIdKey');    
    localStorage.clear();
    this._token = null;
    this._refreshedToken = null;
    this._userId = null;
    this._userName = null;
    this._userType = null;
    this._providerId = null;
    this._permissions = null;
    localStorage.setItem('cookieEnabled', _cookieEnabled);
    localStorage.setItem('deviceHardwareIdKey', _deviceHardwareIdKey);
  }

  public static set setUserPermissions(roles: any) {
    localStorage.setItem('permissions', roles);
  }

  public static get getUserPermissions() {
    const permissions = localStorage.getItem('permissions');
    if (permissions) {
      this._permissions = permissions;
    }
    return JSON.parse(this._permissions);
  }

  public static set setUserType(UserType: UserTypeEnum) {
    localStorage.setItem('userType', UserType.toString());
  }

  public static get getUserType(): UserTypeEnum {
    const userType = Number(localStorage.getItem('userType'));
    if (userType) {
      this._userType = userType;
    }
    return <UserTypeEnum>this._userType;
  }

  public static set setUserLogout(logout: any) {
    localStorage.setItem('logout', logout);
  }

  public static set setUserName(name: any) {
    localStorage.setItem('userName', name);
  }

  public static get getUsername() {
    const userName = localStorage.getItem('userName');
    if (userName) {
      this._userName = userName;
    }
    return this._userName;
  }

  public static set setUserProfile(profile: any) {
    this._userProfile = profile;
  }

  public static get getUserProfile() {
    return this._userProfile;
  }

  public static set setItemInfo(itemInfo: any) {
    this.itemInfo = itemInfo;
  }

  public static get getItemInfo() {
    return this.itemInfo;
  }

}
