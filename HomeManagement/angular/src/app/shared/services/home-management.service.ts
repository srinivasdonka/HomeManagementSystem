import { Injectable } from '@angular/core';
import { AdminRoutes } from 'src/app/admin/classes/admin-routes';
import { Observable } from 'rxjs/internal/Observable';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { WebService } from './web.service';
import { Router } from '@angular/router';
import { Response } from '@angular/http';
import { LoaderService } from 'src/app/loader/services/loader.service';
import { WindowRefService } from 'src/app/shared/services/window-ref.service';

@Injectable({
  providedIn: 'root'
})
export class HomeManagementService extends WebService<any> {

  constructor(
    private httpClient: HttpClient,
    private loaderService: LoaderService,
    private router: Router,
    protected windowRef: WindowRefService) {
    super(httpClient, loaderService);
  }

  getHomeExpendature(user_id): Observable<any> {
    const options = {
      url: AdminRoutes.ITEM_LIST(user_id)
    };
    return this.httpClient.get(options.url).pipe(res => res);
  }

  getCountries(): Observable<any> {
    const options = {
      url: AdminRoutes.GET_COUNTRIES()
    };
    return this.httpClient.get(options.url).pipe(res => res);
  }

  getItemByItemId(id) {
    const options = {
      url: AdminRoutes.ITEM_LIST(id)
    };
    return this.httpClient.get(options.url).pipe(res => res);
  }

  getUserInfo(userId, active, page, size) {
    const options = {
      url: AdminRoutes.USER_INFO(userId, active, page, size)
    };
    return this.httpClient.get(options.url).pipe(res => res);
  }

  getUserInfoById(userId) {
    const options = {
      url: AdminRoutes.USER_INFO_BY_ID(userId)
    };
    return this.httpClient.get(options.url).pipe(res => res);
  }

  getCompanyDetailsByUsername(userName) {
    const options = {
      url: AdminRoutes.COMPANY_DETAILS_BY_NAME(userName),
      isLoaderRequired: true
    };
    return this.httpClient.get(options.url).pipe(res => res);
  }

  getCompanyUsersById(companyId) {
    const options = {
      url: AdminRoutes.COMPANY_USERS_BY_ID(companyId),
      isLoaderRequired: true
    };
    return this.httpClient.get(options.url).pipe(res => res);
  }

  getCreateUsers(data) {
    const options = {
      url: AdminRoutes.CREATE_USERS(),
      params: data,
      isLoaderRequired: true
    };
    return this.httpClient.post(options.url, options.params).pipe(res => res);
  }

  getMultipleCreateUsers(data) {
    const options = {
      url: AdminRoutes.CREATE_MULTIPLE_USERS(),
      params: data,
      isLoaderRequired: true
    };
    return this.httpClient.post(options.url, options.params).pipe(res => res);
  }

  updateDevice(data) {
    const options = {
      url: AdminRoutes.UPDATE_DEVICE(),
      params: data,
      isLoaderRequired: true
    };
    return this.httpClient.put(options.url, options.params).pipe(res => res);
  }

  updateMutlipleUser(data) {
    const options = {
      url: AdminRoutes.UPDATE_MULTIPLE_USER(),
      params: data,
      isLoaderRequired: true
    };
    return this.httpClient.put(options.url, options.params).pipe(res => res);
  }
  

  getRoles() {
    const options = {
      url: AdminRoutes.GET_ROLES()
    };
    return this.httpClient.get(options.url).pipe(res => res);
  }

  getPrivileges() {
    const options = {
      url: AdminRoutes.GET_PRIVILEGES()
    };
    return this.httpClient.get(options.url).pipe(res => res);
  }

  getPrivilegesByRoleIdandUserId(roleId, userId) {
    const options = {
      url: AdminRoutes.GET_PRIVILIEGES_BY_ROLEID_USERID(roleId, userId)
    };
    return this.httpClient.get(options.url).pipe(res => res);
  }
  
  updatePrivileges(data) {
    const options = {
      url: AdminRoutes.UPDATE_PRIVILIEGES(),
      params: data,
      isLoaderRequired: true
    };
    return this.httpClient.put(options.url, options.params).pipe(res => res);
  }

  getPhoneHomeData(device_mac_id, hardware_key) {
    const options = {
      url: AdminRoutes.GET_PHONE_HOME_DATA(device_mac_id, hardware_key)
    };
    return this.httpClient.get(options.url).pipe(res => res);
  }

  checkDeviceExists(device_mac_id, hardware_key) {
    const options = {
      url: AdminRoutes.CHECK_DEVICE_EXISTS(device_mac_id, hardware_key)
    };
    return this.httpClient.get(options.url).pipe(res => res);
  }

  getDeviceNetworks() {
    const options = {
      url: AdminRoutes.GET_DEVICE_NETWORKS()
    };
    return this.httpClient.get(options.url).pipe(res => res);
  }

  addDeviceNetwork(data): Observable<any> {
    const options = {
      url: AdminRoutes.ADD_DEVICE_NETWORK(),
      params: data,
      isLoaderRequired: true
    };
    return this.httpClient.post(options.url, options.params).pipe(res => res);
  }

  addDeviceToCompany(data): Observable<any> {
    const options = {
      url: AdminRoutes.ADD_DEVICE_TO_COMPANY(),
      params: data,
      isLoaderRequired: true
    };
    return this.httpClient.post(options.url, options.params).pipe(res => res);
  }

  addExpendture(data): Observable<any> {
    const options = {
      url: AdminRoutes.ADD_EXPENDATURE(),
      params: data,
      isLoaderRequired: true
    };
    return this.httpClient.post(options.url, options.params).pipe(res => res);
  }
}

