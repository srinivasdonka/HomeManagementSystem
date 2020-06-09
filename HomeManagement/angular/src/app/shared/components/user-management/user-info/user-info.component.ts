import { Component, OnInit, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { UsersListService } from 'src/app/shared/services/users-list.service';
import { HeaderService } from 'src/app/shared/services/header.service';

import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { WindowService } from 'src/app/shared/services/window.service';
import { UserStatus } from 'src/app/shared/enums/user-status.enum';
import { AppConfigService } from 'src/app/app-config.service';
import * as moment from 'moment';

@Component({
  selector: 'homemanagement-user-info',
  templateUrl: './user-info.component.html',
  styleUrls: ['./user-info.component.scss']
})
export class UserInfoComponent implements OnInit {
  public config: PerfectScrollbarConfigInterface = {
    suppressScrollX: true
  };

  labels: any;
  validations: any;
  
  mobileFilterSection = false;
  mobileFilter = false;
  innerWidth: any;

  userStatus = UserStatus;

  userId: any;
  userInfo: any;
  active: any;
  showAlert = false;

  status: boolean = false;
  privileges = [];

  page = 0;
  size = 10;

  userInfoTitle = '';
  selectedRoleType: string = 'Enduser';
  registeredDate: string;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private usersListService: UsersListService,
    private headerService: HeaderService,
    private windowService: WindowService,
    private appConfigService: AppConfigService
  ) { }

  ngOnInit() {
    this.labels = this.appConfigService.getLabels;
    this.validations = this.appConfigService.getMessages;

    this.innerWidth = this.windowService.windowRef.innerWidth;
    console.log(this.innerWidth);
    this.mobileFilterSection = (this.innerWidth < 768) ? true: false;
    this.route.params.subscribe(params => {
      this.userId = params['userId'];
      this.loadUserInfo(this.userId, this.active);
    });
  }

  ngAfterViewInit() {
    const headerObject = {
      title: 'User Info',
      back: 'arrow_back',
      setactions: true,
      notification: true
    };
    this.headerService.setTitle(headerObject);
  }

  ngOnDestroy(): void {
    this.headerService.setTitle({});
  }

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.innerWidth = this.windowService.windowRef.innerWidth;
    console.log(this.innerWidth);
    this.mobileFilterSection = (this.innerWidth < 768) ? true: false;
  }

  loadUserInfo(userId, active) {
    this.usersListService.getUserInfoById(userId)
      .subscribe(data => {
        this.userInfo = data['result'];
        this.registeredDate =  moment(this.userInfo.createTimestamp).format("MMM DD, HH:mm, YYYY");
        this.selectedRoleType = this.userInfo.designation;
        
        this.getPrivileges();        
      }, err => {
    });
  }

  clickEvent() {
    this.status = !this.status;
  }

  editUser() {
    this.router.navigate(['/admin/admin-root/edituser/' + this.userInfo.id]);
  }

  getStatusTest() {
    if (this.userInfo && this.userInfo.status === this.userStatus.active) {
      return this.labels.BUTTONS.DEACTIVATE;
    } else {
      return this.labels.BUTTONS.ACTIVATE;
    }
  }

  checkUserCanEditByStatus() {
    if (this.userInfo.status === this.userStatus.active) {
      return true;
    } else {
      return false;
    }
  }

  getPrivileges() {
    this.usersListService.getPrivileges().subscribe(data => {
      const privileges = data['result'];
      privileges.forEach(privilege => {
        this.privileges.push({
          id: privilege.id, 
          name: privilege.name, 
          roleId: privilege.role_id, 
          userId: privilege.user_id,          
          value: privilege.value,
          checked: false
        });
      });
      this.getPrivilegesByRoleIdandUserId(this.userInfo.role_id, this.userInfo.id);
    }, err => {
    });
  }

  getPrivilegesByRoleIdandUserId(roleId, userId) {
    this.usersListService.getPrivilegesByRoleIdandUserId(roleId, userId).subscribe(data => {
      const privilegesMapped = data['result'];

      privilegesMapped.forEach(selectedPrivilege => {
        this.privileges.forEach(privilege => {
          if (selectedPrivilege.privilegeId === privilege.id) {
            privilege.checked = true;
          }
        });        
      });
      
    }, error => {
      
    });
  }

  setStatus() {
    if (this.userInfo.status === this.userStatus.active) {
      this.userInfo.status = this.userStatus.deactive;
    } else {
      this.userInfo.status = this.userStatus.active;
    }

    this.userInfo.isActive = this.userInfo.status;
    this.userInfo.roleId = this.userInfo.role_id;
    
    this.usersListService.updateUser(this.userInfo).subscribe(result => {
      console.log(result);
      this.checkUserCanEditByStatus();
      this.showAlert = true;
        setTimeout(() => {
          this.showAlert = false;
        }, 3000);
    }, error => {
      
    });
  }
}
