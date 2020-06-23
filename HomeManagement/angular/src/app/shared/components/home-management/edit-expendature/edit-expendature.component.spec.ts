import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { EditExpendatureComponent } from './edit-expendature.component';

describe('EditExpendatureComponent', () => {
  let component: EditExpendatureComponent;
  let fixture: ComponentFixture<EditExpendatureComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ EditExpendatureComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(EditExpendatureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
