jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDishTag, DishTag } from '../dish-tag.model';
import { DishTagService } from '../service/dish-tag.service';

import { DishTagRoutingResolveService } from './dish-tag-routing-resolve.service';

describe('DishTag routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: DishTagRoutingResolveService;
  let service: DishTagService;
  let resultDishTag: IDishTag | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(DishTagRoutingResolveService);
    service = TestBed.inject(DishTagService);
    resultDishTag = undefined;
  });

  describe('resolve', () => {
    it('should return IDishTag returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDishTag = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDishTag).toEqual({ id: 123 });
    });

    it('should return new IDishTag if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDishTag = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultDishTag).toEqual(new DishTag());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as DishTag })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultDishTag = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultDishTag).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
