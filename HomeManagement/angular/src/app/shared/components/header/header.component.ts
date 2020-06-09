import {
  Component,
  OnInit,
  Output,
  EventEmitter,
  HostListener,
  Input,
  ViewChild,
  ElementRef,
  OnDestroy
} from '@angular/core';
import { Location } from '@angular/common';
import { Router } from '@angular/router';

import { OverlayPanelModule, OverlayPanel } from 'primeng/overlaypanel';

import { UserTypeEnum } from 'src/app/shared/enums/userType';
import { UserSettings } from '../../classes/user-settings';

import { AuthenticateService } from 'src/app/shared/services/authenticate.service';
import { UserProfileService } from 'src/app/shared/services/user-profile.service';
import { HeaderService } from 'src/app/shared/services/header.service';
import { AppConfigService } from 'src/app/app-config.service';
import { Routes } from 'src/app/shared/classes/routes';

@Component({
  selector: 'homemanagement-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {
  labels: any;
  validations: any;

  userProfile: any;

  loaded = false;
  showSideBar = false;
  @Output() showMenu = new EventEmitter();

  notificationsCount = 5;
  @ViewChild('notificationOP') notificationOP: OverlayPanel;
  @ViewChild('profileOP') profileOP: OverlayPanel;
  notifications = [
    {title: 'Nov 08, 03:45, 2018', description: 'Harry jack Added a new device in Network_abc...'},
    {title: 'Nov 08, 03:45, 2018', description: 'You got permission to access Data view'},
    {title: 'Nov 08, 03:45, 2018', description: 'Harry jack Added a new device in Network_abc...'},
    {title: 'Nov 08, 03:45, 2018', description: 'You got permission to access Data view'},
    {title: 'Nov 08, 03:45, 2018', description: 'Harry jack Added a new device in Network_abc...'},
    {title: 'Nov 08, 03:45, 2018', description: 'You got permission to access Data view'},
    {title: 'Nov 08, 03:45, 2018', description: 'Harry jack Added a new device in Network_abc...'},
    {title: 'Nov 08, 03:45, 2018', description: 'You got permission to access Data view'},
    {title: 'Nov 08, 03:45, 2018', description: 'Harry jack Added a new device in Network_abc...'},
    {title: 'Nov 08, 03:45, 2018', description: 'You got permission to access Data view'}
  ];

  public userTypeEnum: typeof UserTypeEnum = UserTypeEnum;

  headerObject = {
    menu: 'menu',
    title: 'Welcome',
    back: false,
    actions: true,
    setactions: false,
    reload: false
  };

  constructor(
    private router: Router,
    private authenticateService: AuthenticateService,
    private userProfileService: UserProfileService,
    private headerService: HeaderService,
    private location: Location,
    private appConfigService: AppConfigService
  ) { }

  ngOnInit() {
    this.labels = this.appConfigService.getLabels;
    this.validations = this.appConfigService.getMessages;

    const userName = UserSettings.getUsername;
    this.getUserProfile(userName);
    this.headerService.headerObject.subscribe(data => {
      this.headerObject = data;
    });
  }

  onMenuClick() {
    this.showSideBar = !this.showSideBar;
    this.showMenu.emit(this.showSideBar);
  }

  toggleNotificationPanel(event, notificationTarget) {
    this.notificationOP.toggle(event, notificationTarget);
  }
  toggleProfilenPanel(event, profileTarget) {
    this.profileOP.toggle(event, profileTarget);
  }

  navigateToRoot() {
    const isAuthenticated = this.authenticateService.isAuthenticated();
    if (isAuthenticated) {
      switch (UserSettings.getUserType) {
        case UserTypeEnum.EndUser:
          this.router.navigate(['enduser/enduser-root']);
          break;
        case UserTypeEnum.Administrator :
          this.router.navigate(['admin/admin-root']);
          break;
        default:
          break;
      }
    } else {
      this.authenticateService.logout();
    }
  }

  addDevice() {
    this.router.navigate(['admin/admin-root/homemanagement/add-expendature']);
  }

  getUserProfile(userName) {
    this.userProfileService.getUserProfile(userName).subscribe( data => {
      this.userProfile = data.result;
      UserSettings.setUserProfile = data.result;
    });
  }  

  showProfile() {
    this.router.navigate(['admin/admin-root/profile']); 
  }

  showGeneralInfo() {

  }

  reloadPage() {
    location.reload();
  }

  logout() {
    UserSettings.setUserLogout = 'true';
    this.authenticateService.logout();
  }

  hideNotifications() {
    this.notificationOP.hide();
  }
}
