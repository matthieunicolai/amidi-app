jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PictureService } from '../service/picture.service';
import { IPicture, Picture } from '../picture.model';
import { IRestaurant } from 'app/entities/restaurant/restaurant.model';
import { RestaurantService } from 'app/entities/restaurant/service/restaurant.service';

import { PictureUpdateComponent } from './picture-update.component';

describe('Picture Management Update Component', () => {
  let comp: PictureUpdateComponent;
  let fixture: ComponentFixture<PictureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let pictureService: PictureService;
  let restaurantService: RestaurantService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [PictureUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(PictureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PictureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    pictureService = TestBed.inject(PictureService);
    restaurantService = TestBed.inject(RestaurantService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Restaurant query and add missing value', () => {
      const picture: IPicture = { id: 456 };
      const restaurant: IRestaurant = { id: 88588 };
      picture.restaurant = restaurant;

      const restaurantCollection: IRestaurant[] = [{ id: 76947 }];
      jest.spyOn(restaurantService, 'query').mockReturnValue(of(new HttpResponse({ body: restaurantCollection })));
      const additionalRestaurants = [restaurant];
      const expectedCollection: IRestaurant[] = [...additionalRestaurants, ...restaurantCollection];
      jest.spyOn(restaurantService, 'addRestaurantToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ picture });
      comp.ngOnInit();

      expect(restaurantService.query).toHaveBeenCalled();
      expect(restaurantService.addRestaurantToCollectionIfMissing).toHaveBeenCalledWith(restaurantCollection, ...additionalRestaurants);
      expect(comp.restaurantsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const picture: IPicture = { id: 456 };
      const restaurant: IRestaurant = { id: 46030 };
      picture.restaurant = restaurant;

      activatedRoute.data = of({ picture });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(picture));
      expect(comp.restaurantsSharedCollection).toContain(restaurant);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Picture>>();
      const picture = { id: 123 };
      jest.spyOn(pictureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ picture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: picture }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(pictureService.update).toHaveBeenCalledWith(picture);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Picture>>();
      const picture = new Picture();
      jest.spyOn(pictureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ picture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: picture }));
      saveSubject.complete();

      // THEN
      expect(pictureService.create).toHaveBeenCalledWith(picture);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<Picture>>();
      const picture = { id: 123 };
      jest.spyOn(pictureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ picture });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(pictureService.update).toHaveBeenCalledWith(picture);
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
