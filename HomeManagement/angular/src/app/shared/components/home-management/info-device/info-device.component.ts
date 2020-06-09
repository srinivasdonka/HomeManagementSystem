import { Component, OnInit, HostListener } from '@angular/core';
import { AppConfigService } from 'src/app/app-config.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HomeManagementService } from 'src/app/shared/services/home-management.service';
import { DeviceSettings } from 'src/app/shared/classes/device-settings';
import { HeaderService } from 'src/app/shared/services/header.service';
import { WindowService } from 'src/app/shared/services/window.service';
import * as moment from 'moment';
import { ItemStatus } from 'src/app/shared/enums/device-status.enum';

@Component({
  selector: 'homemanagement-info-device',
  templateUrl: './info-device.component.html',
  styleUrls: ['./info-device.component.scss']
})
export class InfoDeviceComponent implements OnInit {
  labels: any;
  homeExpInfoTitle = 'Info Home Expendature';

  itemId: any;
  itemList = [];
  itemInfo: any;
  mobileFilterSection = false;
  innerWidth: any;
  itemStatus: ItemStatus;

  constructor(
    private route: ActivatedRoute,
    private appConfigService: AppConfigService,
    private router: Router,
    private deviceManagementService: HomeManagementService,
    private headerService: HeaderService,
    private windowService: WindowService
  ) { }

  ngAfterViewInit() {
    const headerObject = {
      title: 'Info Home',
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
    this.innerWidth = this.windowService.windowRef.innerWidth;
    this.mobileFilterSection = (this.innerWidth < 768) ? true: false;
    this.route.params.subscribe(params => {
      this.itemId = params['itemId'];
      this.getItemByItemId();
    });
  }

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.innerWidth = this.windowService.windowRef.innerWidth;
    console.log(this.innerWidth);
    this.mobileFilterSection = (this.innerWidth < 768) ? true: false;
  }

  getItemByItemId() {
    this.deviceManagementService.getItemByItemId(this.itemId).subscribe(data => {
      this.itemInfo = data;
      this.itemInfo.registeredDate =  moment(this.itemInfo.createdDate).format("MMM DD, HH:mm, YYYY");
      this.itemInfo.lastModifiedDate =  moment(this.itemInfo.updatedDate).format("MMM DD, HH:mm, YYYY");
      this.itemInfo.firmwareLastUpdatedDate =  moment(this.itemInfo.firmwareLastUpdate).format("MMM DD, HH:mm, YYYY");
      this.itemInfo.firmwareUpdateSettingTime =  moment(this.itemInfo.firmwareUpdateSettings).format("MMM DD, HH:mm, YYYY");
    }, err => {
    });

    console.log(this.itemInfo);
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

  setStatus() {
    if (this.itemInfo.is_active === true) {
      this.itemInfo.is_active = false;
    } else {
      this.itemInfo.is_active = true;
    }
    this.deviceManagementService.updateDevice(this.itemInfo).subscribe(result => {
      console.log(result);
      this.checkUserCanEditDeviceByStatus();      
    }, error => {
      
    });
  }

  checkUserCanEditDeviceByStatus() {
    if (this.itemInfo.is_active === true) {
      return true;
    } else {
      return false;
    }
  }

  editDevice() {
    this.router.navigate(['/admin/admin-root/homemanagement/edit-device/' + this.itemId]);
  }

}

