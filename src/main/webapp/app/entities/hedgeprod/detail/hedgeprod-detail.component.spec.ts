import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HedgeprodDetailComponent } from './hedgeprod-detail.component';

describe('Hedgeprod Management Detail Component', () => {
  let comp: HedgeprodDetailComponent;
  let fixture: ComponentFixture<HedgeprodDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [HedgeprodDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ hedgeprod: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(HedgeprodDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(HedgeprodDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load hedgeprod on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.hedgeprod).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
