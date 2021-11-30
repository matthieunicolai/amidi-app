import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProUserComponent } from '../list/pro-user.component';
import { ProUserDetailComponent } from '../detail/pro-user-detail.component';
import { ProUserUpdateComponent } from '../update/pro-user-update.component';
import { ProUserRoutingResolveService } from './pro-user-routing-resolve.service';

const proUserRoute: Routes = [
  {
    path: '',
    component: ProUserComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProUserDetailComponent,
    resolve: {
      proUser: ProUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProUserUpdateComponent,
    resolve: {
      proUser: ProUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProUserUpdateComponent,
    resolve: {
      proUser: ProUserRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(proUserRoute)],
  exports: [RouterModule],
})
export class ProUserRoutingModule {}
