import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IGrade } from '../grade.model';
import { GradeService } from '../service/grade.service';
import { GradeFormGroup, GradeFormService } from './grade-form.service';

@Component({
  standalone: true,
  selector: 'jhi-grade-update',
  templateUrl: './grade-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class GradeUpdateComponent implements OnInit {
  isSaving = false;
  grade: IGrade | null = null;

  protected gradeService = inject(GradeService);
  protected gradeFormService = inject(GradeFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: GradeFormGroup = this.gradeFormService.createGradeFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ grade }) => {
      this.grade = grade;
      if (grade) {
        this.updateForm(grade);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const grade = this.gradeFormService.getGrade(this.editForm);
    if (grade.id !== null) {
      this.subscribeToSaveResponse(this.gradeService.update(grade));
    } else {
      this.subscribeToSaveResponse(this.gradeService.create(grade));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGrade>>): void {
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

  protected updateForm(grade: IGrade): void {
    this.grade = grade;
    this.gradeFormService.resetForm(this.editForm, grade);
  }
}
