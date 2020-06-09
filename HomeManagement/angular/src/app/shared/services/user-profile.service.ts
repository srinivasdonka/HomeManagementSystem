import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/internal/Observable';

import { Router } from '@angular/router';
import { Response } from '@angular/http';

import { LoaderService } from 'src/app/loader/services/loader.service';
import { WindowRefService } from 'src/app/shared/services/window-ref.service';
import { WebService } from 'src/app/shared/services/web.service';
import { Routes } from 'src/app/shared/classes/routes';

@Injectable({
  providedIn: 'root'
})
export class UserProfileService extends WebService<any> {

  constructor(
    private httpClient: HttpClient,
    private loaderService: LoaderService,
    private router: Router,
    protected windowRef: WindowRefService) {
    super(httpClient, loaderService);
  }

  getUserProfile(userName): Observable<any> {
    const options = {
      url: Routes.USER_PROFILE_BY_USERNAME(userName),
      isLoaderRequired: false
    };
    return this.httpClient.get(options.url).pipe(res => res);
  }

  getAllRoles(): Observable<any> {
    const options = {
      url: Routes.GET_ROLES()
    };
    return this.httpClient.get(options.url).pipe(res => res);
  }

  updateUserLastLogin(userName, lastLogin): Observable<any> {
    const options = {
      url: Routes.UPDATE_USER_LAST_LOGIN(userName, lastLogin),
      params: {}
    };
    return this.httpClient.put(options.url, options.params).pipe(res => res);
  }

  updateUserActivation(userId): Observable<any> {
    const options = {
      url: Routes.UPDATE_USER_ACTIVATION(userId),
      params: {}
    };
    return this.httpClient.put(options.url, options.params).pipe(res => res);
  }
  
}
