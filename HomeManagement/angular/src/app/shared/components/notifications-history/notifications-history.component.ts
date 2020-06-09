import { Component, OnInit, AfterViewInit, OnDestroy } from '@angular/core';
import { AppConfigService } from 'src/app/app-config.service';
import { HeaderService } from 'src/app/shared/services/header.service';

@Component({
  selector: 'homemanagement-notifications-history',
  templateUrl: './notifications-history.component.html',
  styleUrls: ['./notifications-history.component.scss']
})
export class NotificationsHistoryComponent implements OnInit, AfterViewInit, OnDestroy {
  labels: any;

  notificationList = [
    {date: 'Dec 27, 11:45, 2018', description: 'Harry jack Added a new device in Network_abc...', isToday: true},
    {date: 'Dec 27, 03:45, 2018', description: 'Harry jack Added a new device in Network_abc Harry jack Added a new device in Network_abc Harry jack Added in Network_abc'},
    {date: 'Nov 08, 03:45, 2018', description: 'Harry jack Added a new device in Network_abc...', isToday: true},
    {date: 'Nov 08, 03:45, 2018', description: 'You got permission to access Data view'},
    {date: 'Nov 08, 03:45, 2018', description: 'Harry jack Added a new device in Network_abc...'},
    {date: 'Nov 08, 03:45, 2018', description: 'You got permission to access Data view'},
    {date: 'Nov 08, 03:45, 2018', description: 'Harry jack Added a new device in Network_abc...'},
    {date: 'Nov 08, 03:45, 2018', description: 'You got permission to access Data view'},
    {date: 'Nov 08, 03:45, 2018', description: 'Harry jack Added a new device in Network_abc...'},
    {date: 'Nov 08, 03:45, 2018', description: 'You got permission to access Data view'}
  ];

  constructor(
    private appConfigService: AppConfigService,
    private headerService: HeaderService    
  ) { }

  ngOnInit() {
    this.labels = this.appConfigService.getLabels;
  }

  ngAfterViewInit() {
    const headerObject = {
      title: 'Notifications',
      back: 'arrow_back',
      setactions: true,
      notification: true
    };
    this.headerService.setTitle(headerObject);
  }

  ngOnDestroy(): void {
    this.headerService.setTitle({});
  }

}
