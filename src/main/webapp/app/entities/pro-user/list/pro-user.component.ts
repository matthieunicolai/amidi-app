import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProUser } from '../pro-user.model';
import { ProUserService } from '../service/pro-user.service';
import { ProUserDeleteDialogComponent } from '../delete/pro-user-delete-dialog.component';

@Component({
  selector: 'jhi-pro-user',
  templateUrl: './pro-user.component.html',
})
export class ProUserComponent implements OnInit {
  proUsers?: IProUser[];
  isLoading = false;
  currentSearch: string;

  constructor(protected proUserService: ProUserService, protected modalService: NgbModal, protected activatedRoute: ActivatedRoute) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.proUserService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IProUser[]>) => {
            this.isLoading = false;
            this.proUsers = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.proUserService.query().subscribe(
      (res: HttpResponse<IProUser[]>) => {
        this.isLoading = false;
        this.proUsers = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IProUser): number {
    return item.id!;
  }

  delete(proUser: IProUser): void {
    const modalRef = this.modalService.open(ProUserDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.proUser = proUser;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
