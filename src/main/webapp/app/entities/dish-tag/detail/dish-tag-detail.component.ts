import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDishTag } from '../dish-tag.model';

@Component({
  selector: 'jhi-dish-tag-detail',
  templateUrl: './dish-tag-detail.component.html',
})
export class DishTagDetailComponent implements OnInit {
  dishTag: IDishTag | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ dishTag }) => {
      this.dishTag = dishTag;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
