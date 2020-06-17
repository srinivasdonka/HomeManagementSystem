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
  selector: 'homemanagement-info-expendature',
  templateUrl: './info-device.component.html',
  styleUrls: ['./info-device.component.scss']
})
export class 
InfoDeviceComponent implements OnInit {
  labels: any;
  homeExpInfoTitle = 'Info Home Expendature';

  id: any;
  itemList = [];
  itemInfo: any;
  mobileFilterSection = false;
  innerWidth: any;
  itemStatus: ItemStatus;

  constructor(
    private route: ActivatedRoute,
    private appConfigService: AppConfigService,
    private router: Router,
    private homeManagementService: HomeManagementService,
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
      this.id = params['itemId'];
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
    this.homeManagementService.getItemByItemId(this.id).subscribe(data => {
      this.itemInfo = data;
      this.itemInfo.item_id =  this.itemInfo.result.item_id;
      this.itemInfo.item_name  = this.itemInfo.result.item_name;
      this.itemInfo.item_type  =  this.itemInfo.result.item_type;
      this.itemInfo.item_price  =  this.itemInfo.result.item_price;
      this.itemInfo.item_purchase_date  =  moment(this.itemInfo.result.item_purchase_date).format("MMM DD, HH:mm, YYYY");
      this.itemInfo.item_status  =  this.itemInfo.result.item_status;
    });

    console.log(this.itemInfo);
  }

  reconfigureDevice() {
    this.router.navigate(['/admin/admin-root/homemanagement/create-network/' + this.id]);
  }

  getStatusTest() {
    if (this.itemInfo && this.itemInfo.status === ItemStatus.paid) {
      return ItemStatus.paid;
    } else {
      return ItemStatus.notPaid;
    }
  }

  setStatus() {
    if (this.itemInfo.paid === true) {
      this.itemInfo.paid = false;
    } else {
      this.itemInfo.paid = true;
    }
    this.homeManagementService.updateDevice(this.itemInfo).subscribe(result => {
      console.log(result);
      this.checkUserCanEditDeviceByStatus();      
    }, error => {
      
    });
  }

  checkUserCanEditDeviceByStatus() {
    if (this.itemInfo.paid === true) {
      return true;
    } else {
      return false;
    }
  }

  editDevice() {
    this.router.navigate(['/admin/homemanagement/edit-expendature/' + this.id]);
  }

}

