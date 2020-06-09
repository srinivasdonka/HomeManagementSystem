import { Component, OnInit, Input, EventEmitter, Output, HostListener } from '@angular/core';
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
import { HeaderService } from 'src/app/shared/services/header.service';
import { AppConfigService } from 'src/app/app-config.service';

@Component({
  selector: 'homemanagement-edit-users',
  templateUrl: './edit-users.component.html',
  styleUrls: ['./edit-users.component.scss']
})
export class EditUsersComponent implements OnInit {
  @Input() usersList: any;
  @Output() usersListView = new EventEmitter<string>();

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
  showLess = true;

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
  
  constructor(
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private usersListService: UsersListService,
    private modalService: ModalService,
    private authenticateService: AuthenticateService,
    public headerService: HeaderService,
    private windowService: WindowService,
    private appConfigService: AppConfigService
  ) { }

  ngOnInit() {
    this.labels = this.appConfigService.getLabels;
    this.validations = this.appConfigService.getMessages;

    this.data.users = this.usersList;
    this.selectedRoleType = this.usersList[0].designation;
    this.innerWidth = this.windowService.windowRef.innerWidth;
    this.mobileFilterSection = (this.innerWidth < 768) ? true: false;
    
    this.getRoles();
    this.getPrivileges();

    this.createFormControls();
    this.createForm();
  }

  ngAfterViewInit() {
    const headerObject = {
      title: 'Edit User',
      reload: true,
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
    this.mobileFilterSection = (this.innerWidth < 768) ? true: false;
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

  setPrivilege(privilege) {
    privilege.checked = !privilege.checked;
    if (privilege.checked) {
      this.selectedPrivileges.push({
        "privilegeId": privilege.id,
        "name": privilege.name,
        "value": privilege.value,
        "role_id": "",
        "user_id": ""
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
    if (this.selectedRoleType.toLowerCase() == "administrator") {
      this.selectedPrivileges = [];
      this.privileges.forEach(privilege => {
        privilege.checked = true;
        this.selectedPrivileges.push({
          "privilegeId": privilege.id,
          "name": privilege.name,
          "role_id": "",
          "user_id": "",
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

  getPrivileges() {
    this.usersListService.getPrivileges().subscribe(data => {
      const privileges = data['result'];
      privileges.forEach(privilege => {
          this.privileges.push({
            id: privilege.id,
            name: privilege.name,
            roleId: "",
            userId: "",
            value: privilege.value,
            checked: false
          });
      });
      this.selectedRoleTypeChanged(this.selectedRoleType);
    }, err => {
    });
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
        username: x.username,
        firstName: x.firstName
      }));
    });
  }

  deleteUser(index) {
    let control = <FormArray>this.editUserForm.controls.users;
    control.removeAt(index)
  }

  updateUser() {
    if (this.editUserForm.valid) {
      const usersList = [];
      this.data.users.forEach( (user, index) => {
        let userPrivileges = JSON.parse(JSON.stringify(this.selectedPrivileges));
        userPrivileges.forEach(element => {
          element.user_id = user.id
          });
        const usersObject = {
          accountNonExpired: user.accountNonExpired,
          accountNonLocked: user.accountNonLocked,
          authorities: user.authorities,
          checked: user.checked,
          companyName: user.companyName,
          companyAddress: user.companyAddress,
          createTimestamp: user.createTimestamp,
          credentialsNonExpired: user.credentialsNonExpired,
          designation: this.selectedRoleType,
          enabled: user.enabled,
          firstName: this.editUserForm.value.users[index].firstName,
          userId: user.id,
          lastName: user.lastName,
          password: user.password,
          phone: user.phone,
          roleId: this.selectedRole.id,
          status: user.status,
          updatedTimestamp: user.updatedTimestamp,
          username: this.editUserForm.value.users[index].username,
          email: this.editUserForm.value.users[index].username,          
          noOfUsers: user.noOfUsers,
          isActive: user.isActive,
          isSI: user.isSI,          
          privilegesMapping: userPrivileges
        };
        usersList.push(usersObject);
                
        if ((this.editUserForm.value.users.length - 1) === index) {
          this.usersListService.updateMutlipleUser(usersList).subscribe(result => {
            console.log(result);
            this.showAlert = true;
            const modalOptions = {
              icon: 'assets/homemanage_icons/shared/circle_tick.svg',
              bodyText: 'Updated Users Successfully.'
            };
            this.modalService.showSuccessModal(modalOptions);
            setTimeout(() => {              
              this.router.navigate(['/admin/admin-root/usermanagement']);
            }, 3000);
          }, error => {
            this.showAlert = false;
            const modalOptions = {
              icon: 'assets/homemanage_icons/shared/circle_wrong.svg',
              bodyText: 'Invalid details, Please verify your details'
            };
            this.modalService.showErrorModal(modalOptions);
          });
        }
      });      
    }
  }

  goBack() {
    this.usersListView.emit('back');
  }
}



