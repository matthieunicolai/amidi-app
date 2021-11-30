import * as dayjs from 'dayjs';
import { IRestaurant } from 'app/entities/restaurant/restaurant.model';

export interface IClient {
  id?: number;
  name?: string;
  surname?: string;
  birthDate?: dayjs.Dayjs | null;
  email?: string;
  phoneNumber?: string | null;
  clientLogin?: string;
  clientPwd?: string;
  isActivated?: boolean;
  restaurants?: IRestaurant[] | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public name?: string,
    public surname?: string,
    public birthDate?: dayjs.Dayjs | null,
    public email?: string,
    public phoneNumber?: string | null,
    public clientLogin?: string,
    public clientPwd?: string,
    public isActivated?: boolean,
    public restaurants?: IRestaurant[] | null
  ) {
    this.isActivated = this.isActivated ?? false;
  }
}

export function getClientIdentifier(client: IClient): number | undefined {
  return client.id;
}
