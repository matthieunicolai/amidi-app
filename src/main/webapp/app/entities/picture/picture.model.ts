import { IRestaurant } from 'app/entities/restaurant/restaurant.model';

export interface IPicture {
  id?: number;
  pictureName?: string;
  pictureUrl?: string;
  pictureAlt?: string;
  isLogo?: boolean;
  isDisplayed?: boolean;
  restaurant?: IRestaurant | null;
  restaurant?: IRestaurant | null;
}

export class Picture implements IPicture {
  constructor(
    public id?: number,
    public pictureName?: string,
    public pictureUrl?: string,
    public pictureAlt?: string,
    public isLogo?: boolean,
    public isDisplayed?: boolean,
    public restaurant?: IRestaurant | null,
    public restaurant?: IRestaurant | null
  ) {
    this.isLogo = this.isLogo ?? false;
    this.isDisplayed = this.isDisplayed ?? false;
  }
}

export function getPictureIdentifier(picture: IPicture): number | undefined {
  return picture.id;
}
