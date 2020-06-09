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
  selector: 'homemanagement-edit-device',
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
      title: 'Edit Device',
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
      this.itemInfo.registeredDate = moment(this.itemInfo.createdDate).format("MMM DD, HH:mm, YYYY");
      this.itemInfo.lastModifiedDate = moment(this.itemInfo.updatedDate).format("MMM DD, HH:mm, YYYY");
      this.itemInfo.firmwareLastUpdatedDate = moment(this.itemInfo.firmwareLastUpdate).format("MMM DD, HH:mm, YYYY");
      this.itemInfo.firmwareUpdateSettingTime = moment(this.itemInfo.firmwareUpdateSettings).format("MMM DD, HH:mm, YYYY");
      this.createFormControls();
      this.createForm();
    }, err => {
    });
  }

  createFormControls() {
    this.itemName = new FormControl(this.itemInfo.itemName, Validators.required);
    this.networkNameSSID = new FormControl(this.itemInfo.networkName, Validators.required);
    this.networkPassword = new FormControl('', [
      Validators.pattern(this.regularExp.passwordPattern)
    ]);
    this.networkConfirmPassword = new FormControl('', [
      matchOtherValidator('networkPassword'),
      Validators.pattern(this.regularExp.passwordPattern)
    ]);

  }

  createForm() {
    this.editItemForm = new FormGroup({
      deviceName: this.itemName,
      networkNameSSID: this.networkNameSSID,
      networkPassword: this.networkPassword,
      networkConfirmPassword: this.networkConfirmPassword,
    });
  }

  reconfigureDevice() {
    this.router.navigate(['/admin/admin-root/devicemanagement/create-network/' + this.itemId]);
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

  removeDevice(childDevice) {
    if (this.itemInfo.childDevices.find(row => row.id == childDevice.id)) {
      this.itemInfo.childDevices.splice(this.itemInfo.childDevices.indexOf(this.itemInfo.childDevices.find(row => row.id == childDevice.id)), 1);
    }
  }

  updateDevice() {
    if (this.editItemForm.valid) {
      //service call
      this.router.navigate(['/admin/admin-root/devicemanagement/info-device/' + this.itemInfo.itemId]);
      const modalOptions = {
        icon: 'assets/homemanage_icons/shared/circle_tick.svg',
        bodyText: 'Your changes has been saved successfully.'
      };
      this.modalService.showSuccessModal(modalOptions);
    }
  }

}

