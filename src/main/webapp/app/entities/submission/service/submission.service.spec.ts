import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { ISubmission } from '../submission.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../submission.test-samples';

import { RestSubmission, SubmissionService } from './submission.service';

const requireRestSample: RestSubmission = {
  ...sampleWithRequiredData,
  acquisitionDate: sampleWithRequiredData.acquisitionDate?.format(DATE_FORMAT),
  lossDate: sampleWithRequiredData.lossDate?.format(DATE_FORMAT),
  decisionDate: sampleWithRequiredData.decisionDate?.format(DATE_FORMAT),
};

describe('Submission Service', () => {
  let service: SubmissionService;
  let httpMock: HttpTestingController;
  let expectedResult: ISubmission | ISubmission[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(SubmissionService);
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

    it('should create a Submission', () => {
      const submission = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(submission).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Submission', () => {
      const submission = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(submission).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Submission', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Submission', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Submission', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addSubmissionToCollectionIfMissing', () => {
      it('should add a Submission to an empty array', () => {
        const submission: ISubmission = sampleWithRequiredData;
        expectedResult = service.addSubmissionToCollectionIfMissing([], submission);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(submission);
      });

      it('should not add a Submission to an array that contains it', () => {
        const submission: ISubmission = sampleWithRequiredData;
        const submissionCollection: ISubmission[] = [
          {
            ...submission,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addSubmissionToCollectionIfMissing(submissionCollection, submission);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Submission to an array that doesn't contain it", () => {
        const submission: ISubmission = sampleWithRequiredData;
        const submissionCollection: ISubmission[] = [sampleWithPartialData];
        expectedResult = service.addSubmissionToCollectionIfMissing(submissionCollection, submission);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(submission);
      });

      it('should add only unique Submission to an array', () => {
        const submissionArray: ISubmission[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const submissionCollection: ISubmission[] = [sampleWithRequiredData];
        expectedResult = service.addSubmissionToCollectionIfMissing(submissionCollection, ...submissionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const submission: ISubmission = sampleWithRequiredData;
        const submission2: ISubmission = sampleWithPartialData;
        expectedResult = service.addSubmissionToCollectionIfMissing([], submission, submission2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(submission);
        expect(expectedResult).toContain(submission2);
      });

      it('should accept null and undefined values', () => {
        const submission: ISubmission = sampleWithRequiredData;
        expectedResult = service.addSubmissionToCollectionIfMissing([], null, submission, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(submission);
      });

      it('should return initial array if no Submission is added', () => {
        const submissionCollection: ISubmission[] = [sampleWithRequiredData];
        expectedResult = service.addSubmissionToCollectionIfMissing(submissionCollection, undefined, null);
        expect(expectedResult).toEqual(submissionCollection);
      });
    });

    describe('compareSubmission', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareSubmission(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareSubmission(entity1, entity2);
        const compareResult2 = service.compareSubmission(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareSubmission(entity1, entity2);
        const compareResult2 = service.compareSubmission(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareSubmission(entity1, entity2);
        const compareResult2 = service.compareSubmission(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
