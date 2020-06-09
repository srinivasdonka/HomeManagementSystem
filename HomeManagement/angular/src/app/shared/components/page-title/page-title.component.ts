import { Component, OnInit, Input } from '@angular/core';
import { Location } from '@angular/common';
import { AppConfigService } from 'src/app/app-config.service';

@Component({
  selector: 'homemanagement-page-title',
  templateUrl: './page-title.component.html',
  styleUrls: ['./page-title.component.scss']
})
export class PageTitleComponent implements OnInit {
  @Input()title: any;

  labels: any;

  constructor(
    private location: Location,
    private appConfigService: AppConfigService
  ) { }

  ngOnInit() {
    this.labels = this.appConfigService.getLabels;
  }

  navigateBack() {
    this.location.back();
  }
}




