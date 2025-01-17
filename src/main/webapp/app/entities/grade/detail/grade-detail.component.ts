import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IGrade } from '../grade.model';

@Component({
  standalone: true,
  selector: 'jhi-grade-detail',
  templateUrl: './grade-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class GradeDetailComponent {
  grade = input<IGrade | null>(null);

  previousState(): void {
    window.history.back();
  }
}
