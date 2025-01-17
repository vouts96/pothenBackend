import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPosition } from 'app/entities/position/position.model';
import { PositionService } from 'app/entities/position/service/position.service';
import { IGrade } from 'app/entities/grade/grade.model';
import { GradeService } from 'app/entities/grade/service/grade.service';
import { ICommittee } from 'app/entities/committee/committee.model';
import { CommitteeService } from 'app/entities/committee/service/committee.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { SubmissionService } from '../service/submission.service';
import { ISubmission } from '../submission.model';
import { SubmissionFormGroup, SubmissionFormService } from './submission-form.service';

@Component({
  standalone: true,
  selector: 'jhi-submission-update',
  templateUrl: './submission-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class SubmissionUpdateComponent implements OnInit {
  isSaving = false;
  submission: ISubmission | null = null;

  positionsSharedCollection: IPosition[] = [];
  gradesSharedCollection: IGrade[] = [];
  committeesSharedCollection: ICommittee[] = [];
  usersSharedCollection: IUser[] = [];

  protected submissionService = inject(SubmissionService);
  protected submissionFormService = inject(SubmissionFormService);
  protected positionService = inject(PositionService);
  protected gradeService = inject(GradeService);
  protected committeeService = inject(CommitteeService);
  protected userService = inject(UserService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: SubmissionFormGroup = this.submissionFormService.createSubmissionFormGroup();

  comparePosition = (o1: IPosition | null, o2: IPosition | null): boolean => this.positionService.comparePosition(o1, o2);

  compareGrade = (o1: IGrade | null, o2: IGrade | null): boolean => this.gradeService.compareGrade(o1, o2);

  compareCommittee = (o1: ICommittee | null, o2: ICommittee | null): boolean => this.committeeService.compareCommittee(o1, o2);

  compareUser = (o1: IUser | null, o2: IUser | null): boolean => this.userService.compareUser(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ submission }) => {
      this.submission = submission;
      if (submission) {
        this.updateForm(submission);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const submission = this.submissionFormService.getSubmission(this.editForm);
    if (submission.id !== null) {
      this.subscribeToSaveResponse(this.submissionService.update(submission));
    } else {
      this.subscribeToSaveResponse(this.submissionService.create(submission));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubmission>>): void {
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

  protected updateForm(submission: ISubmission): void {
    this.submission = submission;
    this.submissionFormService.resetForm(this.editForm, submission);

    this.positionsSharedCollection = this.positionService.addPositionToCollectionIfMissing<IPosition>(
      this.positionsSharedCollection,
      submission.position,
    );
    this.gradesSharedCollection = this.gradeService.addGradeToCollectionIfMissing<IGrade>(this.gradesSharedCollection, submission.grade);
    this.committeesSharedCollection = this.committeeService.addCommitteeToCollectionIfMissing<ICommittee>(
      this.committeesSharedCollection,
      submission.committeeName,
    );
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing<IUser>(this.usersSharedCollection, submission.user);
  }

  protected loadRelationshipsOptions(): void {
    this.positionService
      .query()
      .pipe(map((res: HttpResponse<IPosition[]>) => res.body ?? []))
      .pipe(
        map((positions: IPosition[]) =>
          this.positionService.addPositionToCollectionIfMissing<IPosition>(positions, this.submission?.position),
        ),
      )
      .subscribe((positions: IPosition[]) => (this.positionsSharedCollection = positions));

    this.gradeService
      .query()
      .pipe(map((res: HttpResponse<IGrade[]>) => res.body ?? []))
      .pipe(map((grades: IGrade[]) => this.gradeService.addGradeToCollectionIfMissing<IGrade>(grades, this.submission?.grade)))
      .subscribe((grades: IGrade[]) => (this.gradesSharedCollection = grades));

    this.committeeService
      .query()
      .pipe(map((res: HttpResponse<ICommittee[]>) => res.body ?? []))
      .pipe(
        map((committees: ICommittee[]) =>
          this.committeeService.addCommitteeToCollectionIfMissing<ICommittee>(committees, this.submission?.committeeName),
        ),
      )
      .subscribe((committees: ICommittee[]) => (this.committeesSharedCollection = committees));

    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing<IUser>(users, this.submission?.user)))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));
  }
}
