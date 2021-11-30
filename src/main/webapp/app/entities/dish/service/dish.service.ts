import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IDish, getDishIdentifier } from '../dish.model';

export type EntityResponseType = HttpResponse<IDish>;
export type EntityArrayResponseType = HttpResponse<IDish[]>;

@Injectable({ providedIn: 'root' })
export class DishService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/dishes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/dishes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(dish: IDish): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dish);
    return this.http
      .post<IDish>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(dish: IDish): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dish);
    return this.http
      .put<IDish>(`${this.resourceUrl}/${getDishIdentifier(dish) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(dish: IDish): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(dish);
    return this.http
      .patch<IDish>(`${this.resourceUrl}/${getDishIdentifier(dish) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IDish>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDish[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IDish[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  addDishToCollectionIfMissing(dishCollection: IDish[], ...dishesToCheck: (IDish | null | undefined)[]): IDish[] {
    const dishes: IDish[] = dishesToCheck.filter(isPresent);
    if (dishes.length > 0) {
      const dishCollectionIdentifiers = dishCollection.map(dishItem => getDishIdentifier(dishItem)!);
      const dishesToAdd = dishes.filter(dishItem => {
        const dishIdentifier = getDishIdentifier(dishItem);
        if (dishIdentifier == null || dishCollectionIdentifiers.includes(dishIdentifier)) {
          return false;
        }
        dishCollectionIdentifiers.push(dishIdentifier);
        return true;
      });
      return [...dishesToAdd, ...dishCollection];
    }
    return dishCollection;
  }

  protected convertDateFromClient(dish: IDish): IDish {
    return Object.assign({}, dish, {
      dishDate: dish.dishDate?.isValid() ? dish.dishDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dishDate = res.body.dishDate ? dayjs(res.body.dishDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((dish: IDish) => {
        dish.dishDate = dish.dishDate ? dayjs(dish.dishDate) : undefined;
      });
    }
    return res;
  }
}
