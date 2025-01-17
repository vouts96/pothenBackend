import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICommittee } from '../committee.model';
import { CommitteeService } from '../service/committee.service';

@Component({
  standalone: true,
  templateUrl: './committee-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CommitteeDeleteDialogComponent {
  committee?: ICommittee;

  protected committeeService = inject(CommitteeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.committeeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
