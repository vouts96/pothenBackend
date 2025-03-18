import { TestBed } from '@angular/core/testing';

import { sampleWithNewData, sampleWithRequiredData } from '../submission-audit.test-samples';

import { SubmissionAuditFormService } from './submission-audit-form.service';

describe('SubmissionAudit Form Service', () => {
  let service: SubmissionAuditFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubmissionAuditFormService);
  });

  describe('Service methods', () => {
    describe('createSubmissionAuditFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSubmissionAuditFormGroup();

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
            modifiedDate: expect.any(Object),
            modifiedBy: expect.any(Object),
            changeType: expect.any(Object),
            originalSubmission: expect.any(Object),
          }),
        );
      });

      it('passing ISubmissionAudit should create a new form with FormGroup', () => {
        const formGroup = service.createSubmissionAuditFormGroup(sampleWithRequiredData);

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
            modifiedDate: expect.any(Object),
            modifiedBy: expect.any(Object),
            changeType: expect.any(Object),
            originalSubmission: expect.any(Object),
          }),
        );
      });
    });

    describe('getSubmissionAudit', () => {
      it('should return NewSubmissionAudit for default SubmissionAudit initial value', () => {
        const formGroup = service.createSubmissionAuditFormGroup(sampleWithNewData);

        const submissionAudit = service.getSubmissionAudit(formGroup) as any;

        expect(submissionAudit).toMatchObject(sampleWithNewData);
      });

      it('should return NewSubmissionAudit for empty SubmissionAudit initial value', () => {
        const formGroup = service.createSubmissionAuditFormGroup();

        const submissionAudit = service.getSubmissionAudit(formGroup) as any;

        expect(submissionAudit).toMatchObject({});
      });

      it('should return ISubmissionAudit', () => {
        const formGroup = service.createSubmissionAuditFormGroup(sampleWithRequiredData);

        const submissionAudit = service.getSubmissionAudit(formGroup) as any;

        expect(submissionAudit).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISubmissionAudit should not enable id FormControl', () => {
        const formGroup = service.createSubmissionAuditFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSubmissionAudit should disable id FormControl', () => {
        const formGroup = service.createSubmissionAuditFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
