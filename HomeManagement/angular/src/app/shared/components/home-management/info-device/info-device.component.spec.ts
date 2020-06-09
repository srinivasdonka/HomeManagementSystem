import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { InfoDeviceComponent } from './info-device.component';

describe('InfoDeviceComponent', () => {
  let component: InfoDeviceComponent;
  let fixture: ComponentFixture<InfoDeviceComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ InfoDeviceComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(InfoDeviceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
