import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IDishTag, DishTag } from '../dish-tag.model';
import { DishTagService } from '../service/dish-tag.service';
import { IDish } from 'app/entities/dish/dish.model';
import { DishService } from 'app/entities/dish/service/dish.service';
import { FoodType } from 'app/entities/enumerations/food-type.model';

@Component({
  selector: 'jhi-dish-tag-update',
  templateUrl: './dish-tag-update.component.html',
})
export class DishTagUpdateComponent implements OnInit {
  isSaving = false;
  foodTypeValues = Object.keys(FoodType);

  dishesSharedCollection: IDish[] = [];

  editForm = this.fb.group({
    id: [],
    dishTag: [null, [Validators.required]],
    dish: [],
  });

  constructor(
    protected dishTagService: DishTagService,
    protected dishService: DishService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dishTag }) => {
      this.updateForm(dishTag);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const dishTag = this.createFromForm();
    if (dishTag.id !== undefined) {
      this.subscribeToSaveResponse(this.dishTagService.update(dishTag));
    } else {
      this.subscribeToSaveResponse(this.dishTagService.create(dishTag));
    }
  }

  trackDishById(index: number, item: IDish): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IDishTag>>): void {
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

  protected updateForm(dishTag: IDishTag): void {
    this.editForm.patchValue({
      id: dishTag.id,
      dishTag: dishTag.dishTag,
      dish: dishTag.dish,
    });

    this.dishesSharedCollection = this.dishService.addDishToCollectionIfMissing(this.dishesSharedCollection, dishTag.dish);
  }

  protected loadRelationshipsOptions(): void {
    this.dishService
      .query()
      .pipe(map((res: HttpResponse<IDish[]>) => res.body ?? []))
      .pipe(map((dishes: IDish[]) => this.dishService.addDishToCollectionIfMissing(dishes, this.editForm.get('dish')!.value)))
      .subscribe((dishes: IDish[]) => (this.dishesSharedCollection = dishes));
  }

  protected createFromForm(): IDishTag {
    return {
      ...new DishTag(),
      id: this.editForm.get(['id'])!.value,
      dishTag: this.editForm.get(['dishTag'])!.value,
      dish: this.editForm.get(['dish'])!.value,
    };
  }
}
