jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IHedgeprod, Hedgeprod } from '../hedgeprod.model';
import { HedgeprodService } from '../service/hedgeprod.service';

import { HedgeprodRoutingResolveService } from './hedgeprod-routing-resolve.service';

describe('Hedgeprod routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: HedgeprodRoutingResolveService;
  let service: HedgeprodService;
  let resultHedgeprod: IHedgeprod | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(HedgeprodRoutingResolveService);
    service = TestBed.inject(HedgeprodService);
    resultHedgeprod = undefined;
  });

  describe('resolve', () => {
    it('should return IHedgeprod returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHedgeprod = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultHedgeprod).toEqual({ id: 123 });
    });

    it('should return new IHedgeprod if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHedgeprod = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultHedgeprod).toEqual(new Hedgeprod());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Hedgeprod })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultHedgeprod = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultHedgeprod).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
