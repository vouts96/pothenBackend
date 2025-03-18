import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ISubmission } from 'app/entities/submission/submission.model';
import { SubmissionService } from 'app/entities/submission/service/submission.service';
import { ISubmissionAudit } from '../submission-audit.model';
import { SubmissionAuditService } from '../service/submission-audit.service';
import { SubmissionAuditFormGroup, SubmissionAuditFormService } from './submission-audit-form.service';

@Component({
  standalone: true,
  selector: 'jhi-submission-audit-update',
  templateUrl: './submission-audit-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SubmissionAuditUpdateComponent implements OnInit {
  isSaving = false;
  submissionAudit: ISubmissionAudit | null = null;

  submissionsSharedCollection: ISubmission[] = [];

  protected submissionAuditService = inject(SubmissionAuditService);
  protected submissionAuditFormService = inject(SubmissionAuditFormService);
  protected submissionService = inject(SubmissionService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SubmissionAuditFormGroup = this.submissionAuditFormService.createSubmissionAuditFormGroup();

  compareSubmission = (o1: ISubmission | null, o2: ISubmission | null): boolean => this.submissionService.compareSubmission(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ submissionAudit }) => {
      this.submissionAudit = submissionAudit;
      if (submissionAudit) {
        this.updateForm(submissionAudit);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const submissionAudit = this.submissionAuditFormService.getSubmissionAudit(this.editForm);
    if (submissionAudit.id !== null) {
      this.subscribeToSaveResponse(this.submissionAuditService.update(submissionAudit));
    } else {
      this.subscribeToSaveResponse(this.submissionAuditService.create(submissionAudit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubmissionAudit>>): void {
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

  protected updateForm(submissionAudit: ISubmissionAudit): void {
    this.submissionAudit = submissionAudit;
    this.submissionAuditFormService.resetForm(this.editForm, submissionAudit);

    this.submissionsSharedCollection = this.submissionService.addSubmissionToCollectionIfMissing<ISubmission>(
      this.submissionsSharedCollection,
      submissionAudit.originalSubmission,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.submissionService
      .query()
      .pipe(map((res: HttpResponse<ISubmission[]>) => res.body ?? []))
      .pipe(
        map((submissions: ISubmission[]) =>
          this.submissionService.addSubmissionToCollectionIfMissing<ISubmission>(submissions, this.submissionAudit?.originalSubmission),
        ),
      )
      .subscribe((submissions: ISubmission[]) => (this.submissionsSharedCollection = submissions));
  }
}
