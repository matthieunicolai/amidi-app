import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IHedgeprod, Hedgeprod } from '../hedgeprod.model';
import { HedgeprodService } from '../service/hedgeprod.service';
import { HedgeprodRole } from 'app/entities/enumerations/hedgeprod-role.model';

@Component({
  selector: 'jhi-hedgeprod-update',
  templateUrl: './hedgeprod-update.component.html',
})
export class HedgeprodUpdateComponent implements OnInit {
  isSaving = false;
  hedgeprodRoleValues = Object.keys(HedgeprodRole);

  editForm = this.fb.group({
    id: [],
    hName: [null, [Validators.required]],
    hSurname: [null, [Validators.required]],
    hRole: [null, [Validators.required]],
    hEmail: [null, [Validators.required]],
    hPhoneNumber: [],
    isActivated: [null, [Validators.required]],
  });

  constructor(protected hedgeprodService: HedgeprodService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hedgeprod }) => {
      this.updateForm(hedgeprod);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const hedgeprod = this.createFromForm();
    if (hedgeprod.id !== undefined) {
      this.subscribeToSaveResponse(this.hedgeprodService.update(hedgeprod));
    } else {
      this.subscribeToSaveResponse(this.hedgeprodService.create(hedgeprod));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IHedgeprod>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(hedgeprod: IHedgeprod): void {
    this.editForm.patchValue({
      id: hedgeprod.id,
      hName: hedgeprod.hName,
      hSurname: hedgeprod.hSurname,
      hRole: hedgeprod.hRole,
      hEmail: hedgeprod.hEmail,
      hPhoneNumber: hedgeprod.hPhoneNumber,
      isActivated: hedgeprod.isActivated,
    });
  }

  protected createFromForm(): IHedgeprod {
    return {
      ...new Hedgeprod(),
      id: this.editForm.get(['id'])!.value,
      hName: this.editForm.get(['hName'])!.value,
      hSurname: this.editForm.get(['hSurname'])!.value,
      hRole: this.editForm.get(['hRole'])!.value,
      hEmail: this.editForm.get(['hEmail'])!.value,
      hPhoneNumber: this.editForm.get(['hPhoneNumber'])!.value,
      isActivated: this.editForm.get(['isActivated'])!.value,
    };
  }
}
