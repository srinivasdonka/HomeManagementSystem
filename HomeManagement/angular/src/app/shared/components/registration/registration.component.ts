import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { UserTypeEnum } from '../../enums/userType';
import { UserSettings } from '../../classes/user-settings';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';

import { ModalService } from '../../services/modal.service';

import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { AuthenticateService } from '../../services/authenticate.service';
import { AppConfigService } from 'src/app/app-config.service';
import { RegularExp } from 'src/app/shared/classes/regex';
import { matchOtherValidator } from 'src/app/shared/classes/matchOtherValidator';
import { UsersListService } from 'src/app/shared/services/users-list.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'homemanagement-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {
  public config: PerfectScrollbarConfigInterface = {
    suppressScrollX: true,
    wheelPropagation: true
  };

  regularExp = RegularExp;

  template: any;
  templateReplaceItems = [];

  registerForm: FormGroup;
  username: FormControl;
  email: FormControl;
  password: FormControl;
  confirmpassword: FormControl;
  companyname: FormControl;
  agree: FormControl;

  validations: any;
  result: any = null;
  labels: any;
  isWebview = false;
  showPassword = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authenticateService: AuthenticateService,
    private formBuilder: FormBuilder,
    private modalService: ModalService,
    private appConfigService: AppConfigService,
    private usersListService: UsersListService
  ) { }

  ngOnInit() {
    this.labels = this.appConfigService.getLabels;
    this.validations = this.appConfigService.getMessages;

    this.readTextFile( environment.apiUrl + "assets/confirm_registration_eMail_Template.html");

    this.createFormControls();
    this.createForm();
  }

  createFormControls() {
    this.username = new FormControl('');
    this.email = new FormControl('', [ Validators.required, Validators.email, Validators.pattern(this.regularExp.emailPattern) ]);
    this.companyname = new FormControl('', Validators.required);
    this.password = new FormControl('', [
      Validators.required,
      Validators.pattern(this.regularExp.passwordPattern)
    ]);
    this.confirmpassword = new FormControl('', [
      Validators.required,
      matchOtherValidator('password'), 
      Validators.pattern(this.regularExp.passwordPattern)
    ]);
    this.agree = new FormControl('', Validators.required);
  }

  createForm() {
    this.registerForm = new FormGroup({
      username: this.username,
      email: this.email,
      companyname: this.companyname,
      password: this.password,
      confirmpassword: this.confirmpassword,
      agree: this.agree
    });
  }

  register() {
    if (this.registerForm.valid) {
      const registerModel = {
        firstName: this.registerForm.value.username,
        lastName: '',
        email: this.registerForm.value.email,
        username: this.registerForm.value.email,
        password: this.registerForm.value.password,
        designation: 'Administrator',
        phone: '',
        companyName: this.registerForm.value.companyName,
        companyId: '',
        roleId: '',
        companyAddress: '',
        enabled: true,
        noOfUsers: 100,
        isActive: 1,
        isSI: '1',
      };

      this.authenticateService.register(registerModel)
        .subscribe(result => {
          const modalOptions = {
            icon: 'assets/homemanage_icons/shared/circle_tick.svg',
            bodyText: 'Registration Successfully.'
          };
          this.modalService.showSuccessModal(modalOptions);

          const emailObj = {
            templateType: 'registration',
            template: this.template,
            fromUser: '',
            toUser: registerModel.email,
            userType: registerModel.designation,
            templateReplaceItems: this.templateReplaceItems,
            email_url: environment.apiUrl +  '#/login/' + result['result'].id,
            email_subject: "HomeManagement Self Registration"
          };
          this.sendEmailToUser(emailObj);

        }, error => {
          const modalOptions = {
            icon: 'assets/homemanage_icons/shared/circle_wrong.svg',
            bodyText: 'Invalid User details, Please verify your details'
          };
          this.modalService.showErrorModal(modalOptions);
        });
    } else {
      const modalOptions = {
        icon: 'assets/homemanage_icons/shared/circle_wrong.svg',
        bodyText: 'Invalid User details, Please verify your details'
      };
      this.modalService.showErrorModal(modalOptions);
    }
  }

  sendEmailToUser(emailObj) {
    this.usersListService.sentEmail(emailObj).subscribe(emailResult => {
      console.log("Email Sent");
      const modalOptions = {
        icon: 'assets/homemanage_icons/shared/circle_tick.svg',
        bodyText: 'Registration Successfull and Email Sent Successfully to ' +  emailObj.toUser + '.'
      };
      this.modalService.showSuccessModal(modalOptions);

      this.authenticateService.logout();
      this.router.navigate(['verifyemail']);
    }, error => {
      const modalOptions = {
        icon: 'assets/homemanage_icons/shared/circle_wrong.svg',
        bodyText: 'Email Not Sent'
      };
      this.modalService.showErrorModal(modalOptions);
    });
  }

  reset() {
    this.registerForm.reset();
  }

  configDevice() {
    this.router.navigate(['guest/guest-root']);
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

