import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IHedgeprod } from '../hedgeprod.model';
import { HedgeprodService } from '../service/hedgeprod.service';
import { HedgeprodDeleteDialogComponent } from '../delete/hedgeprod-delete-dialog.component';

@Component({
  selector: 'jhi-hedgeprod',
  templateUrl: './hedgeprod.component.html',
})
export class HedgeprodComponent implements OnInit {
  hedgeprods?: IHedgeprod[];
  isLoading = false;
  currentSearch: string;

  constructor(protected hedgeprodService: HedgeprodService, protected modalService: NgbModal, protected activatedRoute: ActivatedRoute) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.hedgeprodService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IHedgeprod[]>) => {
            this.isLoading = false;
            this.hedgeprods = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.hedgeprodService.query().subscribe(
      (res: HttpResponse<IHedgeprod[]>) => {
        this.isLoading = false;
        this.hedgeprods = res.body ?? [];
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

  trackId(index: number, item: IHedgeprod): number {
    return item.id!;
  }

  delete(hedgeprod: IHedgeprod): void {
    const modalRef = this.modalService.open(HedgeprodDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.hedgeprod = hedgeprod;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
