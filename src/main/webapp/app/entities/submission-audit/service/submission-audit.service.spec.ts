import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISubmissionAudit } from '../submission-audit.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../submission-audit.test-samples';

import { RestSubmissionAudit, SubmissionAuditService } from './submission-audit.service';

const requireRestSample: RestSubmissionAudit = {
  ...sampleWithRequiredData,
  acquisitionDate: sampleWithRequiredData.acquisitionDate?.format(DATE_FORMAT),
  lossDate: sampleWithRequiredData.lossDate?.format(DATE_FORMAT),
  decisionDate: sampleWithRequiredData.decisionDate?.format(DATE_FORMAT),
  modifiedDate: sampleWithRequiredData.modifiedDate?.toJSON(),
};

describe('SubmissionAudit Service', () => {
  let service: SubmissionAuditService;
  let httpMock: HttpTestingController;
  let expectedResult: ISubmissionAudit | ISubmissionAudit[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SubmissionAuditService);
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

    it('should create a SubmissionAudit', () => {
      const submissionAudit = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(submissionAudit).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a SubmissionAudit', () => {
      const submissionAudit = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(submissionAudit).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a SubmissionAudit', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of SubmissionAudit', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a SubmissionAudit', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSubmissionAuditToCollectionIfMissing', () => {
      it('should add a SubmissionAudit to an empty array', () => {
        const submissionAudit: ISubmissionAudit = sampleWithRequiredData;
        expectedResult = service.addSubmissionAuditToCollectionIfMissing([], submissionAudit);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(submissionAudit);
      });

      it('should not add a SubmissionAudit to an array that contains it', () => {
        const submissionAudit: ISubmissionAudit = sampleWithRequiredData;
        const submissionAuditCollection: ISubmissionAudit[] = [
          {
            ...submissionAudit,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSubmissionAuditToCollectionIfMissing(submissionAuditCollection, submissionAudit);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a SubmissionAudit to an array that doesn't contain it", () => {
        const submissionAudit: ISubmissionAudit = sampleWithRequiredData;
        const submissionAuditCollection: ISubmissionAudit[] = [sampleWithPartialData];
        expectedResult = service.addSubmissionAuditToCollectionIfMissing(submissionAuditCollection, submissionAudit);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(submissionAudit);
      });

      it('should add only unique SubmissionAudit to an array', () => {
        const submissionAuditArray: ISubmissionAudit[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const submissionAuditCollection: ISubmissionAudit[] = [sampleWithRequiredData];
        expectedResult = service.addSubmissionAuditToCollectionIfMissing(submissionAuditCollection, ...submissionAuditArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const submissionAudit: ISubmissionAudit = sampleWithRequiredData;
        const submissionAudit2: ISubmissionAudit = sampleWithPartialData;
        expectedResult = service.addSubmissionAuditToCollectionIfMissing([], submissionAudit, submissionAudit2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(submissionAudit);
        expect(expectedResult).toContain(submissionAudit2);
      });

      it('should accept null and undefined values', () => {
        const submissionAudit: ISubmissionAudit = sampleWithRequiredData;
        expectedResult = service.addSubmissionAuditToCollectionIfMissing([], null, submissionAudit, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(submissionAudit);
      });

      it('should return initial array if no SubmissionAudit is added', () => {
        const submissionAuditCollection: ISubmissionAudit[] = [sampleWithRequiredData];
        expectedResult = service.addSubmissionAuditToCollectionIfMissing(submissionAuditCollection, undefined, null);
        expect(expectedResult).toEqual(submissionAuditCollection);
      });
    });

    describe('compareSubmissionAudit', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSubmissionAudit(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSubmissionAudit(entity1, entity2);
        const compareResult2 = service.compareSubmissionAudit(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSubmissionAudit(entity1, entity2);
        const compareResult2 = service.compareSubmissionAudit(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSubmissionAudit(entity1, entity2);
        const compareResult2 = service.compareSubmissionAudit(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
