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
import { HomeManagementService } from 'src/app/shared/services/home-management.service';

@Component({
  selector: 'homemanagement-add-expendature',
  templateUrl: './add-expendature.component.html',
  styleUrls: ['./add-expendature.component.scss']
})
export class AddExpendatureComponent implements OnInit {
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

  userInfoTitle = 'Add Expendature';

  addExpForm: FormGroup;
  itemName: FormControl;
  itemId: FormControl; 
  itemType: FormControl;
  itemPrice: FormControl; 
  itemPurchaseDate: FormControl;
  itemStatus: FormControl; 
  
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

    this.createFormControls();
    this.createForm();
  }

  ngAfterViewInit() {
    const headerObject = {
      title: 'Add Expendature',
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
    this.itemName = new FormControl('', Validators.required);
    this.itemId = new FormControl('', Validators.required);
    this.itemType = new FormControl('', Validators.required);
    this.itemPrice = new FormControl('', Validators.required);
    this.itemPurchaseDate = new FormControl('', Validators.required);
    this.itemStatus = new FormControl('', Validators.required);
  }

  createForm() {
    this.addExpForm = new FormGroup({
      itemName: this.itemName,
      itemId: this.itemId,
      itemType: this.itemType,
      itemPrice: this.itemPrice,
      itemPurchaseDate: this.itemPurchaseDate,
      itemStatus: this.itemStatus
    });
  }

  addDevice() {
    if (this.addExpForm.valid) {
      const itemName = this.addExpForm.value.itemName;
      const itemId = this.addExpForm.value.itemId;
      const itemType = this.addExpForm.value.itemType;
      const itemPrice = this.addExpForm.value.itemPrice;
      const itemPurchaseDate = this.addExpForm.value.itemPurchaseDate;
      const itemStatus = this.addExpForm.value.itemStatus;
      this.router.navigate(['/admin/admin-root/homemanagement/device-info/', itemName, itemId,itemType,itemPrice,itemPurchaseDate,itemStatus]);
    }
  }

  addExpendature(){
    const expList = [];
    const userProfile = UserSettings.getUserProfile;
    const expObject = {
      item_name: this.addExpForm.value.itemName,
      item_id: this.addExpForm.value.itemId,
      item_type: this.addExpForm.value.itemType,
      item_price: this.addExpForm.value.itemPrice,
      item_purchase_date: this.addExpForm.value.itemPurchaseDate,
      item_status: this.addExpForm.value.itemStatus,
      user_id: userProfile.id
    };
    expList.push(expObject);
    this.homeManagementService.addExpendture(expObject).subscribe(res => {
       this.showAlert = true;
       const modalOptions = {
        icon: 'assets/homemanage_icons/shared/circle_tick.svg',
        bodyText: 'Home Expendature successfully loaded'
      };
      this.modalService.showSuccessModal(modalOptions);
      }, error => {
        this.showAlert = false;
        const modalOptions = {
          icon: 'assets/homemanage_icons/shared/circle_wrong.svg',
          bodyText: 'Invalid User details, Please verify your details'
        };
        this.modalService.showErrorModal(modalOptions);
      });
  }

}



