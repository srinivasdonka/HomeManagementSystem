import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class SetterGetterService {

  public _data: any;

  set data(value: any) {
    this._data = value
  }

  get data(): any {
    return this._data;
  }
}
