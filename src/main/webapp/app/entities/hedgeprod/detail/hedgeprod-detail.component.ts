import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHedgeprod } from '../hedgeprod.model';

@Component({
  selector: 'jhi-hedgeprod-detail',
  templateUrl: './hedgeprod-detail.component.html',
})
export class HedgeprodDetailComponent implements OnInit {
  hedgeprod: IHedgeprod | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ hedgeprod }) => {
      this.hedgeprod = hedgeprod;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
