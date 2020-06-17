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
  itemStatus:ItemStatus;
  showNetworkPassword = false;
  editItemForm: FormGroup;
  itemName: FormControl;
  networkNameSSID: FormControl;
  networkPassword: FormControl;
  networkConfirmPassword: FormControl;

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
    this.itemName = new FormControl(this.itemInfo.itemName, Validators.required);

  }

  createForm() {
    this.editItemForm = new FormGroup({
      itemName: this.itemName,
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
      //service call
      this.router.navigate(['/admin/admin-root/homemanagement/info-expendature/' + this.itemInfo.itemId]);
      const modalOptions = {
        icon: 'assets/homemanage_icons/shared/circle_tick.svg',
        bodyText: 'Your changes has been saved successfully.'
      };
      this.modalService.showSuccessModal(modalOptions);
    }
  }

}

