import { Component, OnInit, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { FormArray, FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';

import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { SelectItem } from 'primeng/api';

import { UsersListService } from 'src/app/shared/services/users-list.service';
import { UserSettings } from 'src/app/shared/classes/user-settings';
import { AuthenticateService } from 'src/app/shared/services/authenticate.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { AdminRoles } from 'src/app/admin/enums/admin-roles.enum';
import { HeaderService } from 'src/app/shared/services/header.service';
import { WindowService } from 'src/app/shared/services/window.service';
import { AppConfigService } from 'src/app/app-config.service';
import { RegularExp } from 'src/app/shared/classes/regex';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'homemanagement-add-user',
  templateUrl: './add-user.component.html',
  styleUrls: ['./add-user.component.scss']
})
export class AddUserComponent implements OnInit {
  labels: any;
  validations: any;

  public config: PerfectScrollbarConfigInterface = {
    suppressScrollX: true
  };
  mobileFilterSection = false;
  mobileFilter = false;
  innerWidth: any;

  companyId: any;
  userId: any;
  userInfo: any;
  active: any;

  template: any;
  templateReplaceItems = [];

  showAlert = false;

  adminRolesEnum: typeof AdminRoles = AdminRoles;

  regularExp = RegularExp;

  userInfoTitle = 'Add User';
  companyDetails: any;
  status: boolean = false;

  selectedRoleType: string;
  selectedRole: any;

  roleTypes = [];
  privileges = [];
  selectedPrivileges = [];

  data = {
    users: [{ email: '', name: '' }]
  };

  addUserForm: FormGroup;

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private usersListService: UsersListService,
    private modalService: ModalService,
    private authenticateService: AuthenticateService,
    private headerService: HeaderService,
    private windowService: WindowService,
    private appConfigService: AppConfigService
  ) { }

  ngOnInit() {
    this.innerWidth = this.windowService.windowRef.innerWidth;
    this.mobileFilterSection = (this.innerWidth < 768) ? true : false;

    this.labels = this.appConfigService.getLabels;
    this.validations = this.appConfigService.getMessages; 

    this.createFormControls();
    this.createForm();

    this.getUserDetails();

    this.readTextFile( environment.apiUrl + "assets/accept_invitation_eMail_Template.html");
  }

  ngAfterViewInit() {
    const headerObject = {
      title: 'Add User',
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

  getUsersFormArray(form) {
    // console.log(form.get('users').controls);
    return form.controls.users.controls;
  }

  getUserDetails() {
    const userName = UserSettings.getUsername;
    this.usersListService.getCompanyDetailsByUsername(userName)
      .subscribe(data => {
        this.companyDetails = data['result'][0];

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

  setPrivilege(privilege) {
    privilege.checked = !privilege.checked;
    if (privilege.checked) {
      this.selectedPrivileges.push({
        "privilegeId": privilege.id,
        "name": privilege.name,
        "role_id": this.selectedRole.id,
        "user_id": "",
        "value": privilege.value
      });
    } else {
      if (this.selectedPrivileges.find(row => row.privilegeId == privilege.id)) {
        this.selectedPrivileges.splice(this.selectedPrivileges.indexOf(this.selectedPrivileges.find(row => row.privilegeId == privilege.id)), 1);
      }
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
    }, err => {
    });
  }

  createFormControls() {
    this.addUserForm = new FormGroup({
      users: this.fb.array([])
    });
  }

  createForm() {
    let control = <FormArray>this.addUserForm.controls.users;
    this.data.users.forEach(x => {
      control.push(this.fb.group({
        email: ['', [Validators.required, Validators.email, Validators.pattern(this.regularExp.emailPattern)]],
        name: ['']
      }))
    })
  }

  addNewUser() {
    let control = <FormArray>this.addUserForm.controls.users;
    control.push(
      this.fb.group({
        email: ['', [Validators.required, Validators.email, Validators.pattern(this.regularExp.emailPattern)]],
        name: ['']
      })
    )
  }

  deleteUser(index) {
    let control = <FormArray>this.addUserForm.controls.users;
    control.removeAt(index)
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
          "role_id": this.selectedRole.id,
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

  generateEmailToken(min, max) {
    return Math.floor(Math.random() * (max - min + 1) + min);
  }

  addUser() {
    // this.generateEmailToken(0, 9999999999999999)
    if (this.addUserForm.valid) {
      const usersList = [];
      this.addUserForm.value.users.forEach((user, index) => {
        const usersObject = {
          firstName: user.name,
          lastName: '',
          email: user.email,
          username: user.email,
          password: 'test@1',
          designation: this.selectedRoleType,
          phone: '9876543210',
          companyName: this.companyDetails.name,
          companyId: this.companyDetails.id,
          roleId: this.selectedRole.id,
          companyAddress: this.companyDetails.address,
          enabled: true,
          noOfUsers: 100,
          isActive: 1,
          isSI: '1',
          privilegesMapping: this.selectedPrivileges
        };       
        usersList.push(usersObject);
        const userProfile = UserSettings.getUserProfile;
        const emailObj = {
          templateType: 'acceptInvitation',
          template: this.template,
          fromUser: userProfile.firstName ? userProfile.firstName : userProfile.username,
          toUser: user.email,
          userType: this.selectedRoleType,
          templateReplaceItems: this.templateReplaceItems,
          email_url: '',
          email_subject: 'Request to join'
        };
        if ((this.addUserForm.value.users.length - 1) === index) {
          this.addUserBeforeEmailSent(usersList, emailObj);
        }
      });
    }
  }

  addUserBeforeEmailSent(usersList, emailObj) {
    this.usersListService.getMultipleCreateUsers(usersList).subscribe(res => {
      this.showAlert = true;
      
      emailObj.email_url = environment.apiUrl +  '#/accept-invitation/' + res['result'].id;
      this.sendEmailToUser(emailObj);
    }, error => {
      this.showAlert = false;
      const modalOptions = {
        icon: 'assets/homemanage_icons/shared/circle_wrong.svg',
        bodyText: 'Invalid User details, Please verify your details'
      };
      this.modalService.showErrorModal(modalOptions);
    });
  }

  sendEmailToUser(emailObj) {
    this.usersListService.sentEmail(emailObj).subscribe(emailResult => {
      console.log("Email Sent");
      const modalOptions = {
        icon: 'assets/homemanage_icons/shared/circle_tick.svg',
        bodyText: 'User Added and Email Sent Successfully to ' +  emailObj.toUser + '.'
      };
      this.modalService.showSuccessModal(modalOptions);
    }, error => {
      const modalOptions = {
        icon: 'assets/homemanage_icons/shared/circle_wrong.svg',
        bodyText: 'Email Not Sent'
      };
      this.modalService.showErrorModal(modalOptions);
    });
  }

  readTextFile(file) {
    let rawFile = new XMLHttpRequest();
    rawFile.open("GET", file, false);
    rawFile.onreadystatechange = () => {
      if(rawFile.readyState === 4) {
        if(rawFile.status === 200 || rawFile.status == 0) {
          const allText = rawFile.responseText;
          const finalStingTemplate = (rawFile.responseText.replace(/\n/g, '')).trim();
          return this.storeTemplate( finalStingTemplate );
        }
      }
    }
    rawFile.send(null);
  }

  storeTemplate(finalStingTemplate) {
    let item;
    const regX = RegularExp.stringBetweenDoubleCurlybraces;
    while (item = regX.exec(finalStingTemplate)) {
      finalStingTemplate = finalStingTemplate.replace(item[0], item[1]);
      this.templateReplaceItems.push(item[1]);
    }
    this.template = finalStingTemplate;
  }

}

