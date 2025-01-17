import { Injectable } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';

import { ICommittee, NewCommittee } from '../committee.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ICommittee for edit and NewCommitteeFormGroupInput for create.
 */
type CommitteeFormGroupInput = ICommittee | PartialWithRequiredKeyOf<NewCommittee>;

type CommitteeFormDefaults = Pick<NewCommittee, 'id'>;

type CommitteeFormGroupContent = {
  id: FormControl<ICommittee['id'] | NewCommittee['id']>;
  name: FormControl<ICommittee['name']>;
};

export type CommitteeFormGroup = FormGroup<CommitteeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class CommitteeFormService {
  createCommitteeFormGroup(committee: CommitteeFormGroupInput = { id: null }): CommitteeFormGroup {
    const committeeRawValue = {
      ...this.getFormDefaults(),
      ...committee,
    };
    return new FormGroup<CommitteeFormGroupContent>({
      id: new FormControl(
        { value: committeeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      name: new FormControl(committeeRawValue.name, {
        validators: [Validators.required],
      }),
    });
  }

  getCommittee(form: CommitteeFormGroup): ICommittee | NewCommittee {
    return form.getRawValue() as ICommittee | NewCommittee;
  }

  resetForm(form: CommitteeFormGroup, committee: CommitteeFormGroupInput): void {
    const committeeRawValue = { ...this.getFormDefaults(), ...committee };
    form.reset(
      {
        ...committeeRawValue,
        id: { value: committeeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): CommitteeFormDefaults {
    return {
      id: null,
    };
  }
}
