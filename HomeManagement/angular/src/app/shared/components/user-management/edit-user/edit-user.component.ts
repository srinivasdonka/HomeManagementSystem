import { Component, OnInit, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { FormArray, FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';

import { SelectItem } from 'primeng/api';

import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { WindowService } from 'src/app/shared/services/window.service';
import { UserStatus } from 'src/app/shared/enums/user-status.enum';

import { UsersListService } from 'src/app/shared/services/users-list.service';
import { UserSettings } from 'src/app/shared/classes/user-settings';
import { AuthenticateService } from 'src/app/shared/services/authenticate.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { AdminRoles } from 'src/app/admin/enums/admin-roles.enum';
import { SetterGetterService } from 'src/app/shared/services/setter-getter.service';
import { HeaderService } from 'src/app/shared/services/header.service';
import { AppConfigService } from 'src/app/app-config.service';
import { Location } from '@angular/common';
import * as moment from 'moment';

@Component({
  selector: 'homemanagement-edit-user',
  templateUrl: './edit-user.component.html',
  styleUrls: ['./edit-user.component.scss']
})
export class EditUserComponent implements OnInit {
  public config: PerfectScrollbarConfigInterface = {
    suppressScrollX: true
  };

  labels: any;
  validations: any;
  mobileFilterSection = false;
  mobileFilter = false;
  innerWidth: any;

  userStatus = UserStatus;
  adminRolesEnum: typeof AdminRoles = AdminRoles;

  userId: any;
  userInfo: any;
  active: any;
  showAlert = false;
  status: boolean = false;

  userInfoTitle = 'Edit User';
  companyDetails: any;

  selectedRoleType: string;
  selectedRole: any;

  roleTypes = [];
  privileges = [];
  selectedPrivileges = [];

  data = {
    users: []
  };

  editUserForm: FormGroup;
  registeredDate: string;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private usersListService: UsersListService,
    private modalService: ModalService,
    private authenticateService: AuthenticateService,
    private setterGetterService: SetterGetterService,
    public headerService: HeaderService,
    public windowService: WindowService,
    private appConfigService: AppConfigService,
    private location: Location
  ) { }

  ngOnInit() {
    this.labels = this.appConfigService.getLabels;
    this.validations = this.appConfigService.getMessages;

    this.innerWidth = this.windowService.windowRef.innerWidth;
    this.mobileFilterSection = (this.innerWidth < 768) ? true : false;

    this.route.params.subscribe(params => {
      this.userId = params['userId'];
      this.loadUserInfo(this.userId, this.active);
    });
  }

  ngAfterViewInit() {
    const headerObject = {
      title: 'Edit User',
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
    this.mobileFilterSection = (this.innerWidth < 768) ? true : false;
  }

  loadUserInfo(userId, active) {
    this.usersListService.getUserInfoById(userId)
      .subscribe(data => {
        this.userInfo = data['result'];
        this.data.users.push(this.userInfo);
        this.selectedRoleType = this.userInfo.designation;
        this.userInfo.roleId = this.userInfo.role_id;
        this.registeredDate =  moment(this.userInfo.createTimestamp).format("MMM DD, HH:mm, YYYY");

        this.createFormControls();
        this.createForm();

        this.getRoles();
        this.getPrivileges();
      }, err => {
      });
  }

  getRoles() {
    this.usersListService.getRoles().subscribe(data => {
      const userRoles = data['result'];
      userRoles.forEach(role => {
        this.roleTypes.push({ label: role.name, value: role.name, id: role.id, description: role.description });
      });
    }, err => {
    });
  }

  clickEvent() {
    this.status = !this.status;
  }

  createFormControls() {
    this.editUserForm = new FormGroup({
      users: this.fb.array([])
    });
  }

  createForm() {
    let control = <FormArray>this.editUserForm.controls.users;
    this.data.users.forEach(x => {
      control.push(this.fb.group({
        firstName: x.firstName
      }))
    })
  }

  addNewUser() {
    let control = <FormArray>this.editUserForm.controls.users;
    control.push(
      this.fb.group({
        firstName: ['']
      })
    )
  }

  deleteUser(index) {
    let control = <FormArray>this.editUserForm.controls.users;
    control.removeAt(index)
  }

  getStatusTest() {
    if (this.userInfo && this.userInfo.status === this.userStatus.active) {
      return this.userStatus.deactive;
    } else {
      return this.userStatus.active;
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

            this.selectedPrivileges.push({
              "privilegeId": privilege.id,
              "name": privilege.name,
              "value": privilege.value,
              "role_id": privilege.roleId,
              "user_id": this.userInfo.id
            });

          }
        });
      });

    }, error => {

    });
  }

  setPrivilege(privilege) {
    privilege.checked = !privilege.checked;
    if (privilege.checked) {
      this.selectedPrivileges.push({
        "privilegeId": privilege.id,
        "name": privilege.name,
        "role_id": this.userInfo.roleId,
        "user_id": this.userInfo.id,
        "value": privilege.value
      });
    } else {
      if (this.selectedPrivileges.find(row => row.privilegeId == privilege.id)) {
        this.selectedPrivileges.splice(this.selectedPrivileges.indexOf(this.selectedPrivileges.find(row => row.privilegeId == privilege.id)), 1);
      }
    }
  }

  selectedRoleTypeChanged(selectedRoleType) {
    this.selectedRoleType = selectedRoleType;
    const index = this.roleTypes.indexOf(this.roleTypes.find(role => role.value == selectedRoleType));

    this.selectedRole = this.roleTypes[index];
    this.userInfo.roleId = this.selectedRole.id;
    if (this.selectedRoleType.toLowerCase() == "administrator") {
      this.selectedPrivileges = [];
      this.privileges.forEach(privilege => {
        privilege.checked = true;
        this.selectedPrivileges.push({
          "privilegeId": privilege.id,
          "name": privilege.name,
          "role_id": this.userInfo.roleId,
          "user_id": this.userInfo.id,
          "value": privilege.value
        });
      });
    }
    else {
      this.privileges.forEach(privilege => { privilege.checked = false; });
      this.selectedPrivileges = [];
    }
    console.log(this.selectedRole);
  }

  updateUser() {
    if (this.editUserForm.valid) {
      this.userInfo.firstName = this.editUserForm.value.users[0].firstName;
      this.userInfo.designation = this.selectedRoleType;
      this.userInfo.userId = this.userInfo.id;

      this.updatePrivilege();

      this.usersListService.updateUser(this.userInfo).subscribe(result => {
        console.log(result);
        this.showAlert = true;
        const modalOptions = {
          icon: 'assets/homemanage_icons/shared/circle_tick.svg',
          bodyText: 'User Added Successfully. To access this device, please click on User Management from the Menu.'
        };
        this.modalService.showSuccessModal(modalOptions);
      }, error => {
        const modalOptions = {
          icon: 'assets/homemanage_icons/shared/circle_wrong.svg',
          bodyText: 'Invalid details, Please verify your details'
        };
        this.modalService.showErrorModal(modalOptions);
      });
    }
  }

  updatePrivilege() {
    this.usersListService.updatePrivileges(this.selectedPrivileges).subscribe(result => {
      console.log(result);
    }, error => { });
  }

  cancelEditUser() {
    this.location.back();
  }
}
