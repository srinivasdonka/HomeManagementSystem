import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';

import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AdminRootComponent } from './components/admin-root/admin-root.component';

import { CommingSoonComponent } from 'src/app/shared/components/comming-soon/comming-soon.component';
import { NotificationsHistoryComponent } from 'src/app/shared/components/notifications-history/notifications-history.component';
import { UsersListComponent } from '../shared/components/user-management/users-list/users-list.component';
import { UserInfoComponent } from '../shared/components/user-management/user-info/user-info.component';
import { AddUserComponent } from '../shared/components/user-management/add-user/add-user.component';
import { EditUserComponent } from '../shared/components/user-management/edit-user/edit-user.component';
import { DevicesListComponent } from '../shared/components/home-management/devices-list/devices-list.component';
import { AddExpendatureComponent } from '../shared/components/home-management/add-expendature/add-expendature.component';
import { DeviceInfoComponent } from '../shared/components/home-management/device-info/device-info.component';
import { CreateNetworkComponent } from '../shared/components/home-management/create-network/create-network.component';
import { MyProfileComponent } from '../shared/components/my-profile/my-profile.component';
import { EditDeviceComponent } from 'src/app/shared/components/home-management/edit-device/edit-device.component';
import { InfoDeviceComponent } from 'src/app/shared/components/home-management/info-device/info-device.component';

export const adminRoutes: Routes = [
  { path: '', redirectTo: 'admin-root', pathMatch: 'full' },
  {
    path: 'admin-root', component: AdminRootComponent,
    children: [
      {
        path: '',
        redirectTo: 'dashboard'
      },
      {
        path: 'dashboard',
        component: DashboardComponent
      },
      {
        path: 'usermanagement',
        component: UsersListComponent
      },
      {
        path: 'userinfo/:userId',
        component: UserInfoComponent

      },
      {
        path: 'adduser/:companyId',
        component: AddUserComponent
      },
      {
        path: 'edituser/:userId',
        component: EditUserComponent
      },
      {
        path: 'homemanagement',
        component: DevicesListComponent
      },
      {
        path: 'homemanagement/edit-expendature/:itemId',
        component: EditDeviceComponent
      },
      {
        path: 'homemanagement/info-expendature/:itemId',
        component: InfoDeviceComponent
      },
      {
        path: 'homemanagement/add-expendature',
        component: AddExpendatureComponent
      },
      {
        path: 'homemanagement/item-expendature/:itemId',
        component: DeviceInfoComponent
      },
      {
        path: 'homemanagement/create-network/:hardwareId',
        component: CreateNetworkComponent
      },      
     
      {
        path: 'propertymanagement',
        component: CommingSoonComponent
      },
      
      
      {
        path: 'profile',
        component: MyProfileComponent

      },
      {
        path: 'notifications',
        component: NotificationsHistoryComponent

      }
    ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(adminRoutes)],
  exports: [RouterModule]
})
export class AdminRoutingModule { }
