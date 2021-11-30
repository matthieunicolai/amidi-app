import { IRestaurant } from 'app/entities/restaurant/restaurant.model';

export interface ILocation {
  id?: number;
  country?: string;
  city?: string;
  postalCode?: string;
  restaurants?: IRestaurant[] | null;
  restaurants?: IRestaurant[] | null;
}

export class Location implements ILocation {
  constructor(
    public id?: number,
    public country?: string,
    public city?: string,
    public postalCode?: string,
    public restaurants?: IRestaurant[] | null,
    public restaurants?: IRestaurant[] | null
  ) {}
}

export function getLocationIdentifier(location: ILocation): number | undefined {
  return location.id;
}
