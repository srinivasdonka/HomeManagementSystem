import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';

import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { AuthenticateService } from '../../services/authenticate.service';
import { AppConfigService } from 'src/app/app-config.service';
import { RegularExp } from 'src/app/shared/classes/regex';
import { ActivatedRoute } from '@angular/router';
import { UsersListService } from 'src/app/shared/services/users-list.service';
import { matchOtherValidator } from 'src/app/shared/classes/matchOtherValidator';
import { ModalService } from 'src/app/shared/services/modal.service';

@Component({
  selector: 'homemanagement-accept-invitation',
  templateUrl: './accept-invitation.component.html',
  styleUrls: ['./accept-invitation.component.scss']
})
export class AcceptInvitationComponent implements OnInit {
  public config: PerfectScrollbarConfigInterface = {
    suppressScrollX: true,
    wheelPropagation: true
  };

  regularExp = RegularExp;

  isInvitationAccepted = false;

  emailId: any;
  userInfo: any;

  invitationForm: FormGroup;
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
      this.emailId = params['id'];
      this.loadUserInfo(this.emailId);
    });
  }

  loadUserInfo(emailId) {
    this.usersListService.getEmailToken(emailId).subscribe(data => {
      this.userInfo = data['result'];
      
      this.isInvitationAccepted = true;
      this.createFormControls();
      this.createForm();
    }, err => {
        
    });
  }

  createFormControls() {
    this.username = new FormControl(this.userInfo.firstName);

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
    this.invitationForm = new FormGroup({
      username: this.username,
      password: this.password,
      confirmpassword: this.confirmpassword,
      agree: this.agree
    });
  }

  iAgreeChanged(event) {

  }

  onSubmit() {
    if (this.invitationForm.valid) {
      this.userInfo.firstName = this.invitationForm.value.username;
      this.userInfo.password = this.invitationForm.value.password;
      this.userInfo.userId = this.userInfo.id;
      this.userInfo.status = "Active";
      
      this.usersListService.updateUser(this.userInfo).subscribe(result => {
        this.isInvitationAccepted = false;
        const modalOptions = {
          icon: 'assets/homemanage_icons/shared/circle_tick.svg',
          bodyText: 'Registration Successfully.'
        };
        this.modalService.showSuccessModal(modalOptions);
      }, error => {
        this.isInvitationAccepted = true;
        const modalOptions = {
          icon: 'assets/homemanage_icons/shared/circle_wrong.svg',
          bodyText: 'Invalid details, Please verify your details'
        };
        this.modalService.showErrorModal(modalOptions);        
      });
    }
  }
}
