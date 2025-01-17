import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../committee.test-samples';

import { CommitteeFormService } from './committee-form.service';

describe('Committee Form Service', () => {
  let service: CommitteeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommitteeFormService);
  });

  describe('Service methods', () => {
    describe('createCommitteeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createCommitteeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });

      it('passing ICommittee should create a new form with FormGroup', () => {
        const formGroup = service.createCommitteeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
          }),
        );
      });
    });

    describe('getCommittee', () => {
      it('should return NewCommittee for default Committee initial value', () => {
        const formGroup = service.createCommitteeFormGroup(sampleWithNewData);

        const committee = service.getCommittee(formGroup) as any;

        expect(committee).toMatchObject(sampleWithNewData);
      });

      it('should return NewCommittee for empty Committee initial value', () => {
        const formGroup = service.createCommitteeFormGroup();

        const committee = service.getCommittee(formGroup) as any;

        expect(committee).toMatchObject({});
      });

      it('should return ICommittee', () => {
        const formGroup = service.createCommitteeFormGroup(sampleWithRequiredData);

        const committee = service.getCommittee(formGroup) as any;

        expect(committee).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ICommittee should not enable id FormControl', () => {
        const formGroup = service.createCommitteeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewCommittee should disable id FormControl', () => {
        const formGroup = service.createCommitteeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
