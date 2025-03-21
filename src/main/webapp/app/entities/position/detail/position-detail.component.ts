import { Component, input } from '@angular/core';
import { RouterModule } from '@angular/router';

import SharedModule from 'app/shared/shared.module';
import { DurationPipe, FormatMediumDatePipe, FormatMediumDatetimePipe } from 'app/shared/date';
import { IPosition } from '../position.model';

@Component({
  standalone: true,
  selector: 'jhi-position-detail',
  templateUrl: './position-detail.component.html',
  imports: [SharedModule, RouterModule, DurationPipe, FormatMediumDatetimePipe, FormatMediumDatePipe],
})
export class PositionDetailComponent {
  position = input<IPosition | null>(null);

  previousState(): void {
    window.history.back();
  }
}
