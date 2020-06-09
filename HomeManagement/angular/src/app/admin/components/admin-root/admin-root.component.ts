import { Component, OnInit, Input, HostListener, ViewChild, AfterViewInit, OnDestroy, AfterContentInit } from '@angular/core';
import { Observable } from 'rxjs';
import { of } from 'rxjs';
import { Router, ActivatedRoute, NavigationStart, NavigationEnd, Event as NavigationEvent } from '@angular/router';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { AuthenticateService } from 'src/app/shared/services/authenticate.service';
import { AdminAppConstants } from 'src/app/admin/classes/admin-app-constants';

import { fromEvent } from 'rxjs';
import { merge } from 'rxjs';

import { AppConfigService } from 'src/app/app-config.service';
import { DeviceSettings } from 'src/app/shared/classes/device-settings';
import { THIS_EXPR } from '@angular/compiler/src/output/output_ast';

@Component({
  selector: 'homemanagement-admin-root',
  templateUrl: './admin-root.component.html',
  styleUrls: ['./admin-root.component.scss']
})
export class AdminRootComponent implements OnInit, OnDestroy {
  canLoadModule = false;
  showMenu = false;
  isAlwaysOpen = false;
  sidebarRoutes = [];
  footerRoutes = [];
  techType = 1;
  isDashboard = true;
  public config: PerfectScrollbarConfigInterface = {
    suppressScrollX: true
  };

  acceptCookie = true;
  labels: any;
  
  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private authenticateService: AuthenticateService,
    private appConfigService: AppConfigService
  ) { }

  ngOnInit() {
    this.sidebarRoutes = AdminAppConstants.adminSideBarMenuItems;
    
    // TODO: check how to optimize this
    of(window.innerWidth > 1024 ? true : false).subscribe((show) => {
      this.showMenu = show;
      this.isAlwaysOpen = show;
    });

    this.labels = this.appConfigService.getLabels;
    if ( DeviceSettings.getCookieEnabled == "false") {
      this.acceptCookie = false;
    } else {
      this.acceptCookie = true;
    }
  }

  onActivate(event: Event) {
    const container = document.querySelector('#scroll-container');
    container.scrollTop = 0;
  }

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.showMenu = event.target.innerWidth > 1024 ? true : false;
    this.isAlwaysOpen = this.showMenu;
  }

  onMenuClicked(show) {
    this.showMenu = !this.showMenu;
  }

  ngOnDestroy(): void {
    
  }

  acceptCookieChecked() {
    this.acceptCookie = false;
    DeviceSettings.setCookieEnabled = '' + this.acceptCookie;
  }
}
