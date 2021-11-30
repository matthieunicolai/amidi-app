jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProUserService } from '../service/pro-user.service';
import { IProUser, ProUser } from '../pro-user.model';
import { IRestaurant } from 'app/entities/restaurant/restaurant.model';
import { RestaurantService } from 'app/entities/restaurant/service/restaurant.service';

import { ProUserUpdateComponent } from './pro-user-update.component';

describe('ProUser Management Update Component', () => {
  let comp: ProUserUpdateComponent;
  let fixture: ComponentFixture<ProUserUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let proUserService: ProUserService;
  let restaurantService: RestaurantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ProUserUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(ProUserUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ProUserUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    proUserService = TestBed.inject(ProUserService);
    restaurantService = TestBed.inject(RestaurantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Restaurant query and add missing value', () => {
      const proUser: IProUser = { id: 456 };
      const restaurant: IRestaurant = { id: 12008 };
      proUser.restaurant = restaurant;
      const restaurant: IRestaurant = { id: 15504 };
      proUser.restaurant = restaurant;

      const restaurantCollection: IRestaurant[] = [{ id: 86928 }];
      jest.spyOn(restaurantService, 'query').mockReturnValue(of(new HttpResponse({ body: restaurantCollection })));
      const additionalRestaurants = [restaurant, restaurant];
      const expectedCollection: IRestaurant[] = [...additionalRestaurants, ...restaurantCollection];
      jest.spyOn(restaurantService, 'addRestaurantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ proUser });
      comp.ngOnInit();

      expect(restaurantService.query).toHaveBeenCalled();
      expect(restaurantService.addRestaurantToCollectionIfMissing).toHaveBeenCalledWith(restaurantCollection, ...additionalRestaurants);
      expect(comp.restaurantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const proUser: IProUser = { id: 456 };
      const restaurant: IRestaurant = { id: 63393 };
      proUser.restaurant = restaurant;
      const restaurant: IRestaurant = { id: 50072 };
      proUser.restaurant = restaurant;

      activatedRoute.data = of({ proUser });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(proUser));
      expect(comp.restaurantsSharedCollection).toContain(restaurant);
      expect(comp.restaurantsSharedCollection).toContain(restaurant);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProUser>>();
      const proUser = { id: 123 };
      jest.spyOn(proUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: proUser }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(proUserService.update).toHaveBeenCalledWith(proUser);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProUser>>();
      const proUser = new ProUser();
      jest.spyOn(proUserService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: proUser }));
      saveSubject.complete();

      // THEN
      expect(proUserService.create).toHaveBeenCalledWith(proUser);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ProUser>>();
      const proUser = { id: 123 };
      jest.spyOn(proUserService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ proUser });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(proUserService.update).toHaveBeenCalledWith(proUser);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackRestaurantById', () => {
      it('Should return tracked Restaurant primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackRestaurantById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
