import { Injectable } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';
import { Router } from '@angular/router';

import { map, catchError} from 'rxjs/operators';
import { Observable, throwError, of} from 'rxjs';

import { UserSettings } from '../classes/user-settings';
import { environment } from '../../../environments/environment';
import { AuthenticateService } from './authenticate.service';
import { HttpUserEvent } from '@angular/common/http';
import { HttpSentEvent } from '@angular/common/http';
import { HttpHeaderResponse } from '@angular/common/http';
import { HttpProgressEvent } from '@angular/common/http';
import { HttpResponse } from '@angular/common/http';
import { HttpErrorResponse } from '@angular/common/http';
import { HttpHeaders } from '@angular/common/http';
import { ModalService } from 'src/app/shared/services/modal.service';
import { Routes } from '../classes/routes';

@Injectable({
  providedIn: 'root'
})
export class HttpInterceptorService implements HttpInterceptor {
  access_token = null;
  headerEnccode: any;
  private clientId = environment.clientId;
  private refreshGrantType = 'refresh_token';

  constructor(
    private authenticateService: AuthenticateService,
    private router: Router,
    private modalService: ModalService
  ) {
    this.headerEnccode = window.btoa(environment.clientId + ':' + environment.client_secret);
  }

  isAuthTokenRequest(url: string) {
    return (/token|authenticate|logout|freshtoken|registerEndUser|accept-invitation|assets/i).test(url);
  }

  isBareerTokenRequest(url: string) {
    return (/createUser|updateUser|get|sentMail/i).test(url);
  }

  isHomeManageService(url) {
    const apiURl = new RegExp(environment.apiUrl, 'gi');
    return (apiURl).test(url);
  }

  isTokenRequired(url) {
    return this.isHomeManageService(url) && !this.isAuthTokenRequest(url);
  }

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    this.access_token = localStorage.getItem('access_token');
    let authReq;
    
    if (this.isBareerTokenRequest(req.url)) {
      authReq = req.clone({
        setHeaders: {
          'Content-Type': 'application/json',
          'Accept': 'application/json'
        }
      });
    } else {

      if (this.isTokenRequired(req.url)) {
        authReq = req.clone({
          setHeaders: {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'Authorization': `Bearer ` + this.access_token
          }
        });
      } 
      else {
        authReq = req.clone({
          setHeaders: {
            'Content-Type': 'application/x-www-form-urlencoded',
            'Authorization': `Basic SG9tZU1hbmFnZW1lbnQ6SG9tZU1hbmFnZW1lbnQ=`
          }
        });
      }
    }

    return next.handle(authReq).pipe(catchError( (err, caught) => {      
      const error = err.error.message || err.statusText;
      this.authenticateService.logout();
      return of(error);
    }) as any);
  }
}
