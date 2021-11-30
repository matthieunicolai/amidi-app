import { IDish } from 'app/entities/dish/dish.model';
import { FoodType } from 'app/entities/enumerations/food-type.model';

export interface IDishTag {
  id?: number;
  dishTag?: FoodType;
  dish?: IDish | null;
}

export class DishTag implements IDishTag {
  constructor(public id?: number, public dishTag?: FoodType, public dish?: IDish | null) {}
}

export function getDishTagIdentifier(dishTag: IDishTag): number | undefined {
  return dishTag.id;
}
