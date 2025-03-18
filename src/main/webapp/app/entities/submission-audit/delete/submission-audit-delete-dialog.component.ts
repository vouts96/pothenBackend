import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ISubmissionAudit } from '../submission-audit.model';
import { SubmissionAuditService } from '../service/submission-audit.service';

@Component({
  standalone: true,
  templateUrl: './submission-audit-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class SubmissionAuditDeleteDialogComponent {
  submissionAudit?: ISubmissionAudit;

  protected submissionAuditService = inject(SubmissionAuditService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.submissionAuditService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
