import { Component, OnInit, AfterViewInit, OnDestroy } from '@angular/core';
import { UserSettings } from 'src/app/shared/classes/user-settings';
import { HeaderService } from 'src/app/shared/services/header.service';

@Component({
  selector: 'homemanagement-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardComponent implements OnInit, AfterViewInit, OnDestroy {
  userName: any;

  constructor(
    public headerService: HeaderService
  ) { }

  ngOnInit() {
    this.userName = UserSettings.getUsername;
  }

  ngAfterViewInit() {
    const headerObject = {
      title: 'Dashboard',
      logo: true,
      menu: 'menu',
      actions: true,
      setactions: true,
      notification: true
    };
    this.headerService.setTitle(headerObject);
  }

  ngOnDestroy(): void {
    this.headerService.setTitle({});
  }

}
