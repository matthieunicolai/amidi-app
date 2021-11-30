import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IDish, Dish } from '../dish.model';

import { DishService } from './dish.service';

describe('Dish Service', () => {
  let service: DishService;
  let httpMock: HttpTestingController;
  let elemDefault: IDish;
  let expectedResult: IDish | IDish[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DishService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      dishName: 'AAAAAAA',
      dishDescription: 'AAAAAAA',
      dishPrice: 0,
      dishDate: currentDate,
      dishPictureName: 'AAAAAAA',
      dishPictureUrl: 'AAAAAAA',
      dishPictureAlt: 'AAAAAAA',
      isDailyDish: false,
      isAvailable: false,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          dishDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Dish', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          dishDate: currentDate.format(DATE_TIME_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dishDate: currentDate,
        },
        returnedFromService
      );

      service.create(new Dish()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Dish', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dishName: 'BBBBBB',
          dishDescription: 'BBBBBB',
          dishPrice: 1,
          dishDate: currentDate.format(DATE_TIME_FORMAT),
          dishPictureName: 'BBBBBB',
          dishPictureUrl: 'BBBBBB',
          dishPictureAlt: 'BBBBBB',
          isDailyDish: true,
          isAvailable: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dishDate: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Dish', () => {
      const patchObject = Object.assign(
        {
          dishPrice: 1,
          dishDate: currentDate.format(DATE_TIME_FORMAT),
          dishPictureAlt: 'BBBBBB',
          isDailyDish: true,
          isAvailable: true,
        },
        new Dish()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          dishDate: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Dish', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dishName: 'BBBBBB',
          dishDescription: 'BBBBBB',
          dishPrice: 1,
          dishDate: currentDate.format(DATE_TIME_FORMAT),
          dishPictureName: 'BBBBBB',
          dishPictureUrl: 'BBBBBB',
          dishPictureAlt: 'BBBBBB',
          isDailyDish: true,
          isAvailable: true,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          dishDate: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Dish', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDishToCollectionIfMissing', () => {
      it('should add a Dish to an empty array', () => {
        const dish: IDish = { id: 123 };
        expectedResult = service.addDishToCollectionIfMissing([], dish);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dish);
      });

      it('should not add a Dish to an array that contains it', () => {
        const dish: IDish = { id: 123 };
        const dishCollection: IDish[] = [
          {
            ...dish,
          },
          { id: 456 },
        ];
        expectedResult = service.addDishToCollectionIfMissing(dishCollection, dish);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Dish to an array that doesn't contain it", () => {
        const dish: IDish = { id: 123 };
        const dishCollection: IDish[] = [{ id: 456 }];
        expectedResult = service.addDishToCollectionIfMissing(dishCollection, dish);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dish);
      });

      it('should add only unique Dish to an array', () => {
        const dishArray: IDish[] = [{ id: 123 }, { id: 456 }, { id: 14513 }];
        const dishCollection: IDish[] = [{ id: 123 }];
        expectedResult = service.addDishToCollectionIfMissing(dishCollection, ...dishArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dish: IDish = { id: 123 };
        const dish2: IDish = { id: 456 };
        expectedResult = service.addDishToCollectionIfMissing([], dish, dish2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dish);
        expect(expectedResult).toContain(dish2);
      });

      it('should accept null and undefined values', () => {
        const dish: IDish = { id: 123 };
        expectedResult = service.addDishToCollectionIfMissing([], null, dish, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dish);
      });

      it('should return initial array if no Dish is added', () => {
        const dishCollection: IDish[] = [{ id: 123 }];
        expectedResult = service.addDishToCollectionIfMissing(dishCollection, undefined, null);
        expect(expectedResult).toEqual(dishCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
