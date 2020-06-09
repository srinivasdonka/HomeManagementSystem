import { Component, OnInit, Output, EventEmitter, Input, HostListener, ViewChild, NgZone } from '@angular/core';
import { PerfectScrollbarConfigInterface, PerfectScrollbarDirective } from 'ngx-perfect-scrollbar';
import { Router, ActivatedRoute } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { AppConfigService } from 'src/app/app-config.service';
import { WindowRefService } from 'src/app/shared/services/window-ref.service';
import { UserTypeEnum } from 'src/app/shared/enums/userType';
import { UserSettings } from 'src/app/shared/classes/user-settings';

@Component({
  selector: 'homemanagement-notifications',
  templateUrl: './notifications.component.html',
  styleUrls: ['./notifications.component.scss']
})
export class NotificationsComponent implements OnInit {
  @ViewChild(PerfectScrollbarDirective) perfectscroll: PerfectScrollbarDirective;

  @Input() notificationList;
  @Output() hideOnClick = new EventEmitter();

  labels: any;

  public config: PerfectScrollbarConfigInterface = {
    suppressScrollX: false
  };

  isOnScreen = false;

  constructor(
    private http: HttpClient,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    public appConfigService: AppConfigService,
    private zone: NgZone,
    protected windowRefService: WindowRefService
  ) { }

  ngOnInit() {
    this.labels = this.appConfigService.getLabels;
  }

  viewAllNotifications() {
    if (UserSettings.getUserType === UserTypeEnum.Administrator) {
      this.router.navigate(['admin/admin-root/notifications']);
    } else if (UserSettings.getUserType === UserTypeEnum.EndUser) {
      this.router.navigate(['enduser/notifications']);
    }
    this.hideOnClick.emit(true);
  }

}
