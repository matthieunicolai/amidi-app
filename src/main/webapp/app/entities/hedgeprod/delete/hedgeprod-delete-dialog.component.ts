import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IHedgeprod } from '../hedgeprod.model';
import { HedgeprodService } from '../service/hedgeprod.service';

@Component({
  templateUrl: './hedgeprod-delete-dialog.component.html',
})
export class HedgeprodDeleteDialogComponent {
  hedgeprod?: IHedgeprod;

  constructor(protected hedgeprodService: HedgeprodService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.hedgeprodService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
