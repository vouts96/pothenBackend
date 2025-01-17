import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ICommittee } from '../committee.model';
import { CommitteeService } from '../service/committee.service';
import { CommitteeFormGroup, CommitteeFormService } from './committee-form.service';

@Component({
  standalone: true,
  selector: 'jhi-committee-update',
  templateUrl: './committee-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class CommitteeUpdateComponent implements OnInit {
  isSaving = false;
  committee: ICommittee | null = null;

  protected committeeService = inject(CommitteeService);
  protected committeeFormService = inject(CommitteeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: CommitteeFormGroup = this.committeeFormService.createCommitteeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ committee }) => {
      this.committee = committee;
      if (committee) {
        this.updateForm(committee);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const committee = this.committeeFormService.getCommittee(this.editForm);
    if (committee.id !== null) {
      this.subscribeToSaveResponse(this.committeeService.update(committee));
    } else {
      this.subscribeToSaveResponse(this.committeeService.create(committee));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICommittee>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(committee: ICommittee): void {
    this.committee = committee;
    this.committeeFormService.resetForm(this.editForm, committee);
  }
}
