export interface DeviceHardwareIdKeyObject {
  deviceHardwareId: string,
  deviceKey: string
}

export class HomeSettings {
  
  private static _cookieEnabled: string;
  private static _deviceHardwareIdKey = [];
  
  public static get getCookieEnabled(): string {
    const cookieEnabled = localStorage.getItem('cookieEnabled');
    if (cookieEnabled) {
      this._cookieEnabled = cookieEnabled;
    }
    return this._cookieEnabled;
  }

  public static set setCookieEnabled(cookieEnabled: string) {
    localStorage.setItem('cookieEnabled', cookieEnabled);
    this._cookieEnabled = cookieEnabled;
  }

  public static get getDeviceHardwareIdandKey(): any {
    const deviceHardwareIdKey = JSON.parse( localStorage.getItem('deviceHardwareIdKey') );
    if (deviceHardwareIdKey) {
      this._deviceHardwareIdKey = deviceHardwareIdKey;
    }
    return this._deviceHardwareIdKey;
  }

  public static set setDeviceHardwareIdandKey(deviceHardwareIdKey: DeviceHardwareIdKeyObject) {
    let dummyObj = [];
    if (localStorage.getItem('deviceHardwareIdKey') === null) {
      dummyObj = [];
    } else {
      // Parse the serialized data back into an array of objects
      dummyObj = JSON.parse(localStorage.getItem('deviceHardwareIdKey'));
    }
    // Push the new data (whether it be an object or anything else) onto the array
    dummyObj.push(deviceHardwareIdKey);
    localStorage.setItem('deviceHardwareIdKey', JSON.stringify(dummyObj));
    this._deviceHardwareIdKey.push(JSON.stringify(dummyObj));
  }

  public static removeAllDeviceSettings() {
    localStorage.clear();
    this._cookieEnabled = null;
    this._deviceHardwareIdKey = null;
  }  
}
