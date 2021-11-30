import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { FoodType } from 'app/entities/enumerations/food-type.model';
import { IDishTag, DishTag } from '../dish-tag.model';

import { DishTagService } from './dish-tag.service';

describe('DishTag Service', () => {
  let service: DishTagService;
  let httpMock: HttpTestingController;
  let elemDefault: IDishTag;
  let expectedResult: IDishTag | IDishTag[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(DishTagService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      dishTag: FoodType.VEGAN,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a DishTag', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new DishTag()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DishTag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dishTag: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DishTag', () => {
      const patchObject = Object.assign(
        {
          dishTag: 'BBBBBB',
        },
        new DishTag()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DishTag', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          dishTag: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a DishTag', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addDishTagToCollectionIfMissing', () => {
      it('should add a DishTag to an empty array', () => {
        const dishTag: IDishTag = { id: 123 };
        expectedResult = service.addDishTagToCollectionIfMissing([], dishTag);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dishTag);
      });

      it('should not add a DishTag to an array that contains it', () => {
        const dishTag: IDishTag = { id: 123 };
        const dishTagCollection: IDishTag[] = [
          {
            ...dishTag,
          },
          { id: 456 },
        ];
        expectedResult = service.addDishTagToCollectionIfMissing(dishTagCollection, dishTag);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DishTag to an array that doesn't contain it", () => {
        const dishTag: IDishTag = { id: 123 };
        const dishTagCollection: IDishTag[] = [{ id: 456 }];
        expectedResult = service.addDishTagToCollectionIfMissing(dishTagCollection, dishTag);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dishTag);
      });

      it('should add only unique DishTag to an array', () => {
        const dishTagArray: IDishTag[] = [{ id: 123 }, { id: 456 }, { id: 53797 }];
        const dishTagCollection: IDishTag[] = [{ id: 123 }];
        expectedResult = service.addDishTagToCollectionIfMissing(dishTagCollection, ...dishTagArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const dishTag: IDishTag = { id: 123 };
        const dishTag2: IDishTag = { id: 456 };
        expectedResult = service.addDishTagToCollectionIfMissing([], dishTag, dishTag2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(dishTag);
        expect(expectedResult).toContain(dishTag2);
      });

      it('should accept null and undefined values', () => {
        const dishTag: IDishTag = { id: 123 };
        expectedResult = service.addDishTagToCollectionIfMissing([], null, dishTag, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(dishTag);
      });

      it('should return initial array if no DishTag is added', () => {
        const dishTagCollection: IDishTag[] = [{ id: 123 }];
        expectedResult = service.addDishTagToCollectionIfMissing(dishTagCollection, undefined, null);
        expect(expectedResult).toEqual(dishTagCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
