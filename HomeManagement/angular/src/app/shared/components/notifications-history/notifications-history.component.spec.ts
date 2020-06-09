import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { NotificationsHistoryComponent } from './notifications-history.component';

describe('NotificationsHistoryComponent', () => {
  let component: NotificationsHistoryComponent;
  let fixture: ComponentFixture<NotificationsHistoryComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ NotificationsHistoryComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NotificationsHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
