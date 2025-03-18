import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ISubmissionAudit, NewSubmissionAudit } from '../submission-audit.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISubmissionAudit for edit and NewSubmissionAuditFormGroupInput for create.
 */
type SubmissionAuditFormGroupInput = ISubmissionAudit | PartialWithRequiredKeyOf<NewSubmissionAudit>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ISubmissionAudit | NewSubmissionAudit> = Omit<T, 'modifiedDate'> & {
  modifiedDate?: string | null;
};

type SubmissionAuditFormRawValue = FormValueOf<ISubmissionAudit>;

type NewSubmissionAuditFormRawValue = FormValueOf<NewSubmissionAudit>;

type SubmissionAuditFormDefaults = Pick<NewSubmissionAudit, 'id' | 'previousSubmission' | 'modifiedDate'>;

type SubmissionAuditFormGroupContent = {
  id: FormControl<SubmissionAuditFormRawValue['id'] | NewSubmissionAudit['id']>;
  afm: FormControl<SubmissionAuditFormRawValue['afm']>;
  adt: FormControl<SubmissionAuditFormRawValue['adt']>;
  lastName: FormControl<SubmissionAuditFormRawValue['lastName']>;
  firstName: FormControl<SubmissionAuditFormRawValue['firstName']>;
  fatherName: FormControl<SubmissionAuditFormRawValue['fatherName']>;
  acquisitionDate: FormControl<SubmissionAuditFormRawValue['acquisitionDate']>;
  lossDate: FormControl<SubmissionAuditFormRawValue['lossDate']>;
  organizationUnit: FormControl<SubmissionAuditFormRawValue['organizationUnit']>;
  newOrganizationUnit: FormControl<SubmissionAuditFormRawValue['newOrganizationUnit']>;
  protocolNumber: FormControl<SubmissionAuditFormRawValue['protocolNumber']>;
  decisionDate: FormControl<SubmissionAuditFormRawValue['decisionDate']>;
  previousSubmission: FormControl<SubmissionAuditFormRawValue['previousSubmission']>;
  modifiedDate: FormControl<SubmissionAuditFormRawValue['modifiedDate']>;
  modifiedBy: FormControl<SubmissionAuditFormRawValue['modifiedBy']>;
  changeType: FormControl<SubmissionAuditFormRawValue['changeType']>;
  originalSubmission: FormControl<SubmissionAuditFormRawValue['originalSubmission']>;
};

export type SubmissionAuditFormGroup = FormGroup<SubmissionAuditFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SubmissionAuditFormService {
  createSubmissionAuditFormGroup(submissionAudit: SubmissionAuditFormGroupInput = { id: null }): SubmissionAuditFormGroup {
    const submissionAuditRawValue = this.convertSubmissionAuditToSubmissionAuditRawValue({
      ...this.getFormDefaults(),
      ...submissionAudit,
    });
    return new FormGroup<SubmissionAuditFormGroupContent>({
      id: new FormControl(
        { value: submissionAuditRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      afm: new FormControl(submissionAuditRawValue.afm, {
        validators: [Validators.required, Validators.pattern('^[0-9]{9}$')],
      }),
      adt: new FormControl(submissionAuditRawValue.adt, {
        validators: [Validators.required],
      }),
      lastName: new FormControl(submissionAuditRawValue.lastName, {
        validators: [Validators.required],
      }),
      firstName: new FormControl(submissionAuditRawValue.firstName, {
        validators: [Validators.required],
      }),
      fatherName: new FormControl(submissionAuditRawValue.fatherName, {
        validators: [Validators.required],
      }),
      acquisitionDate: new FormControl(submissionAuditRawValue.acquisitionDate, {
        validators: [Validators.required],
      }),
      lossDate: new FormControl(submissionAuditRawValue.lossDate),
      organizationUnit: new FormControl(submissionAuditRawValue.organizationUnit, {
        validators: [Validators.required],
      }),
      newOrganizationUnit: new FormControl(submissionAuditRawValue.newOrganizationUnit),
      protocolNumber: new FormControl(submissionAuditRawValue.protocolNumber, {
        validators: [Validators.required],
      }),
      decisionDate: new FormControl(submissionAuditRawValue.decisionDate, {
        validators: [Validators.required],
      }),
      previousSubmission: new FormControl(submissionAuditRawValue.previousSubmission, {
        validators: [Validators.required],
      }),
      modifiedDate: new FormControl(submissionAuditRawValue.modifiedDate, {
        validators: [Validators.required],
      }),
      modifiedBy: new FormControl(submissionAuditRawValue.modifiedBy, {
        validators: [Validators.required],
      }),
      changeType: new FormControl(submissionAuditRawValue.changeType, {
        validators: [Validators.required],
      }),
      originalSubmission: new FormControl(submissionAuditRawValue.originalSubmission),
    });
  }

  getSubmissionAudit(form: SubmissionAuditFormGroup): ISubmissionAudit | NewSubmissionAudit {
    return this.convertSubmissionAuditRawValueToSubmissionAudit(
      form.getRawValue() as SubmissionAuditFormRawValue | NewSubmissionAuditFormRawValue,
    );
  }

  resetForm(form: SubmissionAuditFormGroup, submissionAudit: SubmissionAuditFormGroupInput): void {
    const submissionAuditRawValue = this.convertSubmissionAuditToSubmissionAuditRawValue({ ...this.getFormDefaults(), ...submissionAudit });
    form.reset(
      {
        ...submissionAuditRawValue,
        id: { value: submissionAuditRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SubmissionAuditFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      previousSubmission: false,
      modifiedDate: currentTime,
    };
  }

  private convertSubmissionAuditRawValueToSubmissionAudit(
    rawSubmissionAudit: SubmissionAuditFormRawValue | NewSubmissionAuditFormRawValue,
  ): ISubmissionAudit | NewSubmissionAudit {
    return {
      ...rawSubmissionAudit,
      modifiedDate: dayjs(rawSubmissionAudit.modifiedDate, DATE_TIME_FORMAT),
    };
  }

  private convertSubmissionAuditToSubmissionAuditRawValue(
    submissionAudit: ISubmissionAudit | (Partial<NewSubmissionAudit> & SubmissionAuditFormDefaults),
  ): SubmissionAuditFormRawValue | PartialWithRequiredKeyOf<NewSubmissionAuditFormRawValue> {
    return {
      ...submissionAudit,
      modifiedDate: submissionAudit.modifiedDate ? submissionAudit.modifiedDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
