import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISubmission } from '../submission.model';

@Component({
  standalone: true,
  selector: 'jhi-submission-detail',
  templateUrl: './submission-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SubmissionDetailComponent {
  submission = input<ISubmission | null>(null);

  previousState(): void {
    window.history.back();
  }
}
