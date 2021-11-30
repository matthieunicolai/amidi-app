import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProUser, ProUser } from '../pro-user.model';
import { ProUserService } from '../service/pro-user.service';

@Injectable({ providedIn: 'root' })
export class ProUserRoutingResolveService implements Resolve<IProUser> {
  constructor(protected service: ProUserService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IProUser> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((proUser: HttpResponse<ProUser>) => {
          if (proUser.body) {
            return of(proUser.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ProUser());
  }
}