import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../submission.test-samples';

import { SubmissionFormService } from './submission-form.service';

describe('Submission Form Service', () => {
  let service: SubmissionFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubmissionFormService);
  });

  describe('Service methods', () => {
    describe('createSubmissionFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSubmissionFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            afm: expect.any(Object),
            adt: expect.any(Object),
            lastName: expect.any(Object),
            firstName: expect.any(Object),
            fatherName: expect.any(Object),
            acquisitionDate: expect.any(Object),
            lossDate: expect.any(Object),
            organizationUnit: expect.any(Object),
            newOrganizationUnit: expect.any(Object),
            protocolNumber: expect.any(Object),
            decisionDate: expect.any(Object),
            previousSubmission: expect.any(Object),
            position: expect.any(Object),
            grade: expect.any(Object),
            committeeName: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });

      it('passing ISubmission should create a new form with FormGroup', () => {
        const formGroup = service.createSubmissionFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            afm: expect.any(Object),
            adt: expect.any(Object),
            lastName: expect.any(Object),
            firstName: expect.any(Object),
            fatherName: expect.any(Object),
            acquisitionDate: expect.any(Object),
            lossDate: expect.any(Object),
            organizationUnit: expect.any(Object),
            newOrganizationUnit: expect.any(Object),
            protocolNumber: expect.any(Object),
            decisionDate: expect.any(Object),
            previousSubmission: expect.any(Object),
            position: expect.any(Object),
            grade: expect.any(Object),
            committeeName: expect.any(Object),
            user: expect.any(Object),
          }),
        );
      });
    });

    describe('getSubmission', () => {
      it('should return NewSubmission for default Submission initial value', () => {
        const formGroup = service.createSubmissionFormGroup(sampleWithNewData);

        const submission = service.getSubmission(formGroup) as any;

        expect(submission).toMatchObject(sampleWithNewData);
      });

      it('should return NewSubmission for empty Submission initial value', () => {
        const formGroup = service.createSubmissionFormGroup();

        const submission = service.getSubmission(formGroup) as any;

        expect(submission).toMatchObject({});
      });

      it('should return ISubmission', () => {
        const formGroup = service.createSubmissionFormGroup(sampleWithRequiredData);

        const submission = service.getSubmission(formGroup) as any;

        expect(submission).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISubmission should not enable id FormControl', () => {
        const formGroup = service.createSubmissionFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSubmission should disable id FormControl', () => {
        const formGroup = service.createSubmissionFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
