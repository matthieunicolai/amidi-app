import * as dayjs from 'dayjs';
import { IProUser } from 'app/entities/pro-user/pro-user.model';
import { IPicture } from 'app/entities/picture/picture.model';
import { IDish } from 'app/entities/dish/dish.model';
import { ILocation } from 'app/entities/location/location.model';
import { IClient } from 'app/entities/client/client.model';
import { RestaurationType } from 'app/entities/enumerations/restauration-type.model';
import { SubscriptionType } from 'app/entities/enumerations/subscription-type.model';

export interface IRestaurant {
  id?: number;
  restaurantName?: string;
  restaurantAddress?: string;
  restaurantAddressCmp?: string;
  restaurantType?: RestaurationType | null;
  isSub?: boolean;
  restaurantSubscription?: SubscriptionType | null;
  restaurantSubDate?: dayjs.Dayjs | null;
  restaurantDescription?: string | null;
  restaurantPhoneNumber?: string;
  restaurantEmail?: string;
  restaurantWebSite?: string | null;
  restaurantLatitude?: number;
  restaurantLongitude?: number;
  isActivated?: boolean;
  proUsers?: IProUser[] | null;
  pictures?: IPicture[] | null;
  dishes?: IDish[] | null;
  location?: ILocation | null;
  clients?: IClient[] | null;
}

export class Restaurant implements IRestaurant {
  constructor(
    public id?: number,
    public restaurantName?: string,
    public restaurantAddress?: string,
    public restaurantAddressCmp?: string,
    public restaurantType?: RestaurationType | null,
    public isSub?: boolean,
    public restaurantSubscription?: SubscriptionType | null,
    public restaurantSubDate?: dayjs.Dayjs | null,
    public restaurantDescription?: string | null,
    public restaurantPhoneNumber?: string,
    public restaurantEmail?: string,
    public restaurantWebSite?: string | null,
    public restaurantLatitude?: number,
    public restaurantLongitude?: number,
    public isActivated?: boolean,
    public proUsers?: IProUser[] | null,
    public pictures?: IPicture[] | null,
    public dishes?: IDish[] | null,
    public location?: ILocation | null,
    public clients?: IClient[] | null
  ) {
    this.isSub = this.isSub ?? false;
    this.isActivated = this.isActivated ?? false;
  }
}

export function getRestaurantIdentifier(restaurant: IRestaurant): number | undefined {
  return restaurant.id;
}
