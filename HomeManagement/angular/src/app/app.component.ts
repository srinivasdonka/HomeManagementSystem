import { Component, NgZone } from '@angular/core';
import { AuthenticateService } from 'src/app/shared/services/authenticate.service';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { LoaderService } from 'src/app/loader/services/loader.service';
import { WindowRefService } from 'src/app/shared/services/window-ref.service';
import { RouterEvent } from '@angular/router';
import { RouteConfigLoadStart } from '@angular/router';
import { RouteConfigLoadEnd } from '@angular/router';
import { LocationStrategy } from '@angular/common';
import { UserSettings } from 'src/app/shared/classes/user-settings';
import { UserTypeEnum } from 'src/app/shared/enums/userType';

declare var window: any;

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})
export class AppComponent {
  title = 'HomeManagement';

  constructor(
    private authenticateService: AuthenticateService,
    private router: Router,
    private route: ActivatedRoute,
    private location: LocationStrategy,
    private loaderService: LoaderService,
    protected windowRef: WindowRefService,
    private zone: NgZone
  ) {}

  ngOnInit() {
    // alert(navigator.userAgent);
    // Update native app that our app is loaded
    this.showLoaderTillModuleLoads();
    this.loadModules();
  }

  loadModules() {
    const url = this.location.path();
    this.redirectByURL(url);
  }

  redirectByUserType() {
    switch (UserSettings.getUserType) {
      case UserTypeEnum.EndUser:
        this.router.navigate(['enduser/enduser-root']);
        break;
      case UserTypeEnum.Administrator:
        this.router.navigate(['admin/admin-root']);
        break;
      default:
        break;
    }
  }

  checkURL(url) {
    const userType = UserSettings.getUserType;
    if ((userType === UserTypeEnum.EndUser) && url.includes('enduser')) {
      this.router.navigate([url]);
    } else if ((userType === UserTypeEnum.Administrator) && url.includes('admin')) {
      this.router.navigate([url]);
    } else {
      this.redirectByUserType();
    }
  }

  redirectByURL(url) {
    const isAuthenticatedURL = /registration|forgotpassword|resetpassword|checkemail|verifyemail|accept-invitation/.test(url);
    if (isAuthenticatedURL) {
      this.router.navigate([url]);
    } else {
      this.redirectByUserType();
    }
  }

  showLoaderTillModuleLoads() {
    this.router.events.subscribe((event: RouterEvent): void => {
      if (event instanceof RouteConfigLoadStart) {
        console.log('load start count', this.loaderService.contentLoadingCount.value);
        this.loaderService.start();
        console.log('load start', event);
      } else if (event instanceof RouteConfigLoadEnd) {
        console.log('load end count', this.loaderService.contentLoadingCount.value);
        this.loaderService.stop();
        console.log('load end', Date.now());
      }
    });
  }
}
