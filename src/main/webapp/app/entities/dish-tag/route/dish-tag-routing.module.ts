import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DishTagComponent } from '../list/dish-tag.component';
import { DishTagDetailComponent } from '../detail/dish-tag-detail.component';
import { DishTagUpdateComponent } from '../update/dish-tag-update.component';
import { DishTagRoutingResolveService } from './dish-tag-routing-resolve.service';

const dishTagRoute: Routes = [
  {
    path: '',
    component: DishTagComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DishTagDetailComponent,
    resolve: {
      dishTag: DishTagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DishTagUpdateComponent,
    resolve: {
      dishTag: DishTagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DishTagUpdateComponent,
    resolve: {
      dishTag: DishTagRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(dishTagRoute)],
  exports: [RouterModule],
})
export class DishTagRoutingModule {}
