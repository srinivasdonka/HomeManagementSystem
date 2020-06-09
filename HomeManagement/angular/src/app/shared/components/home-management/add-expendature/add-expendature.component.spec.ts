import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AddExpendatureComponent } from './add-expendature.component';

describe('AddDeviceComponent', () => {
  let component: AddExpendatureComponent;
  let fixture: ComponentFixture<AddExpendatureComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AddExpendatureComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AddExpendatureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
