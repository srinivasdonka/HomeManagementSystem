import { Component, OnInit, HostListener } from '@angular/core';
import { AppConfigService } from 'src/app/app-config.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HomeManagementService } from 'src/app/shared/services/home-management.service';
import { DeviceSettings } from 'src/app/shared/classes/device-settings';
import {ItemStatus } from 'src/app/shared/enums/device-status.enum';
import { HeaderService } from 'src/app/shared/services/header.service';
import { Location } from '@angular/common';
import { WindowService } from 'src/app/shared/services/window.service';
import * as moment from 'moment';
import { FormArray, FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { RegularExp } from 'src/app/shared/classes/regex';
import { ModalService } from 'src/app/shared/services/modal.service';
import { matchOtherValidator } from 'src/app/shared/classes/matchOtherValidator';

@Component({
  selector: 'homemanagement-edit-expendature/',
  templateUrl: './edit-device.component.html',
  styleUrls: ['./edit-device.component.scss']
})
export class EditDeviceComponent implements OnInit {
  labels: any;
  validations: any;
  itemInfoTitle = 'Edit Item';

  itemId: any;
  itemList = [];
  itemInfo: any;
  mobileFilterSection = false;
  innerWidth: any;
  showNetworkPassword = false;
  editItemForm: FormGroup;
  itemName: FormControl;
  itemType: FormControl;
  itemPrice: FormControl;
  itemPurchaseDate: FormControl;
  itemStatus: FormControl;
  networkNameSSID: FormControl;
  networkPassword: FormControl;
  networkConfirmPassword: FormControl;
  showAlert = false;
  regularExp = RegularExp;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private appConfigService: AppConfigService,
    private router: Router,
    private homeManagementService: HomeManagementService,
    private headerService: HeaderService,
    private location: Location,
    private windowService: WindowService,
    private modalService: ModalService
  ) { }

  ngAfterViewInit() {
    const headerObject = {
      title: 'Edit Expendature',
      back: 'arrow_back',
      setactions: true,
      notification: true
    };
    this.headerService.setTitle(headerObject);
  }

  ngOnDestroy(): void {
    this.headerService.setTitle({});
  }

  
  ngOnInit() {
    this.labels = this.appConfigService.getLabels;
    this.validations = this.appConfigService.getMessages;
    this.innerWidth = this.windowService.windowRef.innerWidth;
    this.mobileFilterSection = (this.innerWidth < 768) ? true : false;
    this.route.params.subscribe(params => {
      this.itemId = params['itemId'];
      this.getItemByItemId();
    });
  }

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.innerWidth = this.windowService.windowRef.innerWidth;
    console.log(this.innerWidth);
    this.mobileFilterSection = (this.innerWidth < 768) ? true : false;
  }

  getItemByItemId() {
    this.homeManagementService.getItemByItemId(this.itemId).subscribe(data => {
      this.itemInfo = data;
      this.createFormControls();
      this.createForm();
    }, err => {
    });
  }

  createFormControls() {
    this.itemName = new FormControl(this.itemInfo.result.item_name, Validators.required);
    this.itemId = new FormControl(this.itemInfo.result.item_id, Validators.required);
    this.itemType = new FormControl(this.itemInfo.result.item_type, Validators.required);
    this.itemPrice = new FormControl(this.itemInfo.result.item_price, Validators.required);
    this.itemPurchaseDate = new FormControl(this.itemInfo.result.item_purchase_date, Validators.required);
    this.itemStatus = new FormControl(this.itemInfo.result.item_status, Validators.required);

  }

  createForm() {
    this.editItemForm = new FormGroup({
      itemName: this.itemName,
      itemId: this.itemId,
      itemType:this.itemType,
      itemPrice:this.itemPrice,
      itemPurchaseDate:this.itemPurchaseDate,
      itemStatus:this.itemStatus
    });
  }

  reconfigureDevice() {
    this.router.navigate(['/admin/admin-root/homemanagement/create-network/' + this.itemId]);
  }

  getStatusTest() {
    if (this.itemInfo && this.itemInfo.status === ItemStatus.paid) {
      return ItemStatus.paid;
    } else {
      return ItemStatus.notPaid;
    }
  }

  cancelEditUser() {
    this.location.back();
  }


  updateDevice() {
    if (this.editItemForm.valid) {
      const expObject = {
        id:this.itemInfo.result.id,
        item_name: this.editItemForm.value.itemName,
        item_id: this.editItemForm.value.itemId,
        item_type: this.editItemForm.value.itemType,
        item_price: this.editItemForm.value.itemPrice,
        item_purchase_date: this.editItemForm.value.itemPurchaseDate,
        item_status: this.editItemForm.value.itemStatus,
      };
      this.homeManagementService.updateItem(expObject).subscribe(res => {
         this.showAlert = true;
        });
      this.router.navigate(['/admin/admin-root/homemanagement/info-expendature/' + this.itemInfo.result.id]);
      const modalOptions = {
        icon: 'assets/homemanage_icons/shared/circle_tick.svg',
        bodyText: 'Your changes has been saved successfully.'
      };
      this.modalService.showSuccessModal(modalOptions);
    }
  }

}

