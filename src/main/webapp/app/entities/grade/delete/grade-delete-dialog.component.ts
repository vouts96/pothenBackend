import { Component, inject } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IGrade } from '../grade.model';
import { GradeService } from '../service/grade.service';

@Component({
  standalone: true,
  templateUrl: './grade-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class GradeDeleteDialogComponent {
  grade?: IGrade;

  protected gradeService = inject(GradeService);
  protected activeModal = inject(NgbActiveModal);

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.gradeService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
