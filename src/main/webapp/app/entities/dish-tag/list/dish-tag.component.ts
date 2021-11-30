import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDishTag } from '../dish-tag.model';
import { DishTagService } from '../service/dish-tag.service';
import { DishTagDeleteDialogComponent } from '../delete/dish-tag-delete-dialog.component';

@Component({
  selector: 'jhi-dish-tag',
  templateUrl: './dish-tag.component.html',
})
export class DishTagComponent implements OnInit {
  dishTags?: IDishTag[];
  isLoading = false;
  currentSearch: string;

  constructor(protected dishTagService: DishTagService, protected modalService: NgbModal, protected activatedRoute: ActivatedRoute) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.dishTagService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IDishTag[]>) => {
            this.isLoading = false;
            this.dishTags = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.dishTagService.query().subscribe(
      (res: HttpResponse<IDishTag[]>) => {
        this.isLoading = false;
        this.dishTags = res.body ?? [];
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

  trackId(index: number, item: IDishTag): number {
    return item.id!;
  }

  delete(dishTag: IDishTag): void {
    const modalRef = this.modalService.open(DishTagDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.dishTag = dishTag;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
