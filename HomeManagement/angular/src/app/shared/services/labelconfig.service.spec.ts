import { TestBed } from '@angular/core/testing';

import { LabelconfigService } from './labelconfig.service';

describe('LabelconfigService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: LabelconfigService = TestBed.get(LabelconfigService);
    expect(service).toBeTruthy();
  });
});
