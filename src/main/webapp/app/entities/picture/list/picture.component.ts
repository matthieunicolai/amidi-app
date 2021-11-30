import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPicture } from '../picture.model';
import { PictureService } from '../service/picture.service';
import { PictureDeleteDialogComponent } from '../delete/picture-delete-dialog.component';

@Component({
  selector: 'jhi-picture',
  templateUrl: './picture.component.html',
})
export class PictureComponent implements OnInit {
  pictures?: IPicture[];
  isLoading = false;
  currentSearch: string;

  constructor(protected pictureService: PictureService, protected modalService: NgbModal, protected activatedRoute: ActivatedRoute) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.pictureService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IPicture[]>) => {
            this.isLoading = false;
            this.pictures = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.pictureService.query().subscribe(
      (res: HttpResponse<IPicture[]>) => {
        this.isLoading = false;
        this.pictures = res.body ?? [];
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

  trackId(index: number, item: IPicture): number {
    return item.id!;
  }

  delete(picture: IPicture): void {
    const modalRef = this.modalService.open(PictureDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.picture = picture;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
