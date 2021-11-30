import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProUserDetailComponent } from './pro-user-detail.component';

describe('ProUser Management Detail Component', () => {
  let comp: ProUserDetailComponent;
  let fixture: ComponentFixture<ProUserDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProUserDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ proUser: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProUserDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProUserDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load proUser on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.proUser).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
