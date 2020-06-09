import { Component, OnInit } from '@angular/core';
import { AppConfigService } from 'src/app/app-config.service';

@Component({
  selector: 'homemanagement-copyright-text',
  templateUrl: './copyright-text.component.html',
  styleUrls: ['./copyright-text.component.scss']
})
export class CopyrightTextComponent implements OnInit {
  labels: any;

  constructor(
    private appConfigService: AppConfigService
  ) { }

  ngOnInit() {
    this.labels = this.appConfigService.getLabels;
  }

}
