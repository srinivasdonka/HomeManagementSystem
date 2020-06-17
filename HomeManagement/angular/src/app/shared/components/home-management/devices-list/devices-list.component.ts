import { Component, OnInit, AfterViewInit, OnDestroy, ViewChild, HostListener } from '@angular/core';
import { OverlayPanelModule, OverlayPanel } from 'primeng/overlaypanel';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { Router } from '@angular/router';
import { WindowService } from 'src/app/shared/services/window.service';
import { HeaderService } from 'src/app/shared/services/header.service';
import { AppConfigService } from 'src/app/app-config.service';

import { HomeManagementService } from 'src/app/shared/services/home-management.service';
import * as moment from 'moment';
import { UserSettings } from 'src/app/shared/classes/user-settings';

@Component({
  selector: 'homemanagement-devices-list',
  templateUrl: './devices-list.component.html',
  styleUrls: ['./devices-list.component.scss']
})
export class DevicesListComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('filterSearchOP') filterSearchOP: OverlayPanel;

  labels: any;
  validations: any;

  public config: PerfectScrollbarConfigInterface = {
    suppressScrollX: true
  };

  editMultipleDevices = false;

  pageHeaderObject: any;
  innerWidth: any;

  mobileFilterSection = false;

  page = 0;
  size = 10;
  itemList = [];
  filterSearchUsersList = [];

  pager: any = {};
  isAllChecked = false;

  companyDetails: any;

  searchInput = '';
  filterInput = [];
  itemFilters = [{ value: 'Mesh Router', checked: false }, { value: 'HomeManagement Router', checked: false }];
  statusFilters = [{ value: 'paid', checked: false }, { value: 'not Paid', checked: false }];

  //sorting
  key = '';
  reverse = false;

  itemsinPage = [
    { id: '5', value: '5' }, { id: '10', value: '10' }, { id: '15', value: '15' }, { id: '25', value: '25' }, { id: '50', value: '50' }, { id: '10000000000', value: 'All' }
  ];
  currentPage = 1;
  showitemsPerPage = 5;
  selectedItemstoShow = this.showitemsPerPage;
  pageNumber = 1;

  showActivateBtn = false;
  showDeactivateBtn = false;
  showEditBtn = false;
  selectedItemList = [];
  selectedActiveItemList = [];
  selectedDeactiveItemList = [];
  noRecords = true;

  constructor(
    private appConfigService: AppConfigService,
    private homeManagementService: HomeManagementService,
    private router: Router,
    private windowService: WindowService,
    public headerService: HeaderService
  ) { }

  ngOnInit() {
    this.labels = this.appConfigService.getLabels;
    this.validations = this.appConfigService.getMessages;

    this.innerWidth = this.windowService.windowRef.innerWidth;
    this.mobileFilterSection = (this.innerWidth < 768) ? true : false;

    this.getItemList();
  }

  ngAfterViewInit() {
    const headerObject = {
      title: 'Home Management',
      menu: 'menu',
      setactions: true,
      notification: true
    };
    this.headerService.setTitle(headerObject);
    this.setPageHeaderObject();
  }

  ngOnDestroy(): void {
    this.headerService.setTitle({});
  }

  resetPaginationItems() {
    this.itemList = [];
    this.itemsinPage = [{ id: '5', value: '5' }, { id: '10', value: '10' }, { id: '15', value: '15' }, { id: '25', value: '25' }, { id: '50', value: '50' }, { id: '1000000000000000', value: 'All' }];
    this.currentPage = 1;
    this.showitemsPerPage = 5;
    this.selectedItemstoShow = this.showitemsPerPage;
    this.pageNumber = 1;
  }

  setPageHeaderObject() {
    setTimeout(() => {
      this.pageHeaderObject = {
        title: 'Home Management',
        back: 'arrow_back',
        chat: true,
        search: true
      };
      this.headerService.setPageTitle(this.pageHeaderObject);
    }, 10);
  }

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    this.innerWidth = this.windowService.windowRef.innerWidth;
    this.mobileFilterSection = (this.innerWidth < 768) ? true : false;
  }

  toggleFilterSearchPanel(event, actualTarget) {
    this.filterSearchOP.toggle(event, actualTarget);
  }

  getItemList() {
    const userProfile = UserSettings.getUserProfile;
    this.homeManagementService.getHomeExpendature(userProfile.id)
      .subscribe(data => {
        this.itemList = data.result;
        if (this.itemList && this.itemList.length > 0) {
          this.noRecords = false;
          this.itemList.forEach(item => {
            let today = new Date();
            const createDate_todayDate_diff = moment(item.item_purchase_date).diff(moment(today), 'hours');
            if (Math.abs(createDate_todayDate_diff) <= 36) {
              item.isNewDevice = true;
            }
            else {
              item.isNewDevice = false;
            }
          });
          // this.devicesList = this.devicesList.concat(DeviceSettings.getDeviceHardwareIdandKey);
          this.pager = data.result;
        } else if (this.itemList && this.itemList.length == 0) {
          this.noRecords = true;
        }

      }, err => {
      });
  }

  addUser() {
    this.router.navigate(['/admin/admin-root/homemanagement/add-expendature']);
  }

  showUserDetails(item) {
    let id = '';
    if (item.id !== undefined) {
      id = item.id;
    }
    if (item.id !== undefined) {
      id = item.id
    }
    this.router.navigate(['/admin/admin-root/homemanagement/info-expendature/' + id]);
  }

  statusChecked() {
    return this.statusFilters.filter(item => { return item.checked; });
  }

  roleChecked() {
    return this.itemFilters.filter(item => { return item.checked; });
  }

  checkAll(event) {
    this.isAllChecked = event;
    this.selectedItemList = [];
    this.selectedActiveItemList = [];
    this.selectedDeactiveItemList = [];

    if (event) {
      this.itemList.forEach((eachItem) => {
        eachItem.checked = this.isAllChecked;
        if (eachItem.is_active === true) {
          this.selectedActiveItemList.push(eachItem);
        }
        if (eachItem.is_active === false) {
          this.selectedDeactiveItemList.push(eachItem);
        }
        this.selectedItemList.push(eachItem);
      });
      this.showEditBtn = true;
    } else {
      this.itemList.forEach((eachdevice) => {
        eachdevice.checked = this.isAllChecked;
      });
      this.showEditBtn = false;
    }
    this.buttonState();
  }

  handleEnable(event, item) {
    this.mobileFilterSection = true;

    if (event && item.is_active === true) {
      this.selectedItemList.push(item);
      this.selectedActiveItemList.push(item);
      this.isAllChecked = false;
    } else if (event && item.is_active === false) {
      this.selectedItemList.push(item);
      this.selectedActiveItemList.push(item);
      this.isAllChecked = false;
    } else {
      if (this.selectedActiveItemList.find(row => row.id == item.id)) {
        this.selectedActiveItemList.splice(this.selectedActiveItemList.indexOf(this.selectedActiveItemList.find(row => row.id == item.id)), 1);
      }
      if (this.selectedDeactiveItemList.find(row => row.id == item.id)) {
        this.selectedDeactiveItemList.splice(this.selectedDeactiveItemList.indexOf(this.selectedDeactiveItemList.find(row => row.id == item.id)), 1);
      }
      if (this.selectedItemList.find(row => row.id == item.id)) {
        this.selectedItemList.splice(this.selectedItemList.indexOf(this.selectedItemList.find(row => row.id == item.id)), 1);
      }
    }
    this.buttonState();
  }

  buttonState() {
    if (this.selectedDeactiveItemList.length === 0 && this.selectedDeactiveItemList.length > 0) {
      this.showDeactivateBtn = true;
      if (this.selectedActiveItemList.length === 1) {
        this.showEditBtn = true;
      } else {
        this.showEditBtn = false;
      }
    } else {
      this.showDeactivateBtn = false;
      this.showEditBtn = false;
    }
    if (this.selectedDeactiveItemList.length === 0 && this.selectedDeactiveItemList.length > 0) {
      this.showActivateBtn = true;
    } else {
      this.showActivateBtn = false;
    }
    // if (this.selectedDeactiveDevicesList.length > 0 && this.selectedActiveDevicesList.length > 0) {
    //   this.mobileFilterSection = true;
    // } else {
    //   this.mobileFilterSection = false;
    // }
  }

  deactivateSelectedItems() {
    this.selectedActiveItemList.forEach((item, index) => {
      if (item.is_active === true) {
        item.is_active = false
      } else {
        item.is_active = true
      }
      item.is_active = item.is_active;
      this.homeManagementService.updateDevice(item).subscribe(result => {
        console.log(result);
        if (index === (this.selectedDeactiveItemList.length - 1)) {
          this.itemList.forEach((eachdevice) => {
            eachdevice.checked = false;
          });
          this.selectedActiveItemList = [];
          this.buttonState();
        }
      }, error => {

      });
    });
  }

  activateSelectedItems() {
    this.selectedDeactiveItemList.forEach((item, index) => {
      if (item.item_status === false) {
        item.item_status = true
      } else {
        item.item_status = false
      }
      item.is_active = item.is_active;
      this.homeManagementService.updateDevice(item).subscribe(result => {
        console.log(result);
        if (index === (this.selectedDeactiveItemList.length - 1)) {
          this.itemList.forEach((eachdevice) => {
            eachdevice.checked = false;
          });
          this.selectedDeactiveItemList = [];
          this.buttonState();
        }
      }, error => {

      });
    });
  }

  editSelectedUsers() {
    alert("test1");
    if (this.selectedItemList.length === 1) {
      this.router.navigate(['/admin/admin-root/homemanagement/' + this.selectedItemList[0].id]);
    } else {
      this.editMultipleDevices = !this.editMultipleDevices;
    }
  }

  filterChanged(event, status) {

  }

  sortIconToggle() {
    if (this.reverse) {
      return 'assets/homemanage_icons/shared/sort_up.svg';
    } else {
      return 'assets/homemanage_icons/shared/sort_down.svg';
    }
  }

  sort(key) {
    this.key = key;
    this.reverse = !this.reverse;
  }

  selectedItemsPerPageChanged(event): void {
    this.showitemsPerPage = event;
  }
}

