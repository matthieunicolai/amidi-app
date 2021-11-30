jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DishTagService } from '../service/dish-tag.service';

import { DishTagComponent } from './dish-tag.component';

describe('DishTag Management Component', () => {
  let comp: DishTagComponent;
  let fixture: ComponentFixture<DishTagComponent>;
  let service: DishTagService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DishTagComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { queryParams: {} } },
        },
      ],
    })
      .overrideTemplate(DishTagComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DishTagComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DishTagService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.dishTags?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
