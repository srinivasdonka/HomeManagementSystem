import { Component, OnInit, AfterViewInit, OnDestroy, ViewChild, HostListener } from '@angular/core';
import { OverlayPanelModule, OverlayPanel } from 'primeng/overlaypanel';
import { PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';

import { UsersListService } from 'src/app/shared/services/users-list.service';
import { Router } from '@angular/router';

import { UserSettings } from 'src/app/shared/classes/user-settings';
import { WindowService } from 'src/app/shared/services/window.service';
import { HeaderService } from 'src/app/shared/services/header.service';
import { AppConfigService } from 'src/app/app-config.service';

@Component({
  selector: 'homemanagement-users-list',
  templateUrl: './users-list.component.html',
  styleUrls: ['./users-list.component.scss']
})
export class UsersListComponent implements OnInit, AfterViewInit, OnDestroy {
  @ViewChild('filterSearchOP') filterSearchOP: OverlayPanel;

  labels: any;
  validations: any;

  public config: PerfectScrollbarConfigInterface = {
    suppressScrollX: true
  };

  editMultipleUsers = false;

  pageHeaderObject: any;
  innerWidth: any;

  mobileFilterSection = false;
  
  page = 0;
  size = 10;
  usersList = [];
  filterSearchUsersList = [];

  pager: any = {};
  isAllChecked = false;

  companyDetails: any;

  searchInput = '';
  filterInput = [];
  roleFilters = [{ value: 'Owner', checked: false },{ value: 'Customer', checked: false },{ value: 'Administrator', checked: false }];
  statusFilters = [{ value: 'Active', checked: false }, { value: 'Deactive', checked: false }, { value: 'Activation Pending', checked: false }];

  //sorting
  key = '';
  reverse = false;

  itemsinPage = [{ id: '5', value: '5' }, { id: '10', value: '10' }, { id: '15', value: '15' }, { id: '25', value: '25' }, { id: '50', value: '50' }, { id: '1000000000000000', value: 'All' }];
  currentPage = 1;
  showitemsPerPage = 5;
  selectedItemstoShow = this.showitemsPerPage;
  pageNumber = 1;

  showActivateBtn = false;
  showDeactivateBtn = false;
  showEditBtn = false;
  selectedUsersList = [];
  selectedActiveUsersList = [];
  selectedDeactiveUsersList = [];

  constructor(
    private appConfigService: AppConfigService,
    private usersListService: UsersListService,
    private router: Router,
    private windowService: WindowService,
    public headerService: HeaderService
  ) { }

  ngOnInit() {
    this.labels = this.appConfigService.getLabels;
    this.validations = this.appConfigService.getMessages;

    this.innerWidth = this.windowService.windowRef.innerWidth;
    this.mobileFilterSection = (this.innerWidth < 768) ? true: false;

    // this.getAllUsersList();
    this.getCompanyDetails();
  }

  ngAfterViewInit() {
    const headerObject = {
      title: 'User Management',
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
    this.usersList = [];
    this.itemsinPage = [{ id: '5', value: '5' }, { id: '10', value: '10' }, { id: '15', value: '15' }, { id: '25', value: '25' }, { id: '50', value: '50' }, { id: '1000000000000000', value: 'All' }];
    this.currentPage = 1;
    this.showitemsPerPage = 5;
    this.selectedItemstoShow = this.showitemsPerPage;
    this.pageNumber = 1;
  }
  
  setPageHeaderObject() {
    setTimeout(() => {
      this.pageHeaderObject = {
        title: 'User Management',
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
    this.mobileFilterSection = (this.innerWidth < 768) ? true: false;
  }

  getCompanyDetails() {
    const userName = UserSettings.getUsername;
    this.usersListService.getCompanyDetailsByUsername(userName)
      .subscribe(data => {
        this.companyDetails = data['result'][0];
        
        this.getCompanyUsers(this.companyDetails);
        // this.getAllUsersList();
      }, err => {
      });
  }

  getCompanyUsers(companyDetails) {
    this.usersListService.getCompanyUsersById(this.companyDetails.id).subscribe(data => {
      this.usersList = data['result'];
    });
  }

  toggleFilterSearchPanel(event, actualTarget) {
    this.filterSearchOP.toggle(event, actualTarget);
  }

  getAllUsersList() {
    this.usersListService.getAllUsers(this.page, this.size)
      .subscribe(data => {
        this.usersList = data.result.content;
        this.filterSearchUsersList = this.usersList;
        this.pager = data.result;
        
        // if (this.innerWidth < 768) {
        //   this.showitemsPerPage = this.pager.totalElements;
        // }
      }, err => {
      });
  }

  addUser() {
    this.router.navigate(['/admin/admin-root/adduser/' + this.companyDetails.id]);
  }

  showUserDetails(user) {
    this.router.navigate(['/admin/admin-root/userinfo/' + user.id]);
  }

  statusChecked() {
    return this.statusFilters.filter(item => { return item.checked; });
  }

  roleChecked() {
    return this.roleFilters.filter(item => { return item.checked; });
  }

  checkAll(event) {
    this.isAllChecked = event;
    this.selectedUsersList = [];
    this.selectedActiveUsersList = [];
    this.selectedDeactiveUsersList = [];

    if (event) {
      this.usersList.forEach((eachuser) => {
        eachuser.checked = this.isAllChecked;
        if (eachuser.status === 'Active') {
          this.selectedActiveUsersList.push(eachuser);
        }
        if (eachuser.status === 'Deactive') {
          this.selectedDeactiveUsersList.push(eachuser);
        }
        this.selectedUsersList.push(eachuser);
      });
      this.showEditBtn = true;
    } else {
      this.usersList.forEach((eachuser) => {
        eachuser.checked = this.isAllChecked;
      });
      this.showEditBtn = false;
    }
    this.buttonState();
  }

  handleEnable(event, user) {
    this.mobileFilterSection = true;
    
    if (event && user.status === 'Active') {
      this.selectedUsersList.push(user);
      this.selectedActiveUsersList.push(user);     
      this.isAllChecked = false;
    } else if (event && user.status === 'Deactive') {
      this.selectedUsersList.push(user);
      this.selectedDeactiveUsersList.push(user);
      this.isAllChecked = false;
    } else {
      if (this.selectedActiveUsersList.find(row => row.id == user.id)) {
        this.selectedActiveUsersList.splice(this.selectedActiveUsersList.indexOf(this.selectedActiveUsersList.find(row => row.id == user.id)), 1);
      }
      if (this.selectedDeactiveUsersList.find(row => row.id == user.id)) {
        this.selectedDeactiveUsersList.splice(this.selectedDeactiveUsersList.indexOf(this.selectedDeactiveUsersList.find(row => row.id == user.id)), 1);
      }
      if (this.selectedUsersList.find(row => row.id == user.id)) {
        this.selectedUsersList.splice(this.selectedUsersList.indexOf(this.selectedUsersList.find(row => row.id == user.id)), 1);
      }
    }
    this.buttonState();
  }

  buttonState() {
    if (this.selectedDeactiveUsersList.length === 0 && this.selectedActiveUsersList.length > 0) {
      this.showDeactivateBtn = true;
      this.showEditBtn = true;
    } else {
      this.showDeactivateBtn = false;
      this.showEditBtn = false;
    }
    if (this.selectedActiveUsersList.length === 0 && this.selectedDeactiveUsersList.length > 0) {
      this.showActivateBtn = true;
    } else {
      this.showActivateBtn = false;
    }
    // if (this.selectedDeactiveUsersList.length > 0 && this.selectedActiveUsersList.length > 0) {
    //   this.mobileFilterSection = true;
    // } else {
    //   this.mobileFilterSection = false;
    // }
  }

  deactivateSelectedUsers() {
    this.selectedActiveUsersList.forEach( (user, index) => {
      if ( user.status === 'Active') {
        user.status = 'Deactive'
      } else {
        user.status = 'Active'
      }
      user.isActive = user.status;
      user.roleId = user.role_id;
      this.usersListService.updateUser(user).subscribe(result => {
        console.log(result);
        if (index === (this.selectedActiveUsersList.length - 1) ) {
          this.usersList.forEach((eachuser) => {
            eachuser.checked = false;
          });
          this.selectedActiveUsersList = [];
          this.buttonState();
        }
      }, error => {
        
      });
    });
  }

  activateSelectedUsers() {
    this.selectedDeactiveUsersList.forEach( (user, index) => {
      if ( user.status === 'Deactive') {
        user.status = 'Active'
      } else {
        user.status = 'Deactive'
      }
      user.isActive = user.status;
      user.roleId = user.role_id;
      this.usersListService.updateUser(user).subscribe(result => {
        console.log(result);
        if (index === (this.selectedDeactiveUsersList.length - 1) ) {
          this.usersList.forEach((eachuser) => {
            eachuser.checked = false;
          });
          this.selectedDeactiveUsersList = [];
          this.buttonState();
        }
      }, error => {
        
      });
    });
  }

  editSelectedUsers() {
    if (this.selectedUsersList.length === 1 ) {
      this.router.navigate(['/admin/admin-root/edituser/' + this.selectedUsersList[0].id]);
    } else {
      this.editMultipleUsers = !this.editMultipleUsers;
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

  getStatusColor(user) {
    if (user.status === 'Active') {
      return 'text-success';
    } else if (user.status === 'Deactive') {
      return 'text-danger';
    } else {
      return 'text-warning';
    }
  }

  pageChanged(event) {
    setTimeout(() => {
      this.pageNumber = event;
    }, 500);
  }

  usersListView(event) {
    this.editMultipleUsers = !this.editMultipleUsers;
  }
}
