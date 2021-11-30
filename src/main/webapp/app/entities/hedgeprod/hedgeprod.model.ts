import { HedgeprodRole } from 'app/entities/enumerations/hedgeprod-role.model';

export interface IHedgeprod {
  id?: number;
  hName?: string;
  hSurname?: string;
  hRole?: HedgeprodRole;
  hEmail?: string;
  hPhoneNumber?: string | null;
  isActivated?: boolean;
}

export class Hedgeprod implements IHedgeprod {
  constructor(
    public id?: number,
    public hName?: string,
    public hSurname?: string,
    public hRole?: HedgeprodRole,
    public hEmail?: string,
    public hPhoneNumber?: string | null,
    public isActivated?: boolean
  ) {
    this.isActivated = this.isActivated ?? false;
  }
}

export function getHedgeprodIdentifier(hedgeprod: IHedgeprod): number | undefined {
  return hedgeprod.id;
}
