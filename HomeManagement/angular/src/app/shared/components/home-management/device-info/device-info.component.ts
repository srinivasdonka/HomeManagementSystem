import { Component, OnInit } from '@angular/core';
import { AppConfigService } from 'src/app/app-config.service';
import { ActivatedRoute, Router } from '@angular/router';
import { HomeManagementService } from 'src/app/shared/services/home-management.service';
import { DeviceSettings } from 'src/app/shared/classes/device-settings';
import { HeaderService } from 'src/app/shared/services/header.service';
import { UserSettings } from 'src/app/shared/classes/user-settings';

@Component({
  selector: 'homemanagement-device-info',
  templateUrl: './device-info.component.html',
  styleUrls: ['./device-info.component.scss']
})
export class DeviceInfoComponent implements OnInit {
  labels: any;
  itemInfoTitle = 'Item Info';

  itemName: any;
  itemId: any;
  itemList = [];
  networksList = [];
  itemInfo: any;
  isExistingItem = false;
  restorePrevItemConfig = false;
  selectedItem = "";
  currentPage = 1;

  constructor(
    private route: ActivatedRoute,
    private appConfigService: AppConfigService,
    private router: Router,
    private homeManagementService: HomeManagementService,
    private headerService: HeaderService
  ) { }

  ngOnInit() {
    this.labels = this.appConfigService.getLabels;
    this.route.params.subscribe(params => {
      this.itemName = params.itemName;
      this.itemId = params.itemId;
      this.getPhoneHomeData(this.itemName, this.itemId);
    });
  }

  ngAfterViewInit() {
    const headerObject = {
      title: 'Device Info',
      back: 'arrow_back',
      setactions: true,
      notification: true
    };
    this.headerService.setTitle(headerObject);
  }

  getPhoneHomeData(device_mac_id, hardware_key) {
    this.homeManagementService.getPhoneHomeData(device_mac_id, hardware_key)
      .subscribe((data) => {
        if (data["status"] == "success") {
          this.itemInfo = data['result'];
          this.homeManagementService.checkDeviceExists(device_mac_id, hardware_key).subscribe(data => {
            if (data["status"] == "success") {
              if(data["message"]=="Device already has existed"){
                this.itemInfo.isExistingDevice = true;
                this.isExistingItem = true;
              }
              else {
                this.itemInfo.isExistingDevice = false;
                this.isExistingItem = false;
              }
            }
          }, err => {
          });
          UserSettings.setDeviceInfo = this.itemInfo;
          const d_list = data['deviceList'];
          this.itemList = [];
          d_list.forEach(ele => {
            this.itemList.push({ "label": ele.device_name, "value": ele.device_id });
          });
          this.homeManagementService.getDeviceNetworks().subscribe(data => {
            const n_list = data['result'];
            this.networksList = []
            n_list.forEach(ele => {
              this.networksList.push({ "label": ele.network_username, "value": ele.network_username });
            });
          }, err => {

          });
        }
      }, err => {
      });
  }


  selectedNetworkChanged(selectedNetwork) {
    this.currentPage = 3;
    this.itemInfoTitle = "Applying settings";
    setTimeout(() => {
      this.currentPage = 4;
      this.itemInfoTitle = "Item Configuration successfully";
    }, 2000);
  }

  selectedDeviceChanged(selectedDevice) {
    this.currentPage = 3;
    this.itemInfoTitle = "Applying settings";
    setTimeout(() => {
      this.currentPage = 4;
      this.itemInfoTitle = "Item Configuration successfully";
    }, 2000);
  }

  restorePrevConfig() {
    this.currentPage = 3;
    this.itemInfoTitle = "Applying settings";
    setTimeout(() => {
      this.currentPage = 4;
      this.itemInfoTitle = "Item Configuration successfully";
    }, 2000);
  }

  // getAllDevicesList(page, size) {
  //   this.deviceManagementService.getAllDevices(page, size).subscribe(data => {
  //     this.devicesList = data.result;
  //     this.getDeviceByHardwareId(this.hardwareId);
  //   }, err => {
  //   });
  // }

  // getDeviceByHardwareId(hardwareId) {
  //   this.isExistingDevice = false;
  //   this.deviceInfo = this.devicesList.filter(res => res.hardware_id === this.hardwareId);
  //   if (this.deviceInfo.length > 0) {
  //     this.deviceInfo = this.deviceInfo[0];
  //     this.isExistingDevice = true;
  //   } else {
  //     this.deviceInfo = DeviceSettings.getDeviceHardwareIdandKey.filter(res => res.hardware_id === this.hardwareId);
  //     this.deviceInfo = this.deviceInfo[0];
  //     this.isExistingDevice = true;
  //   }
  //   console.log(this.deviceInfo);
  // }

  
  addMoreDevice() {
    this.router.navigate(['/admin/admin-root/homemanagement/add-expendature']);
  }

  done() {
    this.router.navigate(['/admin/admin-root/homemanagement']);
  }

}
