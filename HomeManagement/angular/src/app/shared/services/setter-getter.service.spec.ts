import { TestBed } from '@angular/core/testing';

import { SetterGetterService } from './setter-getter.service';

describe('SetterGetterService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: SetterGetterService = TestBed.get(SetterGetterService);
    expect(service).toBeTruthy();
  });
});
