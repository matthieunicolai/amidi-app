jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { HedgeprodService } from '../service/hedgeprod.service';

import { HedgeprodComponent } from './hedgeprod.component';

describe('Hedgeprod Management Component', () => {
  let comp: HedgeprodComponent;
  let fixture: ComponentFixture<HedgeprodComponent>;
  let service: HedgeprodService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [HedgeprodComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { queryParams: {} } },
        },
      ],
    })
      .overrideTemplate(HedgeprodComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(HedgeprodComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(HedgeprodService);

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
    expect(comp.hedgeprods?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
