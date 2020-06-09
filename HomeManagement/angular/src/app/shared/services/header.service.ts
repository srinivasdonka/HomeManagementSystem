import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable()
export class HeaderService {
  public headerObject = new BehaviorSubject<any>({});
  public pageHeaderObject = new BehaviorSubject<any>({});

  constructor() { }

  setTitle(headerObject) {
    this.headerObject.next(headerObject);
  }

  setPageTitle(pageHeaderObject) {
    this.pageHeaderObject.next(pageHeaderObject);
  }
}

