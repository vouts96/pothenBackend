import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ISubmission, NewSubmission } from '../submission.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISubmission for edit and NewSubmissionFormGroupInput for create.
 */
type SubmissionFormGroupInput = ISubmission | PartialWithRequiredKeyOf<NewSubmission>;

type SubmissionFormDefaults = Pick<NewSubmission, 'id' | 'previousSubmission'>;

type SubmissionFormGroupContent = {
  id: FormControl<ISubmission['id'] | NewSubmission['id']>;
  afm: FormControl<ISubmission['afm']>;
  adt: FormControl<ISubmission['adt']>;
  lastName: FormControl<ISubmission['lastName']>;
  firstName: FormControl<ISubmission['firstName']>;
  fatherName: FormControl<ISubmission['fatherName']>;
  acquisitionDate: FormControl<ISubmission['acquisitionDate']>;
  lossDate: FormControl<ISubmission['lossDate']>;
  organizationUnit: FormControl<ISubmission['organizationUnit']>;
  newOrganizationUnit: FormControl<ISubmission['newOrganizationUnit']>;
  protocolNumber: FormControl<ISubmission['protocolNumber']>;
  decisionDate: FormControl<ISubmission['decisionDate']>;
  previousSubmission: FormControl<ISubmission['previousSubmission']>;
  position: FormControl<ISubmission['position']>;
  grade: FormControl<ISubmission['grade']>;
  committeeName: FormControl<ISubmission['committeeName']>;
  user: FormControl<ISubmission['user']>;
};

export type SubmissionFormGroup = FormGroup<SubmissionFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SubmissionFormService {
  createSubmissionFormGroup(submission: SubmissionFormGroupInput = { id: null }): SubmissionFormGroup {
    const submissionRawValue = {
      ...this.getFormDefaults(),
      ...submission,
    };
    return new FormGroup<SubmissionFormGroupContent>({
      id: new FormControl(
        { value: submissionRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      afm: new FormControl(submissionRawValue.afm, {
        validators: [Validators.required, Validators.pattern('^[0-9]{9}$')],
      }),
      adt: new FormControl(submissionRawValue.adt, {
        validators: [Validators.required],
      }),
      lastName: new FormControl(submissionRawValue.lastName, {
        validators: [Validators.required],
      }),
      firstName: new FormControl(submissionRawValue.firstName, {
        validators: [Validators.required],
      }),
      fatherName: new FormControl(submissionRawValue.fatherName, {
        validators: [Validators.required],
      }),
      acquisitionDate: new FormControl(submissionRawValue.acquisitionDate, {
        validators: [Validators.required],
      }),
      lossDate: new FormControl(submissionRawValue.lossDate),
      organizationUnit: new FormControl(submissionRawValue.organizationUnit, {
        validators: [Validators.required],
      }),
      newOrganizationUnit: new FormControl(submissionRawValue.newOrganizationUnit),
      protocolNumber: new FormControl(submissionRawValue.protocolNumber, {
        validators: [Validators.required],
      }),
      decisionDate: new FormControl(submissionRawValue.decisionDate, {
        validators: [Validators.required],
      }),
      previousSubmission: new FormControl(submissionRawValue.previousSubmission, {
        validators: [Validators.required],
      }),
      position: new FormControl(submissionRawValue.position),
      grade: new FormControl(submissionRawValue.grade),
      committeeName: new FormControl(submissionRawValue.committeeName),
      user: new FormControl(submissionRawValue.user),
    });
  }

  getSubmission(form: SubmissionFormGroup): ISubmission | NewSubmission {
    return form.getRawValue() as ISubmission | NewSubmission;
  }

  resetForm(form: SubmissionFormGroup, submission: SubmissionFormGroupInput): void {
    const submissionRawValue = { ...this.getFormDefaults(), ...submission };
    form.reset(
      {
        ...submissionRawValue,
        id: { value: submissionRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): SubmissionFormDefaults {
    return {
      id: null,
      previousSubmission: false,
    };
  }
}
