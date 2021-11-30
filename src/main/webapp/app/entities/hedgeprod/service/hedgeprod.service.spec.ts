import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { HedgeprodRole } from 'app/entities/enumerations/hedgeprod-role.model';
import { IHedgeprod, Hedgeprod } from '../hedgeprod.model';

import { HedgeprodService } from './hedgeprod.service';

describe('Hedgeprod Service', () => {
  let service: HedgeprodService;
  let httpMock: HttpTestingController;
  let elemDefault: IHedgeprod;
  let expectedResult: IHedgeprod | IHedgeprod[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(HedgeprodService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      hName: 'AAAAAAA',
      hSurname: 'AAAAAAA',
      hRole: HedgeprodRole.ADMIN,
      hEmail: 'AAAAAAA',
      hPhoneNumber: 'AAAAAAA',
      isActivated: false,
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

    it('should create a Hedgeprod', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new Hedgeprod()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Hedgeprod', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          hName: 'BBBBBB',
          hSurname: 'BBBBBB',
          hRole: 'BBBBBB',
          hEmail: 'BBBBBB',
          hPhoneNumber: 'BBBBBB',
          isActivated: true,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Hedgeprod', () => {
      const patchObject = Object.assign(
        {
          hName: 'BBBBBB',
          hRole: 'BBBBBB',
          hEmail: 'BBBBBB',
          hPhoneNumber: 'BBBBBB',
        },
        new Hedgeprod()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Hedgeprod', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          hName: 'BBBBBB',
          hSurname: 'BBBBBB',
          hRole: 'BBBBBB',
          hEmail: 'BBBBBB',
          hPhoneNumber: 'BBBBBB',
          isActivated: true,
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

    it('should delete a Hedgeprod', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addHedgeprodToCollectionIfMissing', () => {
      it('should add a Hedgeprod to an empty array', () => {
        const hedgeprod: IHedgeprod = { id: 123 };
        expectedResult = service.addHedgeprodToCollectionIfMissing([], hedgeprod);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hedgeprod);
      });

      it('should not add a Hedgeprod to an array that contains it', () => {
        const hedgeprod: IHedgeprod = { id: 123 };
        const hedgeprodCollection: IHedgeprod[] = [
          {
            ...hedgeprod,
          },
          { id: 456 },
        ];
        expectedResult = service.addHedgeprodToCollectionIfMissing(hedgeprodCollection, hedgeprod);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Hedgeprod to an array that doesn't contain it", () => {
        const hedgeprod: IHedgeprod = { id: 123 };
        const hedgeprodCollection: IHedgeprod[] = [{ id: 456 }];
        expectedResult = service.addHedgeprodToCollectionIfMissing(hedgeprodCollection, hedgeprod);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hedgeprod);
      });

      it('should add only unique Hedgeprod to an array', () => {
        const hedgeprodArray: IHedgeprod[] = [{ id: 123 }, { id: 456 }, { id: 55641 }];
        const hedgeprodCollection: IHedgeprod[] = [{ id: 123 }];
        expectedResult = service.addHedgeprodToCollectionIfMissing(hedgeprodCollection, ...hedgeprodArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const hedgeprod: IHedgeprod = { id: 123 };
        const hedgeprod2: IHedgeprod = { id: 456 };
        expectedResult = service.addHedgeprodToCollectionIfMissing([], hedgeprod, hedgeprod2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(hedgeprod);
        expect(expectedResult).toContain(hedgeprod2);
      });

      it('should accept null and undefined values', () => {
        const hedgeprod: IHedgeprod = { id: 123 };
        expectedResult = service.addHedgeprodToCollectionIfMissing([], null, hedgeprod, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(hedgeprod);
      });

      it('should return initial array if no Hedgeprod is added', () => {
        const hedgeprodCollection: IHedgeprod[] = [{ id: 123 }];
        expectedResult = service.addHedgeprodToCollectionIfMissing(hedgeprodCollection, undefined, null);
        expect(expectedResult).toEqual(hedgeprodCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
