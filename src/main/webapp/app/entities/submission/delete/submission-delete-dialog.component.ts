import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISubmission } from '../submission.model';
import { SubmissionService } from '../service/submission.service';

@Component({
  standalone: true,
  templateUrl: './submission-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SubmissionDeleteDialogComponent {
  submission?: ISubmission;

  protected submissionService = inject(SubmissionService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.submissionService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
