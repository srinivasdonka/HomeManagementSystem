import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { LoginComponent } from './components/login/login.component';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { HttpClientModule } from '@angular/common/http';
import { AuthenticateService } from 'src/app/shared/services/authenticate.service';
import { WebService } from 'src/app/shared/services/web.service';
import { WindowRefService } from 'src/app/shared/services/window-ref.service';
import { GenericModalComponent } from './components/generic-modal/generic-modal.component';

import {
  BsModalService,
  BsModalRef,
  ModalModule,
  CarouselModule,
  TooltipModule
} from 'ngx-bootstrap';

import { ModalService } from 'src/app/shared/services/modal.service';
import { HeaderComponent } from './components/header/header.component';
import { SidebarComponent } from './components/sidebar/sidebar.component';

import {
  SidebarModule,
  InputSwitchModule,
  ButtonModule,
  CardModule,
  ScrollPanelModule,
  OverlayPanelModule,
  TreeTableModule,
  TreeNode,
  GrowlModule,
  AutoCompleteModule,
  DataTableModule,
  DropdownModule,
  EditorModule,
  FileUploadModule,
  CheckboxModule,
  AccordionModule,
  MultiSelectModule,
  CalendarModule,
  SelectButtonModule
} from 'primeng/primeng';

import { NgxCaptchaModule } from 'ngx-captcha';

import { PerfectScrollbarModule, PERFECT_SCROLLBAR_CONFIG, PerfectScrollbarConfigInterface } from 'ngx-perfect-scrollbar';
import { RegistrationComponent } from './components/registration/registration.component';
import { HttpInterceptorService } from 'src/app/shared/services/http-interceptor.service';
import { HTTP_INTERCEPTORS } from '@angular/common/http';
import { CommingSoonComponent } from './components/comming-soon/comming-soon.component';
import { SearchFilterPipe } from './pipes/search-filter.pipe';
import { SearchTextPipe } from './pipes/search-text.pipe';
import { AcceptInvitationComponent } from './components/accept-invitation/accept-invitation.component';
import { UserProfileService } from 'src/app/shared/services/user-profile.service';
import { SetterGetterService } from 'src/app/shared/services/setter-getter.service';
import { OrderByPipe } from './pipes/order-by.pipe';
import { VerifyEmailComponent } from './components/verify-email/verify-email.component';
import { SearchRoleFilterPipe } from './pipes/search-role-filter.pipe';
import { SearchStatusFilterPipe } from './pipes/search-status-filter.pipe';
import { HeaderService } from 'src/app/shared/services/header.service';
import { BackIconComponent } from './components/back-icon/back-icon.component';
import { ForgotPasswordComponent } from './components/forgot-password/forgot-password.component';
import { CheckEmailComponent } from './components/check-email/check-email.component';
import { AppConfigService } from 'src/app/app-config.service';
import { CopyrightTextComponent } from './components/copyright-text/copyright-text.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';
import { NotificationsComponent } from './components/notifications/notifications.component';
import { NotificationsHistoryComponent } from './components/notifications-history/notifications-history.component';
import { DateFormatPipe } from './pipes/date-format.pipe';
import { PageTitleComponent } from 'src/app/shared/components/page-title/page-title.component';
import { UsersListComponent } from './components/user-management/users-list/users-list.component';
import { UserInfoComponent } from './components/user-management/user-info/user-info.component';
import { AddUserComponent } from './components/user-management/add-user/add-user.component';
import { EditUserComponent } from './components/user-management/edit-user/edit-user.component';
import { DevicesListComponent } from './components/home-management/devices-list/devices-list.component';
import { DeviceInfoComponent } from './components/home-management/device-info/device-info.component';
import { CreateNetworkComponent } from './components/home-management/create-network/create-network.component';
import { MyProfileComponent } from './components/my-profile/my-profile.component';
import { NgxPaginationModule } from 'ngx-pagination';
import { OrderModule } from 'ngx-order-pipe';
import { UsersListService } from './services/users-list.service';
import { EditUsersComponent } from './components/user-management/edit-users/edit-users.component';
import { AddExpendatureComponent } from './components/home-management/add-expendature/add-expendature.component';
import { EditDeviceComponent } from './components/home-management/edit-device/edit-device.component';
import { InfoDeviceComponent } from './components/home-management/info-device/info-device.component';
import { LeftBannerImageComponent } from './components/left-banner-image/left-banner-image.component';

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true,
  wheelPropagation: true
};

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    ReactiveFormsModule,
    HttpClientModule,
    RouterModule,
    ModalModule.forRoot(),
    PerfectScrollbarModule,
    ScrollPanelModule,
    SidebarModule,
    OverlayPanelModule,
    CheckboxModule,
    InputSwitchModule,
    ButtonModule,
    CardModule,
    TreeTableModule,
    GrowlModule,
    AutoCompleteModule,
    DataTableModule,
    DropdownModule,
    EditorModule,
    FileUploadModule,
    AccordionModule,
    MultiSelectModule,
    CalendarModule,
    SelectButtonModule,
    NgxCaptchaModule,
    CarouselModule.forRoot(),
    TooltipModule.forRoot(),
    OrderModule,
    NgxPaginationModule
  ],
  declarations: [
    LoginComponent,
    GenericModalComponent,
    HeaderComponent,
    SidebarComponent,
    RegistrationComponent,
    CommingSoonComponent,
    SearchFilterPipe,
    SearchTextPipe,
    AcceptInvitationComponent,
    OrderByPipe,
    VerifyEmailComponent,
    SearchRoleFilterPipe,
    SearchStatusFilterPipe,
    BackIconComponent,
    ForgotPasswordComponent,
    CheckEmailComponent,
    CopyrightTextComponent,
    ResetPasswordComponent,
    NotificationsComponent,
    NotificationsHistoryComponent,
    DateFormatPipe,
    PageTitleComponent,
    UsersListComponent,
    UserInfoComponent,
    AddUserComponent,
    EditUserComponent,
    EditUsersComponent,
    DevicesListComponent,
    AddExpendatureComponent,
    DeviceInfoComponent,
    CreateNetworkComponent,
    MyProfileComponent,
    EditDeviceComponent,
    InfoDeviceComponent,
    LeftBannerImageComponent
  ],
  providers: [
    AuthenticateService,
    UserProfileService,
    BsModalService,
    BsModalRef,
    ModalService,
    WebService,
    WindowRefService,
    HttpInterceptorService,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: HttpInterceptorService,
      multi: true
    },
    SetterGetterService,
    HeaderService,
    {
      provide: PERFECT_SCROLLBAR_CONFIG,
      useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG
    },
    UsersListService
    // CanLoadAuthGaurdService,
    // CanActivateGuard,
    // PermissionsGuard,
    // UserPermissionsService,
    // UtilityService
  ],
  exports: [
    GenericModalComponent,
    HeaderComponent,
    SidebarComponent,
    NotificationsHistoryComponent,
    CommingSoonComponent,
    SearchFilterPipe,
    SearchRoleFilterPipe,
    SearchStatusFilterPipe,
    SearchTextPipe,
    OrderByPipe,
    DateFormatPipe,
    PageTitleComponent
  ],
  entryComponents: [
    GenericModalComponent,
    CommingSoonComponent,
    PageTitleComponent
  ]
})
export class SharedModule { }
