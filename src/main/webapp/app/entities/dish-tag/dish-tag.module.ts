import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { DishTagComponent } from './list/dish-tag.component';
import { DishTagDetailComponent } from './detail/dish-tag-detail.component';
import { DishTagUpdateComponent } from './update/dish-tag-update.component';
import { DishTagDeleteDialogComponent } from './delete/dish-tag-delete-dialog.component';
import { DishTagRoutingModule } from './route/dish-tag-routing.module';

@NgModule({
  imports: [SharedModule, DishTagRoutingModule],
  declarations: [DishTagComponent, DishTagDetailComponent, DishTagUpdateComponent, DishTagDeleteDialogComponent],
  entryComponents: [DishTagDeleteDialogComponent],
})
export class DishTagModule {}
