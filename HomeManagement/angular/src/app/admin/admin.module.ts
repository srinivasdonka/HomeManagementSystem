import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AdminRoutingModule } from './admin.routing';
import { SharedModule } from '../shared/shared.module';

import { 
  ModalModule,
  TooltipModule
} from 'ngx-bootstrap';

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

import {
  PerfectScrollbarModule,
  PERFECT_SCROLLBAR_CONFIG,
  PerfectScrollbarConfigInterface
} from 'ngx-perfect-scrollbar';

import { NgxPaginationModule } from 'ngx-pagination';
import { OrderModule } from 'ngx-order-pipe';

import { DashboardComponent } from './components/dashboard/dashboard.component';
import { AdminRootComponent } from './components/admin-root/admin-root.component';

import { UsersListService } from 'src/app/shared/services/users-list.service';

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true,
  wheelPropagation: true
};

@NgModule({
  imports: [
    CommonModule,
    SharedModule,
    RouterModule,
    AdminRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    ModalModule.forRoot(),
    SidebarModule,
    CardModule,
    InputSwitchModule,
    ButtonModule,
    ScrollPanelModule,
    OverlayPanelModule,
    TreeTableModule,
    GrowlModule,
    AutoCompleteModule,
    SelectButtonModule,
    DataTableModule,
    PerfectScrollbarModule,
    DropdownModule,
    EditorModule,
    FileUploadModule,
    CheckboxModule,
    AccordionModule,
    MultiSelectModule,
    CalendarModule,
    CheckboxModule,
    OrderModule,
    NgxPaginationModule,
    TooltipModule.forRoot()
  ],
  declarations: [
    DashboardComponent,
    AdminRootComponent
  ],
  providers: [
    {
      provide: PERFECT_SCROLLBAR_CONFIG,
      useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG
    },
    UsersListService
  ],
  entryComponents: [
    
  ]
})
export class AdminModule { }
