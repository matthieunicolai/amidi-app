import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DishTagDetailComponent } from './dish-tag-detail.component';

describe('DishTag Management Detail Component', () => {
  let comp: DishTagDetailComponent;
  let fixture: ComponentFixture<DishTagDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [DishTagDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ dishTag: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(DishTagDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DishTagDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load dishTag on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.dishTag).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
