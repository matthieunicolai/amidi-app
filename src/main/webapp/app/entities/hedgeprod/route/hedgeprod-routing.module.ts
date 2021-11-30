import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { HedgeprodComponent } from '../list/hedgeprod.component';
import { HedgeprodDetailComponent } from '../detail/hedgeprod-detail.component';
import { HedgeprodUpdateComponent } from '../update/hedgeprod-update.component';
import { HedgeprodRoutingResolveService } from './hedgeprod-routing-resolve.service';

const hedgeprodRoute: Routes = [
  {
    path: '',
    component: HedgeprodComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: HedgeprodDetailComponent,
    resolve: {
      hedgeprod: HedgeprodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: HedgeprodUpdateComponent,
    resolve: {
      hedgeprod: HedgeprodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: HedgeprodUpdateComponent,
    resolve: {
      hedgeprod: HedgeprodRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(hedgeprodRoute)],
  exports: [RouterModule],
})
export class HedgeprodRoutingModule {}
