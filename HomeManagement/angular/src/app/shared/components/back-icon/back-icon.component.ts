import { Component, OnInit, Input } from '@angular/core';
import { Location } from '@angular/common';
import { AppConfigService } from 'src/app/app-config.service';

@Component({
  selector: 'homemanagement-back-icon',
  templateUrl: './back-icon.component.html',
  styleUrls: ['./back-icon.component.scss']
})
export class BackIconComponent implements OnInit {
  @Input()color: string;

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

