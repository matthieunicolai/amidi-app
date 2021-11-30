import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProUser, ProUser } from '../pro-user.model';
import { ProUserService } from '../service/pro-user.service';
import { IRestaurant } from 'app/entities/restaurant/restaurant.model';
import { RestaurantService } from 'app/entities/restaurant/service/restaurant.service';
import { UserRole } from 'app/entities/enumerations/user-role.model';

@Component({
  selector: 'jhi-pro-user-update',
  templateUrl: './pro-user-update.component.html',
})
export class ProUserUpdateComponent implements OnInit {
  isSaving = false;
  userRoleValues = Object.keys(UserRole);

  restaurantsSharedCollection: IRestaurant[] = [];

  editForm = this.fb.group({
    id: [],
    proUserName: [null, [Validators.required]],
    proUserSurname: [null, [Validators.required]],
    proUserRole: [null, [Validators.required]],
    proUserLogin: [null, [Validators.required]],
    proUserPwd: [null, [Validators.required]],
    proUserEmail: [null, [Validators.required]],
    proUserPhoneNumber: [null, [Validators.required]],
    isActivated: [null, [Validators.required]],
    restaurant: [],
  });

  constructor(
    protected proUserService: ProUserService,
    protected restaurantService: RestaurantService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proUser }) => {
      this.updateForm(proUser);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const proUser = this.createFromForm();
    if (proUser.id !== undefined) {
      this.subscribeToSaveResponse(this.proUserService.update(proUser));
    } else {
      this.subscribeToSaveResponse(this.proUserService.create(proUser));
    }
  }

  trackRestaurantById(index: number, item: IRestaurant): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProUser>>): void {
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

  protected updateForm(proUser: IProUser): void {
    this.editForm.patchValue({
      id: proUser.id,
      proUserName: proUser.proUserName,
      proUserSurname: proUser.proUserSurname,
      proUserRole: proUser.proUserRole,
      proUserLogin: proUser.proUserLogin,
      proUserPwd: proUser.proUserPwd,
      proUserEmail: proUser.proUserEmail,
      proUserPhoneNumber: proUser.proUserPhoneNumber,
      isActivated: proUser.isActivated,
      restaurant: proUser.restaurant,
    });

    this.restaurantsSharedCollection = this.restaurantService.addRestaurantToCollectionIfMissing(
      this.restaurantsSharedCollection,
      proUser.restaurant
    );
  }

  protected loadRelationshipsOptions(): void {
    this.restaurantService
      .query()
      .pipe(map((res: HttpResponse<IRestaurant[]>) => res.body ?? []))
      .pipe(
        map((restaurants: IRestaurant[]) =>
          this.restaurantService.addRestaurantToCollectionIfMissing(restaurants, this.editForm.get('restaurant')!.value)
        )
      )
      .subscribe((restaurants: IRestaurant[]) => (this.restaurantsSharedCollection = restaurants));
  }

  protected createFromForm(): IProUser {
    return {
      ...new ProUser(),
      id: this.editForm.get(['id'])!.value,
      proUserName: this.editForm.get(['proUserName'])!.value,
      proUserSurname: this.editForm.get(['proUserSurname'])!.value,
      proUserRole: this.editForm.get(['proUserRole'])!.value,
      proUserLogin: this.editForm.get(['proUserLogin'])!.value,
      proUserPwd: this.editForm.get(['proUserPwd'])!.value,
      proUserEmail: this.editForm.get(['proUserEmail'])!.value,
      proUserPhoneNumber: this.editForm.get(['proUserPhoneNumber'])!.value,
      isActivated: this.editForm.get(['isActivated'])!.value,
      restaurant: this.editForm.get(['restaurant'])!.value,
    };
  }
}
