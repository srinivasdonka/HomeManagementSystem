import { Component, OnInit, ViewEncapsulation, Input, Output, EventEmitter, TemplateRef, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { HeaderComponent } from '../header/header.component';
import { AuthenticateService } from '../../services/authenticate.service';
import { UserSettings } from '../../classes/user-settings';
import { AppConfigService } from 'src/app/app-config.service';
import { UsersListService } from 'src/app/shared/services/users-list.service';
import { UserProfileService } from '../../services/user-profile.service';

@Component({
  selector: 'homemanagement-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
  encapsulation: ViewEncapsulation.None
})
export class SidebarComponent implements OnInit {
  modalServiceRef: any;
  @Input() showMenu;
  @Input() routes;
  @Input() alwaysOpen;
  @Output() menuClicked = new EventEmitter();

  error = '';
  labels: any;
  validations: any;
  selectedIdx = -1;
  userProfile: any;

  showSubMenu = [];
  routesList = [];
  privileges = [];
  constructor(
    private authenticateService: AuthenticateService,
    private appConfigService: AppConfigService,
    private usersListService: UsersListService,
    private userProfileService: UserProfileService
  ) { }

  ngOnInit() {
    this.labels = this.appConfigService.getLabels;
    this.validations = this.appConfigService.getMessages;
    const userName = UserSettings.getUsername;
    this.getUserProfile(userName);
  }

  getPrivileges() {
    this.usersListService.getPrivileges().subscribe(data => {
      const privileges = data['result'];
      privileges.forEach(privilege => {
        this.routesList.push({
          id: privilege.id, 
          label: privilege.name, 
          routerLink: "./"+ privilege.name.toLowerCase().split(" ").join(""), 
          isEnabled: false
        });
      });
      if (this.userProfile && this.userProfile.role_id) {
        this.getPrivilegesByRoleIdandUserId(this.userProfile.role_id, this.userProfile.id);
      } else {
        this.routesList.push({
          id: '1234567890', 
          label: 'Home Management', 
          routerLink: './homemanagement',
          isEnabled: true
        }); 
      }
      
    }, err => {
    });
  }

  getUserProfile(userName) {
    this.userProfileService.getUserProfile(userName).subscribe( data => {
      this.userProfile = data.result;
      UserSettings.setUserProfile = data.result;
      this.getPrivileges();
    });
  }

  getPrivilegesByRoleIdandUserId(roleId, userId) {
    this.usersListService.getPrivilegesByRoleIdandUserId(roleId, userId).subscribe(data => {
      const userPermissions = data['result'];
      UserSettings.setUserPermissions = userPermissions;
      userPermissions.forEach(selectedPrivilege => {
        this.routesList.forEach(privilege => {
          if (selectedPrivilege.privilegeId === privilege.id) {
            privilege.isEnabled = true;
          }
        });        
      });
      
    }, error => {
      
    });
  }

  menuClick(index) {
    this.selectedIdx = index;
    if (!this.alwaysOpen) {
      this.menuClicked.emit(false);
    }
  }

  logout() {
    UserSettings.setUserLogout = 'true';
    this.authenticateService.logout();
  }

  getMenuId(index) {
    return 'menu' + index;
  }

  addDevice() {

  }
}
