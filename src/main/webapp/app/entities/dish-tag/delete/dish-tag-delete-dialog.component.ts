import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDishTag } from '../dish-tag.model';
import { DishTagService } from '../service/dish-tag.service';

@Component({
  templateUrl: './dish-tag-delete-dialog.component.html',
})
export class DishTagDeleteDialogComponent {
  dishTag?: IDishTag;

  constructor(protected dishTagService: DishTagService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.dishTagService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
