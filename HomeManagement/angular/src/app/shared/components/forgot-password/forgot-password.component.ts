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
import { UserProfileService } from 'src/app/shared/services/user-profile.service';
import { UsersListService } from 'src/app/shared/services/users-list.service';

@Component({
  selector: 'homemanagement-forgot-password',
  templateUrl: './forgot-password.component.html',
  styleUrls: ['./forgot-password.component.scss']
})
export class ForgotPasswordComponent implements OnInit, OnDestroy {
  validations: any;
  error = '';
  labels: any;

  public config: PerfectScrollbarConfigInterface = {
    suppressScrollX: true,
    wheelPropagation: true
  };

  loggedIn: boolean;
  regularExp = RegularExp;

  forgotPasswordForm: FormGroup;
  email: FormControl;
  recaptcha: FormControl;

  returnUrl: string;

  loginBtn = false;
  showPassword = false;
  loading = false;
  
  loginSubscription: Subscription;
  clientName = environment.clientName;
  siteKey = environment.siteKey;

  template: any;
  templateReplaceItems = [];

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private formBuilder: FormBuilder,
    private modalService: ModalService,
    private authenticateService: AuthenticateService,
    private appConfigService: AppConfigService,
    private userProfileService: UserProfileService,
    private reCaptchaV3Service: ReCaptchaV3Service,
    private usersListService: UsersListService
  ) {
  }

  ngOnInit() {
    this.labels = this.appConfigService.getLabels;
    this.validations = this.appConfigService.getMessages;

    this.readTextFile( environment.apiUrl +  "assets/reset_my_password_eMail_Template.html");
    
    this.createFormControls();
    this.createForm();
  }

  createFormControls() {
    this.email = new FormControl('', [ Validators.required, Validators.email, Validators.pattern(this.regularExp.emailPattern) ]);
    this.recaptcha = new FormControl('', Validators.required);
  }

  createForm() {
    this.forgotPasswordForm = new FormGroup({
      email: this.email,
      recaptcha: this.recaptcha
    });
  }

  ngOnDestroy(): void {
    if (this.loginSubscription) {
      this.loginSubscription.unsubscribe();
    }
  }

  sendEmail() {
    if (this.forgotPasswordForm.valid) {
      this.getUserProfile(this.forgotPasswordForm.controls.email.value);
    }
  }

  getUserProfile(userName) {
    this.userProfileService.getUserProfile(userName).subscribe( data => {
      if(data.status === 'success') {
        const emailObj = {
          templateType: 'forgotPassword',
          template: this.template,
          fromUser: '',
          toUser: data['result'].username,
          userType: data['result'].designation,
          templateReplaceItems: this.templateReplaceItems,
          email_url: environment.apiUrl +  '#/resetpassword/' + data['result'].id,
          email_subject: 'HomeManagement account password reset'
        };
        this.sendEmailToUser(emailObj);
      } else {
        const modalOptions = {
          icon: 'assets/homemanage_icons/shared/circle_wrong.svg',
          bodyText: 'Email ID is incorrect, Please re-enter email Id.'
        };
        this.modalService.showSuccessModal(modalOptions);
      }
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
      this.router.navigate(['checkemail']);
    }, error => {
      const modalOptions = {
        icon: 'assets/homemanage_icons/shared/circle_wrong.svg',
        bodyText: 'Email Not Sent'
      };
      this.modalService.showErrorModal(modalOptions);
    });
  }

  onSuccess() {

  }

  onFailure() {

  }

  redirectTodashboard(userTypeId) {
    if (userTypeId) {
      this.router.navigate(['admin/admin-root']);
    }
    this.loginBtn = false;
  }

  handleReset() {}
  
  handleExpire() {
    return false;
  }
  
  handleLoad() {

  }
  
  handleSuccess(event) {
    console.log(event);
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



