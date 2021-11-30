import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { UserRole } from 'app/entities/enumerations/user-role.model';
import { IProUser, ProUser } from '../pro-user.model';

import { ProUserService } from './pro-user.service';

describe('ProUser Service', () => {
  let service: ProUserService;
  let httpMock: HttpTestingController;
  let elemDefault: IProUser;
  let expectedResult: IProUser | IProUser[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProUserService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      proUserName: 'AAAAAAA',
      proUserSurname: 'AAAAAAA',
      proUserRole: UserRole.OWNER,
      proUserLogin: 'AAAAAAA',
      proUserPwd: 'AAAAAAA',
      proUserEmail: 'AAAAAAA',
      proUserPhoneNumber: 'AAAAAAA',
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

    it('should create a ProUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new ProUser()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          proUserName: 'BBBBBB',
          proUserSurname: 'BBBBBB',
          proUserRole: 'BBBBBB',
          proUserLogin: 'BBBBBB',
          proUserPwd: 'BBBBBB',
          proUserEmail: 'BBBBBB',
          proUserPhoneNumber: 'BBBBBB',
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

    it('should partial update a ProUser', () => {
      const patchObject = Object.assign(
        {
          proUserName: 'BBBBBB',
          proUserPwd: 'BBBBBB',
          proUserEmail: 'BBBBBB',
          isActivated: true,
        },
        new ProUser()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProUser', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          proUserName: 'BBBBBB',
          proUserSurname: 'BBBBBB',
          proUserRole: 'BBBBBB',
          proUserLogin: 'BBBBBB',
          proUserPwd: 'BBBBBB',
          proUserEmail: 'BBBBBB',
          proUserPhoneNumber: 'BBBBBB',
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

    it('should delete a ProUser', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addProUserToCollectionIfMissing', () => {
      it('should add a ProUser to an empty array', () => {
        const proUser: IProUser = { id: 123 };
        expectedResult = service.addProUserToCollectionIfMissing([], proUser);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(proUser);
      });

      it('should not add a ProUser to an array that contains it', () => {
        const proUser: IProUser = { id: 123 };
        const proUserCollection: IProUser[] = [
          {
            ...proUser,
          },
          { id: 456 },
        ];
        expectedResult = service.addProUserToCollectionIfMissing(proUserCollection, proUser);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProUser to an array that doesn't contain it", () => {
        const proUser: IProUser = { id: 123 };
        const proUserCollection: IProUser[] = [{ id: 456 }];
        expectedResult = service.addProUserToCollectionIfMissing(proUserCollection, proUser);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(proUser);
      });

      it('should add only unique ProUser to an array', () => {
        const proUserArray: IProUser[] = [{ id: 123 }, { id: 456 }, { id: 51415 }];
        const proUserCollection: IProUser[] = [{ id: 123 }];
        expectedResult = service.addProUserToCollectionIfMissing(proUserCollection, ...proUserArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const proUser: IProUser = { id: 123 };
        const proUser2: IProUser = { id: 456 };
        expectedResult = service.addProUserToCollectionIfMissing([], proUser, proUser2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(proUser);
        expect(expectedResult).toContain(proUser2);
      });

      it('should accept null and undefined values', () => {
        const proUser: IProUser = { id: 123 };
        expectedResult = service.addProUserToCollectionIfMissing([], null, proUser, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(proUser);
      });

      it('should return initial array if no ProUser is added', () => {
        const proUserCollection: IProUser[] = [{ id: 123 }];
        expectedResult = service.addProUserToCollectionIfMissing(proUserCollection, undefined, null);
        expect(expectedResult).toEqual(proUserCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
