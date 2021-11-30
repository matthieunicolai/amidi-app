import { IRestaurant } from 'app/entities/restaurant/restaurant.model';
import { UserRole } from 'app/entities/enumerations/user-role.model';

export interface IProUser {
  id?: number;
  proUserName?: string;
  proUserSurname?: string;
  proUserRole?: UserRole;
  proUserLogin?: string;
  proUserPwd?: string;
  proUserEmail?: string;
  proUserPhoneNumber?: string;
  isActivated?: boolean;
  restaurant?: IRestaurant | null;
  restaurant?: IRestaurant | null;
}

export class ProUser implements IProUser {
  constructor(
    public id?: number,
    public proUserName?: string,
    public proUserSurname?: string,
    public proUserRole?: UserRole,
    public proUserLogin?: string,
    public proUserPwd?: string,
    public proUserEmail?: string,
    public proUserPhoneNumber?: string,
    public isActivated?: boolean,
    public restaurant?: IRestaurant | null,
    public restaurant?: IRestaurant | null
  ) {
    this.isActivated = this.isActivated ?? false;
  }
}

export function getProUserIdentifier(proUser: IProUser): number | undefined {
  return proUser.id;
}
