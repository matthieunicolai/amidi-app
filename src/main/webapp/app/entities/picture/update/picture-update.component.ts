import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPicture, Picture } from '../picture.model';
import { PictureService } from '../service/picture.service';
import { IRestaurant } from 'app/entities/restaurant/restaurant.model';
import { RestaurantService } from 'app/entities/restaurant/service/restaurant.service';

@Component({
  selector: 'jhi-picture-update',
  templateUrl: './picture-update.component.html',
})
export class PictureUpdateComponent implements OnInit {
  isSaving = false;

  restaurantsSharedCollection: IRestaurant[] = [];

  editForm = this.fb.group({
    id: [],
    pictureName: [null, [Validators.required]],
    pictureUrl: [null, [Validators.required]],
    pictureAlt: [null, [Validators.required]],
    isLogo: [null, [Validators.required]],
    isDisplayed: [null, [Validators.required]],
    restaurant: [],
    restaurant: [],
  });

  constructor(
    protected pictureService: PictureService,
    protected restaurantService: RestaurantService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ picture }) => {
      this.updateForm(picture);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const picture = this.createFromForm();
    if (picture.id !== undefined) {
      this.subscribeToSaveResponse(this.pictureService.update(picture));
    } else {
      this.subscribeToSaveResponse(this.pictureService.create(picture));
    }
  }

  trackRestaurantById(index: number, item: IRestaurant): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPicture>>): void {
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

  protected updateForm(picture: IPicture): void {
    this.editForm.patchValue({
      id: picture.id,
      pictureName: picture.pictureName,
      pictureUrl: picture.pictureUrl,
      pictureAlt: picture.pictureAlt,
      isLogo: picture.isLogo,
      isDisplayed: picture.isDisplayed,
      restaurant: picture.restaurant,
      restaurant: picture.restaurant,
    });

    this.restaurantsSharedCollection = this.restaurantService.addRestaurantToCollectionIfMissing(
      this.restaurantsSharedCollection,
      picture.restaurant,
      picture.restaurant
    );
  }

  protected loadRelationshipsOptions(): void {
    this.restaurantService
      .query()
      .pipe(map((res: HttpResponse<IRestaurant[]>) => res.body ?? []))
      .pipe(
        map((restaurants: IRestaurant[]) =>
          this.restaurantService.addRestaurantToCollectionIfMissing(
            restaurants,
            this.editForm.get('restaurant')!.value,
            this.editForm.get('restaurant')!.value
          )
        )
      )
      .subscribe((restaurants: IRestaurant[]) => (this.restaurantsSharedCollection = restaurants));
  }

  protected createFromForm(): IPicture {
    return {
      ...new Picture(),
      id: this.editForm.get(['id'])!.value,
      pictureName: this.editForm.get(['pictureName'])!.value,
      pictureUrl: this.editForm.get(['pictureUrl'])!.value,
      pictureAlt: this.editForm.get(['pictureAlt'])!.value,
      isLogo: this.editForm.get(['isLogo'])!.value,
      isDisplayed: this.editForm.get(['isDisplayed'])!.value,
      restaurant: this.editForm.get(['restaurant'])!.value,
      restaurant: this.editForm.get(['restaurant'])!.value,
    };
  }
}
