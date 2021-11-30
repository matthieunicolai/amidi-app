import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { HedgeprodComponent } from './list/hedgeprod.component';
import { HedgeprodDetailComponent } from './detail/hedgeprod-detail.component';
import { HedgeprodUpdateComponent } from './update/hedgeprod-update.component';
import { HedgeprodDeleteDialogComponent } from './delete/hedgeprod-delete-dialog.component';
import { HedgeprodRoutingModule } from './route/hedgeprod-routing.module';

@NgModule({
  imports: [SharedModule, HedgeprodRoutingModule],
  declarations: [HedgeprodComponent, HedgeprodDetailComponent, HedgeprodUpdateComponent, HedgeprodDeleteDialogComponent],
  entryComponents: [HedgeprodDeleteDialogComponent],
})
export class HedgeprodModule {}
