import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoaderService {
  public contentLoadingCount: BehaviorSubject<number> = new BehaviorSubject<number>(0);
  constructor() { }

  start(isRequired = true) {
    if (isRequired) {
      const counter = this.contentLoadingCount.value + 1;
      console.log('start:', counter);
      this.contentLoadingCount.next(counter);
    }
  }

  stop(isRequired = true) {
    if (isRequired) {
      const counter = this.contentLoadingCount.value - 1;
      console.log('stop:', counter);
      this.contentLoadingCount.next(counter);
      if (counter < 0) {
        this.contentLoadingCount.next(0);
      }
    }
  }

  reset() {
    this.contentLoadingCount.next(0);
  }
}

