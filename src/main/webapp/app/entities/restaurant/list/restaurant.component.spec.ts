jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { RestaurantService } from '../service/restaurant.service';

import { RestaurantComponent } from './restaurant.component';

describe('Restaurant Management Component', () => {
  let comp: RestaurantComponent;
  let fixture: ComponentFixture<RestaurantComponent>;
  let service: RestaurantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [RestaurantComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { queryParams: {} } },
        },
      ],
    })
      .overrideTemplate(RestaurantComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(RestaurantComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(RestaurantService);

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
    expect(comp.restaurants?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
