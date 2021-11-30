import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IProUser, getProUserIdentifier } from '../pro-user.model';

export type EntityResponseType = HttpResponse<IProUser>;
export type EntityArrayResponseType = HttpResponse<IProUser[]>;

@Injectable({ providedIn: 'root' })
export class ProUserService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/pro-users');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/pro-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(proUser: IProUser): Observable<EntityResponseType> {
    return this.http.post<IProUser>(this.resourceUrl, proUser, { observe: 'response' });
  }

  update(proUser: IProUser): Observable<EntityResponseType> {
    return this.http.put<IProUser>(`${this.resourceUrl}/${getProUserIdentifier(proUser) as number}`, proUser, { observe: 'response' });
  }

  partialUpdate(proUser: IProUser): Observable<EntityResponseType> {
    return this.http.patch<IProUser>(`${this.resourceUrl}/${getProUserIdentifier(proUser) as number}`, proUser, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProUser>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProUser[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProUser[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addProUserToCollectionIfMissing(proUserCollection: IProUser[], ...proUsersToCheck: (IProUser | null | undefined)[]): IProUser[] {
    const proUsers: IProUser[] = proUsersToCheck.filter(isPresent);
    if (proUsers.length > 0) {
      const proUserCollectionIdentifiers = proUserCollection.map(proUserItem => getProUserIdentifier(proUserItem)!);
      const proUsersToAdd = proUsers.filter(proUserItem => {
        const proUserIdentifier = getProUserIdentifier(proUserItem);
        if (proUserIdentifier == null || proUserCollectionIdentifiers.includes(proUserIdentifier)) {
          return false;
        }
        proUserCollectionIdentifiers.push(proUserIdentifier);
        return true;
      });
      return [...proUsersToAdd, ...proUserCollection];
    }
    return proUserCollection;
  }
}
