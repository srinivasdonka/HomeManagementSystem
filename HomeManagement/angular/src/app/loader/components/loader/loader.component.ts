import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';

import { LoaderService } from '../../services/loader.service';

@Component({
  selector: 'homemanagement-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.scss']
})
export class LoaderComponent implements OnInit, OnDestroy {
  showLoader = false;
  loaderSubscription: Subscription;

  constructor(private loaderService: LoaderService) {}

  ngOnInit() {
    this.loaderSubscription = this.loaderService.contentLoadingCount.subscribe((counter: number) => {
      this.showLoader = counter > 0;
    });
  }

  ngOnDestroy() {
    this.loaderSubscription.unsubscribe();
  }
}


