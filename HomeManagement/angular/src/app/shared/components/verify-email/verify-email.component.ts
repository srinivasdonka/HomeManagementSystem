import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, FormControl, Validators } from '@angular/forms';

import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { AuthenticateService } from '../../services/authenticate.service';
import { AppConfigService } from 'src/app/app-config.service';
import { Router } from '@angular/router';

@Component({
  selector: 'homemanagement-verify-email',
  templateUrl: './verify-email.component.html',
  styleUrls: ['./verify-email.component.scss']
})
export class VerifyEmailComponent implements OnInit {
  public config: PerfectScrollbarConfigInterface = {
    suppressScrollX: true,
    wheelPropagation: true
  };

  timeLeft: number = 60;
  interval;

  invitationForm: FormGroup;
  username: FormControl;
  email: FormControl;
  password: FormControl;
  confirmpassword: FormControl;
  companyname: FormControl;
  agree: FormControl;

  labels: any;
  validations: any;
  result: any = null;
  isWebview = false;
  showPassword = false;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private appConfigService: AppConfigService
  ) { }

  ngOnInit() {
    this.labels = this.appConfigService.getLabels;
    this.validations = this.appConfigService.getMessages;

    this.createFormControls();
    this.createForm();
    this.startTimer();
  }

  createFormControls() {
    this.username = new FormControl('');
    this.password = new FormControl('', [
      Validators.required,
      Validators.pattern('((?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,30})')
    ]);
    this.confirmpassword = new FormControl('', [
      Validators.required,
      Validators.pattern('((?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,30})')
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

  startTimer() {
    this.interval = setInterval(() => {
      if (this.timeLeft > 0) {
        this.timeLeft--;
      } else {
        this.timeLeft = 0;
        clearInterval(this.interval);
        this.router.navigate(['login']);
      }
    }, 1000)
  }

  onSubmit() {
    if (this.invitationForm.valid) {
      console.log(this.invitationForm.value);
    }
  }
}

