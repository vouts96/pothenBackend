import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { ICommittee } from '../committee.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../committee.test-samples';

import { CommitteeService } from './committee.service';

const requireRestSample: ICommittee = {
  ...sampleWithRequiredData,
};

describe('Committee Service', () => {
  let service: CommitteeService;
  let httpMock: HttpTestingController;
  let expectedResult: ICommittee | ICommittee[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(CommitteeService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Committee', () => {
      const committee = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(committee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Committee', () => {
      const committee = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(committee).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Committee', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Committee', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Committee', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addCommitteeToCollectionIfMissing', () => {
      it('should add a Committee to an empty array', () => {
        const committee: ICommittee = sampleWithRequiredData;
        expectedResult = service.addCommitteeToCollectionIfMissing([], committee);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(committee);
      });

      it('should not add a Committee to an array that contains it', () => {
        const committee: ICommittee = sampleWithRequiredData;
        const committeeCollection: ICommittee[] = [
          {
            ...committee,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addCommitteeToCollectionIfMissing(committeeCollection, committee);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Committee to an array that doesn't contain it", () => {
        const committee: ICommittee = sampleWithRequiredData;
        const committeeCollection: ICommittee[] = [sampleWithPartialData];
        expectedResult = service.addCommitteeToCollectionIfMissing(committeeCollection, committee);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(committee);
      });

      it('should add only unique Committee to an array', () => {
        const committeeArray: ICommittee[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const committeeCollection: ICommittee[] = [sampleWithRequiredData];
        expectedResult = service.addCommitteeToCollectionIfMissing(committeeCollection, ...committeeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const committee: ICommittee = sampleWithRequiredData;
        const committee2: ICommittee = sampleWithPartialData;
        expectedResult = service.addCommitteeToCollectionIfMissing([], committee, committee2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(committee);
        expect(expectedResult).toContain(committee2);
      });

      it('should accept null and undefined values', () => {
        const committee: ICommittee = sampleWithRequiredData;
        expectedResult = service.addCommitteeToCollectionIfMissing([], null, committee, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(committee);
      });

      it('should return initial array if no Committee is added', () => {
        const committeeCollection: ICommittee[] = [sampleWithRequiredData];
        expectedResult = service.addCommitteeToCollectionIfMissing(committeeCollection, undefined, null);
        expect(expectedResult).toEqual(committeeCollection);
      });
    });

    describe('compareCommittee', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareCommittee(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareCommittee(entity1, entity2);
        const compareResult2 = service.compareCommittee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareCommittee(entity1, entity2);
        const compareResult2 = service.compareCommittee(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareCommittee(entity1, entity2);
        const compareResult2 = service.compareCommittee(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
