import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IRestaurant } from '../restaurant.model';
import { RestaurantService } from '../service/restaurant.service';
import { RestaurantDeleteDialogComponent } from '../delete/restaurant-delete-dialog.component';

@Component({
  selector: 'jhi-restaurant',
  templateUrl: './restaurant.component.html',
})
export class RestaurantComponent implements OnInit {
  restaurants?: IRestaurant[];
  isLoading = false;
  currentSearch: string;

  constructor(protected restaurantService: RestaurantService, protected modalService: NgbModal, protected activatedRoute: ActivatedRoute) {
    this.currentSearch = this.activatedRoute.snapshot.queryParams['search'] ?? '';
  }

  loadAll(): void {
    this.isLoading = true;
    if (this.currentSearch) {
      this.restaurantService
        .search({
          query: this.currentSearch,
        })
        .subscribe(
          (res: HttpResponse<IRestaurant[]>) => {
            this.isLoading = false;
            this.restaurants = res.body ?? [];
          },
          () => {
            this.isLoading = false;
          }
        );
      return;
    }

    this.restaurantService.query().subscribe(
      (res: HttpResponse<IRestaurant[]>) => {
        this.isLoading = false;
        this.restaurants = res.body ?? [];
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

  trackId(index: number, item: IRestaurant): number {
    return item.id!;
  }

  delete(restaurant: IRestaurant): void {
    const modalRef = this.modalService.open(RestaurantDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.restaurant = restaurant;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
