import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { ICommittee } from '../committee.model';

@Component({
  standalone: true,
  selector: 'jhi-committee-detail',
  templateUrl: './committee-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class CommitteeDetailComponent {
  committee = input<ICommittee | null>(null);

  previousState(): void {
    window.history.back();
  }
}
