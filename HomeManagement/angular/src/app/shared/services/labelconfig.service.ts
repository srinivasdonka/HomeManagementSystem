import {throwError as observableThrowError,  Observable } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { catchError } from 'rxjs/operators';
import {empty} from 'rxjs';

export function initLabels(config: LabelconfigService) {
  return () => config.loadhomemanageLabels();
}
export function initMessages(config: LabelconfigService) {
  return () => config.loadhomemanageMessages();
}

@Injectable()
export class LabelconfigService {
  private homemanageLabels: any;
  private homemanageMessages: any;
  constructor(private http: HttpClient) {
    this.loadhomemanageLabels().subscribe(data => {
      this.homemanageLabels = data;
    });
    this.loadhomemanageMessages().subscribe(data => {
      this.homemanageMessages = data;
    });
  }

  public loadhomemanageLabels(): Observable<any> {
    return this.http.get("assets/json/labels.json")
  }

  public loadhomemanageMessages(): Observable<any> {
    return this.http.get("assets/json/validations.json")
  }
}



