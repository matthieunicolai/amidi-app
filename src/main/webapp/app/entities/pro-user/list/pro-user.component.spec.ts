jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProUserService } from '../service/pro-user.service';

import { ProUserComponent } from './pro-user.component';

describe('ProUser Management Component', () => {
  let comp: ProUserComponent;
  let fixture: ComponentFixture<ProUserComponent>;
  let service: ProUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ProUserComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { queryParams: {} } },
        },
      ],
    })
      .overrideTemplate(ProUserComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProUserComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ProUserService);

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
    expect(comp.proUsers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
