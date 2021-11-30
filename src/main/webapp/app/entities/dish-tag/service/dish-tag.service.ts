import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Search } from 'app/core/request/request.model';
import { IDishTag, getDishTagIdentifier } from '../dish-tag.model';

export type EntityResponseType = HttpResponse<IDishTag>;
export type EntityArrayResponseType = HttpResponse<IDishTag[]>;

@Injectable({ providedIn: 'root' })
export class DishTagService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dish-tags');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/dish-tags');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dishTag: IDishTag): Observable<EntityResponseType> {
    return this.http.post<IDishTag>(this.resourceUrl, dishTag, { observe: 'response' });
  }

  update(dishTag: IDishTag): Observable<EntityResponseType> {
    return this.http.put<IDishTag>(`${this.resourceUrl}/${getDishTagIdentifier(dishTag) as number}`, dishTag, { observe: 'response' });
  }

  partialUpdate(dishTag: IDishTag): Observable<EntityResponseType> {
    return this.http.patch<IDishTag>(`${this.resourceUrl}/${getDishTagIdentifier(dishTag) as number}`, dishTag, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDishTag>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDishTag[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: Search): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDishTag[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  addDishTagToCollectionIfMissing(dishTagCollection: IDishTag[], ...dishTagsToCheck: (IDishTag | null | undefined)[]): IDishTag[] {
    const dishTags: IDishTag[] = dishTagsToCheck.filter(isPresent);
    if (dishTags.length > 0) {
      const dishTagCollectionIdentifiers = dishTagCollection.map(dishTagItem => getDishTagIdentifier(dishTagItem)!);
      const dishTagsToAdd = dishTags.filter(dishTagItem => {
        const dishTagIdentifier = getDishTagIdentifier(dishTagItem);
        if (dishTagIdentifier == null || dishTagCollectionIdentifiers.includes(dishTagIdentifier)) {
          return false;
        }
        dishTagCollectionIdentifiers.push(dishTagIdentifier);
        return true;
      });
      return [...dishTagsToAdd, ...dishTagCollection];
    }
    return dishTagCollection;
  }
}
