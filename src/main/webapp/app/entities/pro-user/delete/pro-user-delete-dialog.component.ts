import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProUser } from '../pro-user.model';
import { ProUserService } from '../service/pro-user.service';

@Component({
  templateUrl: './pro-user-delete-dialog.component.html',
})
export class ProUserDeleteDialogComponent {
  proUser?: IProUser;

  constructor(protected proUserService: ProUserService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.proUserService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
