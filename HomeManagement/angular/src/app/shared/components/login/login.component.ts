import { Component, OnInit, OnDestroy } from '@angular/core';
import { Location } from '@angular/common';
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';

import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';

import { environment } from 'src/environments/environment';
import { AuthenticateService } from 'src/app/shared/services/authenticate.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { ReCaptchaV3Service } from 'ngx-captcha';
import { AppConfigService } from 'src/app/app-config.service';
import { RegularExp } from 'src/app/shared/classes/regex';
import { UsersListService } from 'src/app/shared/services/users-list.service';
import { CarouselConfig } from 'ngx-bootstrap/carousel';
import { UserProfileService } from 'src/app/shared/services/user-profile.service';
import { UserSettings } from 'src/app/shared/classes/user-settings';
import { UserTypeEnum } from 'src/app/shared/enums/userType';
import * as moment from 'moment'

@Component({
  selector: 'homemanagement-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
  providers: [
    { provide: CarouselConfig, useValue: { interval: 2000, noPause: true, showIndicators: false } }
  ]
})
export class LoginComponent implements OnInit, OnDestroy {
  labels: any;
  validations: any;

  public config: PerfectScrollbarConfigInterface = {
    suppressScrollX: true,
    wheelPropagation: true
  };

  regularExp = RegularExp;

  loggedIn: boolean;
  userProfile: any;

  loginForm: FormGroup;
  email: FormControl;
  password: FormControl;
  recaptcha: FormControl;

  returnUrl: string;

  loginBtn = false;
  showPassword = false;
  loading = false;
  ifWrongPassword = false;

  loginSubscription: Subscription;
  clientName = environment.clientName;
  siteKey = environment.siteKey;

  error = '';
  activateUserId: "";

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private formBuilder: FormBuilder,
    private modalService: ModalService,
    private authenticateService: AuthenticateService,
    private reCaptchaV3Service: ReCaptchaV3Service,
    private appConfigService: AppConfigService,
    private userProfileService: UserProfileService,
    private usersListService: UsersListService
  ) {
  }

  ngOnInit() {
    this.labels = this.appConfigService.getLabels;
    this.validations = this.appConfigService.getMessages;
    this.route.params.subscribe(params => {
      this.activateUserId = params['id'];
    });
    this.createFormControls();
    this.createForm();
  }

  createFormControls() {
    this.email = new FormControl('', [Validators.required, Validators.email, Validators.pattern(this.regularExp.emailPattern)]);
    this.password = new FormControl('', [
      Validators.required,
      Validators.pattern(this.regularExp.passwordPattern)
    ]);
    this.recaptcha = new FormControl('');
  }

  createForm() {
    this.loginForm = new FormGroup({
      email: this.email,
      password: this.password,
      recaptcha: this.recaptcha
    });
  }

  ngOnDestroy(): void {
    if (this.loginSubscription) {
      this.loginSubscription.unsubscribe();
    }
  }

  login() {
    if (this.loginForm.valid) {
      this.loginBtn = true;
      // get return url from route parameters or default to '/'
      this.returnUrl = this.route.snapshot.queryParams['returnUrl'] || '/';

      const userObject = 'username=' + this.loginForm.value.email + '&password=' + this.loginForm.value.password +
        '&client_secret=' + environment.client_secret + '&client_id=' + environment.clientId +
        '&grant_type=' + environment.grant_type + '&scope=write';

      this.authenticateService.login(userObject, this.loginForm.value.email).subscribe(
        data => {
          if (data) {
            console.log(data);
            if (this.activateUserId && this.activateUserId != "") {
              this.userProfileService.updateUserActivation(this.activateUserId).subscribe(data => {
                this.getUserProfile();
              })
            } else {
              this.getUserProfile();
            }
          }
        },
        error => {
          console.log(error);
          if (error.status === 400 && error['error']['error'] === 'invalid_grant') {
            this.ifWrongPassword = true;
          }
        }
      );
    }
  }

  onSuccess() {

  }

  onFailure() {

  }

  getUserProfile() {
    const userName = UserSettings.getUsername;
    this.userProfileService.getUserProfile(userName).subscribe(data => {
      this.userProfile = data.result;
      const user_status = this.userProfile.status;
      if(user_status == "Active") {
      UserSettings.setUserProfile = data.result;
      this.getAllRoles(this.userProfile);

      const date = moment.utc().format();
      const loginTime = moment.utc(date).local().format("MMM DD, HH:mm, YYYY");
      this.userProfile.lastLogin = loginTime;
      UserSettings.setUserProfile = this.userProfile;
      this.userProfileService.updateUserLastLogin(this.userProfile.username, loginTime).subscribe(result => {
        console.log(result);
      }, error => { });
    } else if (user_status == "Activation Pending") {
      const modalOptions = {
        icon: 'assets/homemanage_icons/shared/circle_wrong.svg',
        bodyText: 'Activation Pending. Please use the activation link sent to your registered email account.'
      };
      this.modalService.showErrorModal(modalOptions);
    }
    });

  }

  getAllRoles(userProfile) {
    this.userProfileService.getAllRoles().subscribe(data => {
      let suserProfile = data.result;
      data.result.forEach(item => {
        if (item.id === userProfile.role_id) {
          switch (item.name) {
            case 'Administrator':
              UserSettings.setUserType = UserTypeEnum.Administrator;
              break;
            case 'System Integrator':
              UserSettings.setUserType = UserTypeEnum.SystemIntegrator;
              break;
            case 'Enduser':
              UserSettings.setUserType = UserTypeEnum.EndUser;
              break;
          }
          this.redirectTodashboard(UserSettings.getUserType);
        }
      });
    });
  }

  redirectTodashboard(userTypeId) {
    if (userTypeId === UserTypeEnum.Administrator) {
      this.router.navigate(['admin/admin-root']);
    } else if (userTypeId === UserTypeEnum.EndUser) {
      this.router.navigate(['enduser/enduser-root']);
    } else if (userTypeId === UserTypeEnum.SystemIntegrator) {
      this.router.navigate(['admin/admin-root']);
    }
    this.loginBtn = false;
  }

  configDevice() {
    this.router.navigate(['guest/guest-root']);
  }

  handleReset() { }

  handleExpire() {
    return false;
  }

  handleLoad() { }

  handleSuccess(event) {
    console.log(event);
  }
}


