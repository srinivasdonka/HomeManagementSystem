import { Component, OnInit, HostListener } from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { FormArray, FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';

import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { SelectItem } from 'primeng/api';
import { matchOtherValidator } from 'src/app/shared/classes/matchOtherValidator';

import { UsersListService } from 'src/app/shared/services/users-list.service';
import { UserSettings } from 'src/app/shared/classes/user-settings';
import { AuthenticateService } from 'src/app/shared/services/authenticate.service';
import { ModalService } from 'src/app/shared/services/modal.service';
import { AdminRoles } from 'src/app/admin/enums/admin-roles.enum';
import { HeaderService } from 'src/app/shared/services/header.service';
import { WindowService } from 'src/app/shared/services/window.service';
import { AppConfigService } from 'src/app/app-config.service';
import { RegularExp } from 'src/app/shared/classes/regex';
import { HomeManagementService } from 'src/app/shared/services/home-management.service';

@Component({
  selector: 'homemanagement-create-network',
  templateUrl: './create-network.component.html',
  styleUrls: ['./create-network.component.scss']
})
export class CreateNetworkComponent implements OnInit {
  labels: any;
  validations: any;

  public config: PerfectScrollbarConfigInterface = {
    suppressScrollX: true
  };
  mobileFilterSection = false;
  mobileFilter = false;
  innerWidth: any;

  showAlert = false;

  regularExp = RegularExp;

  userInfoTitle: any;

  currentPage = 1;
  hardwareId: any;
  devicesList = [];
  deviceInfo: any;
  countriesList: any;
  showPassword = false;
  showNetworkPassword = false;

  createNetworkForm: FormGroup;
  createNetworkLicenseForm: FormGroup;
  accountUserName: FormControl;
  accountPassword: FormControl;
  accountConfirmPassword: FormControl;
  networkNameSSID: FormControl;
  networkPassword: FormControl;
  networkConfirmPassword: FormControl;
  networkCountry: FormControl;
  allowExternalAccessToSRM: FormControl;
  operationModeWirelessRouter: FormControl;
  operationModeWirelessRouterAP: FormControl;
  followAgreement: FormControl;
  
  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private route: ActivatedRoute,
    private usersListService: UsersListService,
    private modalService: ModalService,
    private authenticateService: AuthenticateService,
    private headerService: HeaderService,
    private windowService: WindowService,
    private appConfigService: AppConfigService,
    private homeManagementService: HomeManagementService
  ) { }

  ngOnInit() {
    this.innerWidth = this.windowService.windowRef.innerWidth;
    this.mobileFilterSection = (this.innerWidth < 768) ? true : false;

    this.labels = this.appConfigService.getLabels;
    this.validations = this.appConfigService.getMessages;

    this.currentPageTitle();
    this.createFormControls();
    this.createForm();
    this.getCountriesList();
    this.route.params.subscribe(params => {
      this.hardwareId = params['hardwareId'];
    });
  }

  ngAfterViewInit() {
    const headerObject = {
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

  createFormControls() {
    this.accountUserName = new FormControl('', Validators.required);
    this.accountPassword = new FormControl('', [
      Validators.required,
      Validators.pattern(this.regularExp.passwordPattern)
    ]);
    this.accountConfirmPassword = new FormControl('', [
      Validators.required,
      matchOtherValidator('accountPassword'),
      Validators.pattern(this.regularExp.passwordPattern)
    ]);
    this.networkNameSSID = new FormControl('', Validators.required);
    this.networkPassword = new FormControl('', [
      Validators.required,
      Validators.pattern(this.regularExp.passwordPattern)
    ]);
    this.networkConfirmPassword = new FormControl('', [
      Validators.required,
      matchOtherValidator('networkPassword'),
      Validators.pattern(this.regularExp.passwordPattern)
    ]);
    this.networkCountry = new FormControl('', Validators.required)
    this.allowExternalAccessToSRM = new FormControl(false);
    this.operationModeWirelessRouter = new FormControl(true);
    this.operationModeWirelessRouterAP = new FormControl(false);
    this.followAgreement = new FormControl('', Validators.required);
  }

  createForm() {
    this.createNetworkForm = new FormGroup({
      accountUserName: this.accountUserName,
      accountPassword: this.accountPassword,
      accountConfirmPassword: this.accountConfirmPassword,
      networkNameSSID: this.networkNameSSID,
      networkPassword: this.networkPassword,
      networkConfirmPassword: this.networkConfirmPassword,
      networkCountry: this.networkCountry,
      allowExternalAccessToSRM: this.allowExternalAccessToSRM,
      operationModeWirelessRouter: this.operationModeWirelessRouter,
      operationModeWirelessRouterAP: this.operationModeWirelessRouterAP

    });
    this.createNetworkLicenseForm = new FormGroup({
      followAgreement: this.followAgreement
    });
  }

  getCountriesList(){
    this.homeManagementService.getCountries().subscribe(data => {
      this.countriesList = data.countriesList;
    }, err => {
    });
  }

  next() {
    this.currentPage = ++this.currentPage;
    this.currentPageTitle();
    if (this.currentPage === 3) {
      this.addDevice();
    }
  }

  goBack() {
    this.currentPage = --this.currentPage;
    this.currentPageTitle();
  }

  currentPageTitle() {
    // this.currentPage = this.currentPage >= 1 ? this.currentPage + 1 : this.currentPage - 1;
    switch (this.currentPage) {
      case 1: this.userInfoTitle = 'Create new network'; break;
      case 2: this.userInfoTitle = 'License Agreement'; break;
      case 3: this.userInfoTitle = 'Applying settings'; break;
      case 4: this.userInfoTitle = 'Device Configuration successfully'; break;
    }
  }

  addDevice() {
    if (this.createNetworkForm.valid && this.createNetworkLicenseForm.valid) {
      const userProfile = UserSettings.getUserProfile;
      const networkInfo = {
        "network_id": '',
        'admin_username': this.createNetworkForm.value.accountUserName,
        'admin_password': this.createNetworkForm.value.accountPassword,
        'admin_confirmpassword': this.createNetworkForm.value.accountConfirmPassword,
        'network_username': this.createNetworkForm.value.networkNameSSID,
        'network_password': this.createNetworkForm.value.networkPassword,
        'network_confirmpassword': this.createNetworkForm.value.networkConfirmPassword,
        'country': this.createNetworkForm.value.networkCountry,
        'access_srm': this.createNetworkForm.value.allowExternalAccessToSRM,
        'wireless_router': this.createNetworkForm.value.operationModeWirelessRouter,
        'wireless_router_ap': this.createNetworkForm.value.operationModeWirelessRouterAP,
        'privacy_policy': this.createNetworkLicenseForm.value.followAgreement,
        'user_id': userProfile.id
      };

      this.homeManagementService.addDeviceNetwork(networkInfo).subscribe(data => {
        if(data["status"] == "success") {
          const deviceInfo = UserSettings.getDeviceInfo;
          const deviceInfoUpdated = {
            "device_id": deviceInfo.device_mac_id,
            "device_name": "Router",
            "device_feed_type": "JSON",
            "is_active": deviceInfo.is_active,
            "company_id": "123456",
            "device_type": deviceInfo.device_type,
            "hardware_key": deviceInfo.hardware_key,
            "hardware_version": deviceInfo.hardware_version,
            "model_name": deviceInfo.model_name,
            "serial_number": deviceInfo.serial_number,
            "device_parent_id": null,
            "user_id": userProfile.id,
            "network_id": data.result
          }
          this.homeManagementService.addDeviceToCompany(deviceInfoUpdated).subscribe(data => {
            if(data["status"] == "success") {
              this.currentPage = 4;
              this.currentPageTitle();
            }            
          }, err => {

          });
        }
        
      }, err => {
      });
    }
  }

  addMoreDevice() {
    this.router.navigate(['/admin/admin-root/homemanagement/add-expendature']);
  }

  done() {
    this.router.navigate(['/admin/admin-root/homemanagement']);
  }
}



