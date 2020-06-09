import { Component, OnInit, HostListener } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { Location } from '@angular/common';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { UsersListService } from 'src/app/shared/services/users-list.service';
import { HeaderService } from 'src/app/shared/services/header.service';

import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { WindowService } from 'src/app/shared/services/window.service';
import { UserStatus } from 'src/app/shared/enums/user-status.enum';
import { UserSettings } from 'src/app/shared/classes/user-settings';
import { UserProfileService } from 'src/app/shared/services/user-profile.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { AppConfigService } from 'src/app/app-config.service';

import { RegularExp } from 'src/app/shared/classes/regex';
import { matchOtherValidator } from 'src/app/shared/classes/matchOtherValidator';
import * as moment from 'moment';

@Component({
  selector: 'homemanagement-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.scss']
})
export class MyProfileComponent implements OnInit {
  public config: PerfectScrollbarConfigInterface = {
    suppressScrollX: true
  };

  labels: any;
  validations: any;
  profileTabs: string[] = ['General Info','Settings', 'My Activity'];
  selectedTab = this.profileTabs[0];
  
  regularExp = RegularExp;

  userProfile: any;
  userProfileForm: FormGroup;
  username: FormControl;
  email: FormControl;
  companyname: FormControl;
  address: FormControl;
  oldpassword: FormControl;
  newpassword: FormControl;
  confirmpassword: FormControl;
  notificationOnscreen: FormControl;
  notificationByEmail: FormControl;
  isGeneralInfoReadOnly = true;
  isPasswordReadOnly = true;
 
  result: any = null;
  isWebview = false;
  showPassword = false;

  userStatus = UserStatus;

  userId: any;
  showAlert = false;

  status: boolean = false;
  userInfoTitle = 'User Info';
  selectedRoleType: string = 'Enduser';
  registeredDate: string;

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private usersListService: UsersListService,
    private headerService: HeaderService,
    private windowService: WindowService,
    private appConfigService: AppConfigService,
    private userProfileService: UserProfileService,
    private modalService: ModalService,
    private location: Location
  ) { }

  ngOnInit() {
    this.labels = this.appConfigService.getLabels;
    this.validations = this.appConfigService.getMessages;

    const userName = UserSettings.getUsername;
    this.getUserProfile(userName);
  }

  getUserProfile(userName) {
    this.userProfileService.getUserProfile(userName).subscribe( data => {
      this.userProfile = data.result;
      UserSettings.setUserProfile = data.result;
      this.registeredDate =  moment(this.userProfile.createTimestamp).format("MMM DD, HH:mm, YYYY");

      this.createFormControls();
      this.createForm();
    });
  }

  createFormControls() {
    this.username = new FormControl(this.userProfile.firstName);
    this.email = new FormControl(this.userProfile.username, [ Validators.required, Validators.email, Validators.pattern(this.regularExp.emailPattern) ]);
    this.companyname = new FormControl(this.userProfile.companyName, Validators.required);
    this.address = new FormControl(this.userProfile.username);

    this.oldpassword = new FormControl(this.userProfile.password, [
      Validators.required,
      Validators.pattern(this.regularExp.passwordPattern)
    ]);

    this.newpassword = new FormControl('', [
      Validators.pattern(this.regularExp.passwordPattern)
    ]);
    this.confirmpassword = new FormControl('', [
      matchOtherValidator('newpassword'),   
      Validators.pattern(this.regularExp.passwordPattern)
    ]);

    this.notificationOnscreen = new FormControl('false');
    this.notificationByEmail = new FormControl('false');
  }

  createForm() {
    this.userProfileForm = new FormGroup({
      username: this.username,
      email: this.email,
      companyname: this.companyname,
      address: this.address,
      oldpassword: this.oldpassword,
      newpassword: this.newpassword,
      confirmpassword: this.confirmpassword,
      notificationOnscreen: this.notificationOnscreen,
      notificationByEmail: this.notificationByEmail
    });
  }

  ngAfterViewInit() {
    const headerObject = {
      title: 'User Profile',
      back: 'arrow_back',
      setactions: true,
      notification: true
    };
    this.headerService.setTitle(headerObject);
  }

  ngOnDestroy(): void {
    this.headerService.setTitle({});
  }

  toggleTabs(anchorHash, index) {
    this.selectedTab = anchorHash;
    setTimeout(() => {
      const anchor = document.getElementById('tab' + index);
      if (anchor) {
          anchor.focus();
          anchor.scrollIntoView();
      }
    });
  }

  editGeneralInfo() {
    this.isGeneralInfoReadOnly = false;    
    this.isPasswordReadOnly = false;
  }

  saveGeneralInfo() {
    this.isGeneralInfoReadOnly = true;
    if (this.userProfileForm.valid) {
      this.userProfile.firstName = this.userProfileForm.value.username;
      this.userProfile.password = this.userProfileForm.value.newpassword;
      
      this.usersListService.updateUser(this.userProfile).subscribe(result => {
        console.log(result);
        this.showAlert = true;
        const modalOptions = {
          icon: 'assets/homemanage_icons/shared/circle_tick.svg',
          bodyText: 'Profile Updated Successfully.'
        };
        this.modalService.showSuccessModal(modalOptions);
      }, error => {
        this.showAlert = false;
        const modalOptions = {
          icon: 'assets/homemanage_icons/shared/circle_wrong.svg',
          bodyText: 'Invalid details, Please verify your details'
        };
        this.modalService.showErrorModal(modalOptions);
      });
    }
    
  }
}

