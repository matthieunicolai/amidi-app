import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IDish, Dish } from '../dish.model';
import { DishService } from '../service/dish.service';
import { IRestaurant } from 'app/entities/restaurant/restaurant.model';
import { RestaurantService } from 'app/entities/restaurant/service/restaurant.service';

@Component({
  selector: 'jhi-dish-update',
  templateUrl: './dish-update.component.html',
})
export class DishUpdateComponent implements OnInit {
  isSaving = false;

  restaurantsSharedCollection: IRestaurant[] = [];

  editForm = this.fb.group({
    id: [],
    dishName: [null, [Validators.required]],
    dishDescription: [],
    dishPrice: [null, [Validators.required]],
    dishDate: [null, [Validators.required]],
    dishPictureName: [null, [Validators.required]],
    dishPictureUrl: [null, [Validators.required]],
    dishPictureAlt: [],
    isDailyDish: [null, [Validators.required]],
    isAvailable: [null, [Validators.required]],
    restaurant: [],
    restaurant: [],
  });

  constructor(
    protected dishService: DishService,
    protected restaurantService: RestaurantService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dish }) => {
      if (dish.id === undefined) {
        const today = dayjs().startOf('day');
        dish.dishDate = today;
      }

      this.updateForm(dish);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dish = this.createFromForm();
    if (dish.id !== undefined) {
      this.subscribeToSaveResponse(this.dishService.update(dish));
    } else {
      this.subscribeToSaveResponse(this.dishService.create(dish));
    }
  }

  trackRestaurantById(index: number, item: IRestaurant): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDish>>): void {
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

  protected updateForm(dish: IDish): void {
    this.editForm.patchValue({
      id: dish.id,
      dishName: dish.dishName,
      dishDescription: dish.dishDescription,
      dishPrice: dish.dishPrice,
      dishDate: dish.dishDate ? dish.dishDate.format(DATE_TIME_FORMAT) : null,
      dishPictureName: dish.dishPictureName,
      dishPictureUrl: dish.dishPictureUrl,
      dishPictureAlt: dish.dishPictureAlt,
      isDailyDish: dish.isDailyDish,
      isAvailable: dish.isAvailable,
      restaurant: dish.restaurant,
      restaurant: dish.restaurant,
    });

    this.restaurantsSharedCollection = this.restaurantService.addRestaurantToCollectionIfMissing(
      this.restaurantsSharedCollection,
      dish.restaurant,
      dish.restaurant
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

  protected createFromForm(): IDish {
    return {
      ...new Dish(),
      id: this.editForm.get(['id'])!.value,
      dishName: this.editForm.get(['dishName'])!.value,
      dishDescription: this.editForm.get(['dishDescription'])!.value,
      dishPrice: this.editForm.get(['dishPrice'])!.value,
      dishDate: this.editForm.get(['dishDate'])!.value ? dayjs(this.editForm.get(['dishDate'])!.value, DATE_TIME_FORMAT) : undefined,
      dishPictureName: this.editForm.get(['dishPictureName'])!.value,
      dishPictureUrl: this.editForm.get(['dishPictureUrl'])!.value,
      dishPictureAlt: this.editForm.get(['dishPictureAlt'])!.value,
      isDailyDish: this.editForm.get(['isDailyDish'])!.value,
      isAvailable: this.editForm.get(['isAvailable'])!.value,
      restaurant: this.editForm.get(['restaurant'])!.value,
      restaurant: this.editForm.get(['restaurant'])!.value,
    };
  }
}
