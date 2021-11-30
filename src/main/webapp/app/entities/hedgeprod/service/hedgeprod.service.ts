import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IHedgeprod, getHedgeprodIdentifier } from '../hedgeprod.model';

export type EntityResponseType = HttpResponse<IHedgeprod>;
export type EntityArrayResponseType = HttpResponse<IHedgeprod[]>;

@Injectable({ providedIn: 'root' })
export class HedgeprodService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/hedgeprods');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/hedgeprods');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(hedgeprod: IHedgeprod): Observable<EntityResponseType> {
    return this.http.post<IHedgeprod>(this.resourceUrl, hedgeprod, { observe: 'response' });
  }

  update(hedgeprod: IHedgeprod): Observable<EntityResponseType> {
    return this.http.put<IHedgeprod>(`${this.resourceUrl}/${getHedgeprodIdentifier(hedgeprod) as number}`, hedgeprod, {
      observe: 'response',
    });
  }

  partialUpdate(hedgeprod: IHedgeprod): Observable<EntityResponseType> {
    return this.http.patch<IHedgeprod>(`${this.resourceUrl}/${getHedgeprodIdentifier(hedgeprod) as number}`, hedgeprod, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IHedgeprod>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHedgeprod[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IHedgeprod[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addHedgeprodToCollectionIfMissing(
    hedgeprodCollection: IHedgeprod[],
    ...hedgeprodsToCheck: (IHedgeprod | null | undefined)[]
  ): IHedgeprod[] {
    const hedgeprods: IHedgeprod[] = hedgeprodsToCheck.filter(isPresent);
    if (hedgeprods.length > 0) {
      const hedgeprodCollectionIdentifiers = hedgeprodCollection.map(hedgeprodItem => getHedgeprodIdentifier(hedgeprodItem)!);
      const hedgeprodsToAdd = hedgeprods.filter(hedgeprodItem => {
        const hedgeprodIdentifier = getHedgeprodIdentifier(hedgeprodItem);
        if (hedgeprodIdentifier == null || hedgeprodCollectionIdentifiers.includes(hedgeprodIdentifier)) {
          return false;
        }
        hedgeprodCollectionIdentifiers.push(hedgeprodIdentifier);
        return true;
      });
      return [...hedgeprodsToAdd, ...hedgeprodCollection];
    }
    return hedgeprodCollection;
  }
}
