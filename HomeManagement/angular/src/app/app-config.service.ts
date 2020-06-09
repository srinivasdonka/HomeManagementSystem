import { throwError as observableThrowError,  Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import { empty } from 'rxjs';

export function initLabels(config: AppConfigService): Function {
  return () => config.loadHomeManageLabels();
}
export function initMessages(config: AppConfigService): Function {
  return () => config.loadHomeManageMessages();
}

@Injectable()
export class AppConfigService {
  private homeManageLabels: any;
  private homeManageMessages: any;

  constructor(private http: HttpClient) { }

  loadHomeManageLabels(): Promise<any> {

    const labelsObservable = this.http.get('assets/json/labels.json');
    labelsObservable
      .pipe(catchError(error => {
        return empty();
      }))
      .subscribe(config => {
        this.homeManageLabels = config;
      });
    return labelsObservable.toPromise();
  }

  loadHomeManageMessages(): Promise<any> {
    const messagesObservable = this.http.get('assets/json/validations.json');
    messagesObservable
    .pipe(catchError(error => {
        return empty();
      }))
      .subscribe(config => {
        this.homeManageMessages = config;
      });
    return messagesObservable.toPromise();
  }

  get getLabels(): any {
    return this.homeManageLabels;
  }

  get getMessages(): any {
    return this.homeManageMessages;
  }

  private handleError(error: Response) {
    return observableThrowError(error.json() || 'Unable to load resource.');
  }
}

