jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { DishTagService } from '../service/dish-tag.service';
import { IDishTag, DishTag } from '../dish-tag.model';
import { IDish } from 'app/entities/dish/dish.model';
import { DishService } from 'app/entities/dish/service/dish.service';

import { DishTagUpdateComponent } from './dish-tag-update.component';

describe('DishTag Management Update Component', () => {
  let comp: DishTagUpdateComponent;
  let fixture: ComponentFixture<DishTagUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let dishTagService: DishTagService;
  let dishService: DishService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DishTagUpdateComponent],
      providers: [FormBuilder, ActivatedRoute],
    })
      .overrideTemplate(DishTagUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DishTagUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    dishTagService = TestBed.inject(DishTagService);
    dishService = TestBed.inject(DishService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Dish query and add missing value', () => {
      const dishTag: IDishTag = { id: 456 };
      const dish: IDish = { id: 79409 };
      dishTag.dish = dish;

      const dishCollection: IDish[] = [{ id: 37793 }];
      jest.spyOn(dishService, 'query').mockReturnValue(of(new HttpResponse({ body: dishCollection })));
      const additionalDishes = [dish];
      const expectedCollection: IDish[] = [...additionalDishes, ...dishCollection];
      jest.spyOn(dishService, 'addDishToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ dishTag });
      comp.ngOnInit();

      expect(dishService.query).toHaveBeenCalled();
      expect(dishService.addDishToCollectionIfMissing).toHaveBeenCalledWith(dishCollection, ...additionalDishes);
      expect(comp.dishesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const dishTag: IDishTag = { id: 456 };
      const dish: IDish = { id: 42919 };
      dishTag.dish = dish;

      activatedRoute.data = of({ dishTag });
      comp.ngOnInit();

      expect(comp.editForm.value).toEqual(expect.objectContaining(dishTag));
      expect(comp.dishesSharedCollection).toContain(dish);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DishTag>>();
      const dishTag = { id: 123 };
      jest.spyOn(dishTagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dishTag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dishTag }));
      saveSubject.complete();

      // THEN
      expect(comp.previousState).toHaveBeenCalled();
      expect(dishTagService.update).toHaveBeenCalledWith(dishTag);
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DishTag>>();
      const dishTag = new DishTag();
      jest.spyOn(dishTagService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dishTag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: dishTag }));
      saveSubject.complete();

      // THEN
      expect(dishTagService.create).toHaveBeenCalledWith(dishTag);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<DishTag>>();
      const dishTag = { id: 123 };
      jest.spyOn(dishTagService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ dishTag });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(dishTagService.update).toHaveBeenCalledWith(dishTag);
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Tracking relationships identifiers', () => {
    describe('trackDishById', () => {
      it('Should return tracked Dish primary key', () => {
        const entity = { id: 123 };
        const trackResult = comp.trackDishById(0, entity);
        expect(trackResult).toEqual(entity.id);
      });
    });
  });
});
