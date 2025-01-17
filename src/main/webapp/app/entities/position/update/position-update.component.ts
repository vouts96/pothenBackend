import { Component, OnInit, inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IPosition } from '../position.model';
import { PositionService } from '../service/position.service';
import { PositionFormGroup, PositionFormService } from './position-form.service';

@Component({
  standalone: true,
  selector: 'jhi-position-update',
  templateUrl: './position-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class PositionUpdateComponent implements OnInit {
  isSaving = false;
  position: IPosition | null = null;

  protected positionService = inject(PositionService);
  protected positionFormService = inject(PositionFormService);
  protected activatedRoute = inject(ActivatedRoute);

  // eslint-disable-next-line @typescript-eslint/member-ordering
  editForm: PositionFormGroup = this.positionFormService.createPositionFormGroup();

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ position }) => {
      this.position = position;
      if (position) {
        this.updateForm(position);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const position = this.positionFormService.getPosition(this.editForm);
    if (position.id !== null) {
      this.subscribeToSaveResponse(this.positionService.update(position));
    } else {
      this.subscribeToSaveResponse(this.positionService.create(position));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPosition>>): void {
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

  protected updateForm(position: IPosition): void {
    this.position = position;
    this.positionFormService.resetForm(this.editForm, position);
  }
}
