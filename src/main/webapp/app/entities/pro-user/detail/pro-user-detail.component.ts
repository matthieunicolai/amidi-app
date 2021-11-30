import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IProUser } from '../pro-user.model';

@Component({
  selector: 'jhi-pro-user-detail',
  templateUrl: './pro-user-detail.component.html',
})
export class ProUserDetailComponent implements OnInit {
  proUser: IProUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proUser }) => {
      this.proUser = proUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
