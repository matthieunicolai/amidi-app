import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'hedgeprod',
        data: { pageTitle: 'amidiappApp.hedgeprod.home.title' },
        loadChildren: () => import('./hedgeprod/hedgeprod.module').then(m => m.HedgeprodModule),
      },
      {
        path: 'restaurant',
        data: { pageTitle: 'amidiappApp.restaurant.home.title' },
        loadChildren: () => import('./restaurant/restaurant.module').then(m => m.RestaurantModule),
      },
      {
        path: 'client',
        data: { pageTitle: 'amidiappApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'pro-user',
        data: { pageTitle: 'amidiappApp.proUser.home.title' },
        loadChildren: () => import('./pro-user/pro-user.module').then(m => m.ProUserModule),
      },
      {
        path: 'picture',
        data: { pageTitle: 'amidiappApp.picture.home.title' },
        loadChildren: () => import('./picture/picture.module').then(m => m.PictureModule),
      },
      {
        path: 'dish',
        data: { pageTitle: 'amidiappApp.dish.home.title' },
        loadChildren: () => import('./dish/dish.module').then(m => m.DishModule),
      },
      {
        path: 'dish-tag',
        data: { pageTitle: 'amidiappApp.dishTag.home.title' },
        loadChildren: () => import('./dish-tag/dish-tag.module').then(m => m.DishTagModule),
      },
      {
        path: 'location',
        data: { pageTitle: 'amidiappApp.location.home.title' },
        loadChildren: () => import('./location/location.module').then(m => m.LocationModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
