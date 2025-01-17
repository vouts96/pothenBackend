import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { IPosition } from 'app/entities/position/position.model';
import { PositionService } from 'app/entities/position/service/position.service';
import { IGrade } from 'app/entities/grade/grade.model';
import { GradeService } from 'app/entities/grade/service/grade.service';
import { ICommittee } from 'app/entities/committee/committee.model';
import { CommitteeService } from 'app/entities/committee/service/committee.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/service/user.service';
import { ISubmission } from '../submission.model';
import { SubmissionService } from '../service/submission.service';
import { SubmissionFormService } from './submission-form.service';

import { SubmissionUpdateComponent } from './submission-update.component';

describe('Submission Management Update Component', () => {
  let comp: SubmissionUpdateComponent;
  let fixture: ComponentFixture<SubmissionUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let submissionFormService: SubmissionFormService;
  let submissionService: SubmissionService;
  let positionService: PositionService;
  let gradeService: GradeService;
  let committeeService: CommitteeService;
  let userService: UserService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SubmissionUpdateComponent],
      providers: [
        provideHttpClient(),
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(SubmissionUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SubmissionUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    submissionFormService = TestBed.inject(SubmissionFormService);
    submissionService = TestBed.inject(SubmissionService);
    positionService = TestBed.inject(PositionService);
    gradeService = TestBed.inject(GradeService);
    committeeService = TestBed.inject(CommitteeService);
    userService = TestBed.inject(UserService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Position query and add missing value', () => {
      const submission: ISubmission = { id: 456 };
      const position: IPosition = { id: 13891 };
      submission.position = position;

      const positionCollection: IPosition[] = [{ id: 6316 }];
      jest.spyOn(positionService, 'query').mockReturnValue(of(new HttpResponse({ body: positionCollection })));
      const additionalPositions = [position];
      const expectedCollection: IPosition[] = [...additionalPositions, ...positionCollection];
      jest.spyOn(positionService, 'addPositionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      expect(positionService.query).toHaveBeenCalled();
      expect(positionService.addPositionToCollectionIfMissing).toHaveBeenCalledWith(
        positionCollection,
        ...additionalPositions.map(expect.objectContaining),
      );
      expect(comp.positionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Grade query and add missing value', () => {
      const submission: ISubmission = { id: 456 };
      const grade: IGrade = { id: 13147 };
      submission.grade = grade;

      const gradeCollection: IGrade[] = [{ id: 1558 }];
      jest.spyOn(gradeService, 'query').mockReturnValue(of(new HttpResponse({ body: gradeCollection })));
      const additionalGrades = [grade];
      const expectedCollection: IGrade[] = [...additionalGrades, ...gradeCollection];
      jest.spyOn(gradeService, 'addGradeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      expect(gradeService.query).toHaveBeenCalled();
      expect(gradeService.addGradeToCollectionIfMissing).toHaveBeenCalledWith(
        gradeCollection,
        ...additionalGrades.map(expect.objectContaining),
      );
      expect(comp.gradesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Committee query and add missing value', () => {
      const submission: ISubmission = { id: 456 };
      const committeeName: ICommittee = { id: 26044 };
      submission.committeeName = committeeName;

      const committeeCollection: ICommittee[] = [{ id: 14928 }];
      jest.spyOn(committeeService, 'query').mockReturnValue(of(new HttpResponse({ body: committeeCollection })));
      const additionalCommittees = [committeeName];
      const expectedCollection: ICommittee[] = [...additionalCommittees, ...committeeCollection];
      jest.spyOn(committeeService, 'addCommitteeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      expect(committeeService.query).toHaveBeenCalled();
      expect(committeeService.addCommitteeToCollectionIfMissing).toHaveBeenCalledWith(
        committeeCollection,
        ...additionalCommittees.map(expect.objectContaining),
      );
      expect(comp.committeesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call User query and add missing value', () => {
      const submission: ISubmission = { id: 456 };
      const user: IUser = { id: 9691 };
      submission.user = user;

      const userCollection: IUser[] = [{ id: 25269 }];
      jest.spyOn(userService, 'query').mockReturnValue(of(new HttpResponse({ body: userCollection })));
      const additionalUsers = [user];
      const expectedCollection: IUser[] = [...additionalUsers, ...userCollection];
      jest.spyOn(userService, 'addUserToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      expect(userService.query).toHaveBeenCalled();
      expect(userService.addUserToCollectionIfMissing).toHaveBeenCalledWith(
        userCollection,
        ...additionalUsers.map(expect.objectContaining),
      );
      expect(comp.usersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const submission: ISubmission = { id: 456 };
      const position: IPosition = { id: 26511 };
      submission.position = position;
      const grade: IGrade = { id: 11313 };
      submission.grade = grade;
      const committeeName: ICommittee = { id: 31657 };
      submission.committeeName = committeeName;
      const user: IUser = { id: 13632 };
      submission.user = user;

      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      expect(comp.positionsSharedCollection).toContain(position);
      expect(comp.gradesSharedCollection).toContain(grade);
      expect(comp.committeesSharedCollection).toContain(committeeName);
      expect(comp.usersSharedCollection).toContain(user);
      expect(comp.submission).toEqual(submission);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubmission>>();
      const submission = { id: 123 };
      jest.spyOn(submissionFormService, 'getSubmission').mockReturnValue(submission);
      jest.spyOn(submissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: submission }));
      saveSubject.complete();

      // THEN
      expect(submissionFormService.getSubmission).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(submissionService.update).toHaveBeenCalledWith(expect.objectContaining(submission));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubmission>>();
      const submission = { id: 123 };
      jest.spyOn(submissionFormService, 'getSubmission').mockReturnValue({ id: null });
      jest.spyOn(submissionService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ submission: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: submission }));
      saveSubject.complete();

      // THEN
      expect(submissionFormService.getSubmission).toHaveBeenCalled();
      expect(submissionService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubmission>>();
      const submission = { id: 123 };
      jest.spyOn(submissionService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ submission });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(submissionService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('comparePosition', () => {
      it('Should forward to positionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(positionService, 'comparePosition');
        comp.comparePosition(entity, entity2);
        expect(positionService.comparePosition).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareGrade', () => {
      it('Should forward to gradeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(gradeService, 'compareGrade');
        comp.compareGrade(entity, entity2);
        expect(gradeService.compareGrade).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareCommittee', () => {
      it('Should forward to committeeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(committeeService, 'compareCommittee');
        comp.compareCommittee(entity, entity2);
        expect(committeeService.compareCommittee).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareUser', () => {
      it('Should forward to userService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(userService, 'compareUser');
        comp.compareUser(entity, entity2);
        expect(userService.compareUser).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
