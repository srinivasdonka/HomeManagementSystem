
import {throwError as observableThrowError,  Observable ,  throwError as _throw } from 'rxjs';
import { map, catchError, finalize } from 'rxjs/operators';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import * as _ from 'lodash';
import { LoaderService } from '../../loader/services/loader.service';

@Injectable()
export class WebService<T> {
  private observable: Observable<T>;

  constructor(
    private http: HttpClient,
    private loader: LoaderService
  ) { }

  get(options: any): Observable<T> {
    this.loader.start(options.isLoaderRequired);
    this.observable = this.http
      .get<any>(options.url, {
        params: options.params
      })
      .pipe(map(response => {
        this.observable = null;
        return response;
      }))
      .pipe(catchError(this.handleError))
      .pipe(finalize(() => {
        this.loader.stop(options.isLoaderRequired);
      }));

    return this.observable;
  }

  post(options: any): Observable<T> {
    this.loader.start(options.isLoaderRequired);
    this.observable = this.http.post<any>(options.url, options.params)
    .pipe(map(response => {
        return response;
      }))
      .pipe(catchError(this.handleError))
      .pipe(finalize(() => {
        this.loader.stop(options.isLoaderRequired);
      }));
    return this.observable;
  }

  put(options: any): Observable<T> {
    this.loader.start(options.isLoaderRequired);
    this.observable = this.http.put<any>(options.url, options.params)
      .pipe(map(response => {
        return response;
      }))
      .pipe(catchError(this.handleError))
      .pipe(finalize(() => {
        this.loader.stop(options.isLoaderRequired);
      }));
    return this.observable;
  }

  delete(options: any): Observable<T> {
    this.loader.start(options.isLoaderRequired);
    this.observable = this.http.delete<any>(options.url)
      .pipe(map(response => {
        return response;
      }))
      .pipe(catchError(this.handleError))
      .pipe(finalize(() => {
        this.loader.stop(options.isLoaderRequired);
      }));
    return this.observable;
  }

  handleError(error: any) {
    return observableThrowError(error);
  }
}
