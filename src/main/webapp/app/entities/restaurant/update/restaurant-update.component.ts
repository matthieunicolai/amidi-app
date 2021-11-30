import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IRestaurant, Restaurant } from '../restaurant.model';
import { RestaurantService } from '../service/restaurant.service';
import { ILocation } from 'app/entities/location/location.model';
import { LocationService } from 'app/entities/location/service/location.service';
import { RestaurationType } from 'app/entities/enumerations/restauration-type.model';
import { SubscriptionType } from 'app/entities/enumerations/subscription-type.model';

@Component({
  selector: 'jhi-restaurant-update',
  templateUrl: './restaurant-update.component.html',
})
export class RestaurantUpdateComponent implements OnInit {
  isSaving = false;
  restaurationTypeValues = Object.keys(RestaurationType);
  subscriptionTypeValues = Object.keys(SubscriptionType);

  locationsSharedCollection: ILocation[] = [];

  editForm = this.fb.group({
    id: [],
    restaurantName: [null, [Validators.required]],
    restaurantAddress: [null, [Validators.required]],
    restaurantAddressCmp: [null, [Validators.required]],
    restaurantType: [],
    isSub: [null, [Validators.required]],
    restaurantSubscription: [],
    restaurantSubDate: [],
    restaurantDescription: [],
    restaurantPhoneNumber: [null, [Validators.required]],
    restaurantEmail: [null, [Validators.required]],
    restaurantWebSite: [],
    restaurantLatitude: [null, [Validators.required]],
    restaurantLongitude: [null, [Validators.required]],
    isActivated: [null, [Validators.required]],
    location: [],
    location: [],
  });

  constructor(
    protected restaurantService: RestaurantService,
    protected locationService: LocationService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ restaurant }) => {
      if (restaurant.id === undefined) {
        const today = dayjs().startOf('day');
        restaurant.restaurantSubDate = today;
      }

      this.updateForm(restaurant);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const restaurant = this.createFromForm();
    if (restaurant.id !== undefined) {
      this.subscribeToSaveResponse(this.restaurantService.update(restaurant));
    } else {
      this.subscribeToSaveResponse(this.restaurantService.create(restaurant));
    }
  }

  trackLocationById(index: number, item: ILocation): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IRestaurant>>): void {
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

  protected updateForm(restaurant: IRestaurant): void {
    this.editForm.patchValue({
      id: restaurant.id,
      restaurantName: restaurant.restaurantName,
      restaurantAddress: restaurant.restaurantAddress,
      restaurantAddressCmp: restaurant.restaurantAddressCmp,
      restaurantType: restaurant.restaurantType,
      isSub: restaurant.isSub,
      restaurantSubscription: restaurant.restaurantSubscription,
      restaurantSubDate: restaurant.restaurantSubDate ? restaurant.restaurantSubDate.format(DATE_TIME_FORMAT) : null,
      restaurantDescription: restaurant.restaurantDescription,
      restaurantPhoneNumber: restaurant.restaurantPhoneNumber,
      restaurantEmail: restaurant.restaurantEmail,
      restaurantWebSite: restaurant.restaurantWebSite,
      restaurantLatitude: restaurant.restaurantLatitude,
      restaurantLongitude: restaurant.restaurantLongitude,
      isActivated: restaurant.isActivated,
      location: restaurant.location,
      location: restaurant.location,
    });

    this.locationsSharedCollection = this.locationService.addLocationToCollectionIfMissing(
      this.locationsSharedCollection,
      restaurant.location,
      restaurant.location
    );
  }

  protected loadRelationshipsOptions(): void {
    this.locationService
      .query()
      .pipe(map((res: HttpResponse<ILocation[]>) => res.body ?? []))
      .pipe(
        map((locations: ILocation[]) =>
          this.locationService.addLocationToCollectionIfMissing(
            locations,
            this.editForm.get('location')!.value,
            this.editForm.get('location')!.value
          )
        )
      )
      .subscribe((locations: ILocation[]) => (this.locationsSharedCollection = locations));
  }

  protected createFromForm(): IRestaurant {
    return {
      ...new Restaurant(),
      id: this.editForm.get(['id'])!.value,
      restaurantName: this.editForm.get(['restaurantName'])!.value,
      restaurantAddress: this.editForm.get(['restaurantAddress'])!.value,
      restaurantAddressCmp: this.editForm.get(['restaurantAddressCmp'])!.value,
      restaurantType: this.editForm.get(['restaurantType'])!.value,
      isSub: this.editForm.get(['isSub'])!.value,
      restaurantSubscription: this.editForm.get(['restaurantSubscription'])!.value,
      restaurantSubDate: this.editForm.get(['restaurantSubDate'])!.value
        ? dayjs(this.editForm.get(['restaurantSubDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      restaurantDescription: this.editForm.get(['restaurantDescription'])!.value,
      restaurantPhoneNumber: this.editForm.get(['restaurantPhoneNumber'])!.value,
      restaurantEmail: this.editForm.get(['restaurantEmail'])!.value,
      restaurantWebSite: this.editForm.get(['restaurantWebSite'])!.value,
      restaurantLatitude: this.editForm.get(['restaurantLatitude'])!.value,
      restaurantLongitude: this.editForm.get(['restaurantLongitude'])!.value,
      isActivated: this.editForm.get(['isActivated'])!.value,
      location: this.editForm.get(['location'])!.value,
      location: this.editForm.get(['location'])!.value,
    };
  }
}
