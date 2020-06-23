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
import { ItemListComponent } from '../shared/components/home-management/item-list/item-list.component';
import { AddExpendatureComponent } from '../shared/components/home-management/add-expendature/add-expendature.component';
import { ItemInfoComponent } from '../shared/components/home-management/item-info/item-info.component';
import { MyProfileComponent } from '../shared/components/my-profile/my-profile.component';
import { EditExpendatureComponent } from 'src/app/shared/components/home-management/edit-expendature/edit-expendature.component';
import { InfoItemComponent } from 'src/app/shared/components/home-management/info-item/info-item.component';

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
        component: ItemListComponent
      },
      {
        path: 'homemanagement/edit-expendature/:itemId',
        component: EditExpendatureComponent
      },
      {
        path: 'homemanagement/info-expendature/:itemId',
        component: InfoItemComponent
      },
      {
        path: 'homemanagement/add-expendature',
        component: AddExpendatureComponent
      },
      {
        path: 'homemanagement/item-expendature/:itemId',
        component: ItemInfoComponent
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
