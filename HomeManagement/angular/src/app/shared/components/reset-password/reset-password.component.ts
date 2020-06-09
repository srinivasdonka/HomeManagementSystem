import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';

import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { AuthenticateService } from '../../services/authenticate.service';
import { AppConfigService } from 'src/app/app-config.service';
import { RegularExp } from 'src/app/shared/classes/regex';
import { matchOtherValidator } from 'src/app/shared/classes/matchOtherValidator';
import { UsersListService } from 'src/app/shared/services/users-list.service';
import { ActivatedRoute } from '@angular/router';
import { ModalService } from 'src/app/shared/services/modal.service';

@Component({
  selector: 'homemanagement-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.scss']
})
export class ResetPasswordComponent implements OnInit {
  public config: PerfectScrollbarConfigInterface = {
    suppressScrollX: true,
    wheelPropagation: true
  };

  emailId: any;
  userInfo: any;

  isResetPasswordDone = false;

  regularExp = RegularExp;

  resetPasswordForm: FormGroup;
  password: FormControl;
  confirmpassword: FormControl;
  
  validations: any;
  result: any = null;
  labels: any;
  isWebview = false;
  showPassword = false;

  constructor(
    private formBuilder: FormBuilder,
    private route: ActivatedRoute,
    private appConfigService: AppConfigService,
    private authenticateService: AuthenticateService,
    private usersListService: UsersListService,
    private modalService: ModalService
  ) { }

  ngOnInit() {
    this.labels = this.appConfigService.getLabels;
    this.validations = this.appConfigService.getMessages;

    this.route.params.subscribe(params => {
      const userId = params['id'];
      this.loadUserInfo(userId);
    });
  }

  loadUserInfo(userId) {
    this.usersListService.getEmailToken(userId).subscribe(data => {
      this.userInfo = data['result'];
      if(this.userInfo) {
        this.emailId = this.userInfo.username;
      }
      this.isResetPasswordDone = true;
      this.createFormControls();
      this.createForm();
    }, err => {
        
    });
  }

  createFormControls() {
    this.password = new FormControl('', [
      Validators.required,
      Validators.minLength(6),
      Validators.pattern(this.regularExp.passwordPattern)
    ]);
    this.confirmpassword = new FormControl('', [
      Validators.required,
      Validators.minLength(6),
      matchOtherValidator('password'), 
      Validators.pattern(this.regularExp.passwordPattern)
    ]);
  }

  createForm() {
    this.resetPasswordForm = new FormGroup({
      password: this.password,
      confirmpassword: this.confirmpassword
    });
  }

  iAgreeChanged(event) {

  }

  onSubmit() {
    if (this.resetPasswordForm.valid) {
      
      this.userInfo.password = this.resetPasswordForm.value.password;
      this.userInfo.userId = this.userInfo.id;

      this.usersListService.updateUser(this.userInfo).subscribe(result => {
        this.isResetPasswordDone = false;
        const modalOptions = {
          icon: 'assets/homemanage_icons/shared/circle_tick.svg',
          bodyText: 'Reset Password Successfully.'
        };
        this.modalService.showSuccessModal(modalOptions);
      }, error => {
        this.isResetPasswordDone = true;
        const modalOptions = {
          icon: 'assets/homemanage_icons/shared/circle_wrong.svg',
          bodyText: 'Invalid details, Please verify your details'
        };
        this.modalService.showErrorModal(modalOptions);        
      });
    }
  }
}
