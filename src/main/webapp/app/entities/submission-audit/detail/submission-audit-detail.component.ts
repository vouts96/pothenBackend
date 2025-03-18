import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ISubmissionAudit } from '../submission-audit.model';

@Component({
  standalone: true,
  selector: 'jhi-submission-audit-detail',
  templateUrl: './submission-audit-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class SubmissionAuditDetailComponent {
  submissionAudit = input<ISubmissionAudit | null>(null);

  previousState(): void {
    window.history.back();
  }
}
