import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IHedgeprod, Hedgeprod } from '../hedgeprod.model';
import { HedgeprodService } from '../service/hedgeprod.service';

@Injectable({ providedIn: 'root' })
export class HedgeprodRoutingResolveService implements Resolve<IHedgeprod> {
  constructor(protected service: HedgeprodService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IHedgeprod> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((hedgeprod: HttpResponse<Hedgeprod>) => {
          if (hedgeprod.body) {
            return of(hedgeprod.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Hedgeprod());
  }
}
