import { TestBed } from '@angular/core/testing';

import { HomeManagementService } from './home-management.service';

describe('DeviceManagementService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: HomeManagementService = TestBed.get(HomeManagementService);
    expect(service).toBeTruthy();
  });
});
