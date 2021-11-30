import * as dayjs from 'dayjs';
import { IDishTag } from 'app/entities/dish-tag/dish-tag.model';
import { IRestaurant } from 'app/entities/restaurant/restaurant.model';

export interface IDish {
  id?: number;
  dishName?: string;
  dishDescription?: string | null;
  dishPrice?: number;
  dishDate?: dayjs.Dayjs;
  dishPictureName?: string;
  dishPictureUrl?: string;
  dishPictureAlt?: string | null;
  isDailyDish?: boolean;
  isAvailable?: boolean;
  dishTags?: IDishTag[] | null;
  restaurant?: IRestaurant | null;
  restaurant?: IRestaurant | null;
}

export class Dish implements IDish {
  constructor(
    public id?: number,
    public dishName?: string,
    public dishDescription?: string | null,
    public dishPrice?: number,
    public dishDate?: dayjs.Dayjs,
    public dishPictureName?: string,
    public dishPictureUrl?: string,
    public dishPictureAlt?: string | null,
    public isDailyDish?: boolean,
    public isAvailable?: boolean,
    public dishTags?: IDishTag[] | null,
    public restaurant?: IRestaurant | null,
    public restaurant?: IRestaurant | null
  ) {
    this.isDailyDish = this.isDailyDish ?? false;
    this.isAvailable = this.isAvailable ?? false;
  }
}

export function getDishIdentifier(dish: IDish): number | undefined {
  return dish.id;
}
