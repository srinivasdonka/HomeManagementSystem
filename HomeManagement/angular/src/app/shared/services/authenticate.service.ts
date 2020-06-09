import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { map } from 'rxjs/operators';
import { WebService } from './web.service';
import { Routes } from '../classes/routes';
import { Observable } from 'rxjs';
import { UserSettings } from '../classes/user-settings';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { environment } from '../../../environments/environment';
import { UserTypeEnum } from '../enums/userType';
import { LoaderService } from 'src/app/loader/services/loader.service';
import { WindowRefService } from 'src/app/shared/services/window-ref.service';

@Injectable({
  providedIn: 'root'
})
export class AuthenticateService extends WebService<any> {

  loginInfo: any;

  constructor(
    private httpClient: HttpClient,
    private loaderService: LoaderService,
    private router: Router,
    protected windowRef: WindowRefService) {
    super(httpClient, loaderService);
  }

  register(model) {
    const options = {
      url: Routes.REGISTER(),
      params: model
    };
    return this.httpClient.post(options.url, options.params).pipe(res => res);
  }

  login(loginData, user): Observable<any> {
    const options = {
      url: Routes.OAUTHTOKEN(),
      params: loginData
    };
    this.loginInfo = user;
    return this.httpClient.post(options.url, options.params)
      .pipe(map(data => {
        if (data && data['access_token']) {
          this.storeAuthInfo(data);
          return data;
        }
      }));
  }

  storeAuthInfo(AuthData) {
    if (AuthData && AuthData.access_token) {
      UserSettings.setToken(AuthData.access_token, AuthData.refresh_token);
      UserSettings.setUserType = <UserTypeEnum>2;
      UserSettings.setUserName = this.loginInfo;
    }
  }

  logout() {
    const token = UserSettings.accessToken;
    this.loaderService.contentLoadingCount.next(0);
    UserSettings.removeAllUserDetails();
    this.router.navigate(['login']);
  }

  isAuthenticated(): boolean {
    // get the token
    const token = UserSettings.accessToken;
    return (token) ? true : false;
  }
}
