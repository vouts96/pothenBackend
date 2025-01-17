import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IGrade } from '../grade.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../grade.test-samples';

import { GradeService } from './grade.service';

const requireRestSample: IGrade = {
  ...sampleWithRequiredData,
};

describe('Grade Service', () => {
  let service: GradeService;
  let httpMock: HttpTestingController;
  let expectedResult: IGrade | IGrade[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(GradeService);
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

    it('should create a Grade', () => {
      const grade = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(grade).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Grade', () => {
      const grade = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(grade).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Grade', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Grade', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Grade', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addGradeToCollectionIfMissing', () => {
      it('should add a Grade to an empty array', () => {
        const grade: IGrade = sampleWithRequiredData;
        expectedResult = service.addGradeToCollectionIfMissing([], grade);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(grade);
      });

      it('should not add a Grade to an array that contains it', () => {
        const grade: IGrade = sampleWithRequiredData;
        const gradeCollection: IGrade[] = [
          {
            ...grade,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addGradeToCollectionIfMissing(gradeCollection, grade);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Grade to an array that doesn't contain it", () => {
        const grade: IGrade = sampleWithRequiredData;
        const gradeCollection: IGrade[] = [sampleWithPartialData];
        expectedResult = service.addGradeToCollectionIfMissing(gradeCollection, grade);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(grade);
      });

      it('should add only unique Grade to an array', () => {
        const gradeArray: IGrade[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const gradeCollection: IGrade[] = [sampleWithRequiredData];
        expectedResult = service.addGradeToCollectionIfMissing(gradeCollection, ...gradeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const grade: IGrade = sampleWithRequiredData;
        const grade2: IGrade = sampleWithPartialData;
        expectedResult = service.addGradeToCollectionIfMissing([], grade, grade2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(grade);
        expect(expectedResult).toContain(grade2);
      });

      it('should accept null and undefined values', () => {
        const grade: IGrade = sampleWithRequiredData;
        expectedResult = service.addGradeToCollectionIfMissing([], null, grade, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(grade);
      });

      it('should return initial array if no Grade is added', () => {
        const gradeCollection: IGrade[] = [sampleWithRequiredData];
        expectedResult = service.addGradeToCollectionIfMissing(gradeCollection, undefined, null);
        expect(expectedResult).toEqual(gradeCollection);
      });
    });

    describe('compareGrade', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareGrade(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareGrade(entity1, entity2);
        const compareResult2 = service.compareGrade(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareGrade(entity1, entity2);
        const compareResult2 = service.compareGrade(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareGrade(entity1, entity2);
        const compareResult2 = service.compareGrade(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
