import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ProUserComponent } from './list/pro-user.component';
import { ProUserDetailComponent } from './detail/pro-user-detail.component';
import { ProUserUpdateComponent } from './update/pro-user-update.component';
import { ProUserDeleteDialogComponent } from './delete/pro-user-delete-dialog.component';
import { ProUserRoutingModule } from './route/pro-user-routing.module';

@NgModule({
  imports: [SharedModule, ProUserRoutingModule],
  declarations: [ProUserComponent, ProUserDetailComponent, ProUserUpdateComponent, ProUserDeleteDialogComponent],
  entryComponents: [ProUserDeleteDialogComponent],
})
export class ProUserModule {}
