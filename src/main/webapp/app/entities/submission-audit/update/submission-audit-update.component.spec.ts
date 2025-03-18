import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { ISubmission } from 'app/entities/submission/submission.model';
import { SubmissionService } from 'app/entities/submission/service/submission.service';
import { SubmissionAuditService } from '../service/submission-audit.service';
import { ISubmissionAudit } from '../submission-audit.model';
import { SubmissionAuditFormService } from './submission-audit-form.service';

import { SubmissionAuditUpdateComponent } from './submission-audit-update.component';

describe('SubmissionAudit Management Update Component', () => {
  let comp: SubmissionAuditUpdateComponent;
  let fixture: ComponentFixture<SubmissionAuditUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let submissionAuditFormService: SubmissionAuditFormService;
  let submissionAuditService: SubmissionAuditService;
  let submissionService: SubmissionService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [SubmissionAuditUpdateComponent],
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
      .overrideTemplate(SubmissionAuditUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SubmissionAuditUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    submissionAuditFormService = TestBed.inject(SubmissionAuditFormService);
    submissionAuditService = TestBed.inject(SubmissionAuditService);
    submissionService = TestBed.inject(SubmissionService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Submission query and add missing value', () => {
      const submissionAudit: ISubmissionAudit = { id: 456 };
      const originalSubmission: ISubmission = { id: 6747 };
      submissionAudit.originalSubmission = originalSubmission;

      const submissionCollection: ISubmission[] = [{ id: 29777 }];
      jest.spyOn(submissionService, 'query').mockReturnValue(of(new HttpResponse({ body: submissionCollection })));
      const additionalSubmissions = [originalSubmission];
      const expectedCollection: ISubmission[] = [...additionalSubmissions, ...submissionCollection];
      jest.spyOn(submissionService, 'addSubmissionToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ submissionAudit });
      comp.ngOnInit();

      expect(submissionService.query).toHaveBeenCalled();
      expect(submissionService.addSubmissionToCollectionIfMissing).toHaveBeenCalledWith(
        submissionCollection,
        ...additionalSubmissions.map(expect.objectContaining),
      );
      expect(comp.submissionsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const submissionAudit: ISubmissionAudit = { id: 456 };
      const originalSubmission: ISubmission = { id: 14214 };
      submissionAudit.originalSubmission = originalSubmission;

      activatedRoute.data = of({ submissionAudit });
      comp.ngOnInit();

      expect(comp.submissionsSharedCollection).toContain(originalSubmission);
      expect(comp.submissionAudit).toEqual(submissionAudit);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubmissionAudit>>();
      const submissionAudit = { id: 123 };
      jest.spyOn(submissionAuditFormService, 'getSubmissionAudit').mockReturnValue(submissionAudit);
      jest.spyOn(submissionAuditService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ submissionAudit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: submissionAudit }));
      saveSubject.complete();

      // THEN
      expect(submissionAuditFormService.getSubmissionAudit).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(submissionAuditService.update).toHaveBeenCalledWith(expect.objectContaining(submissionAudit));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubmissionAudit>>();
      const submissionAudit = { id: 123 };
      jest.spyOn(submissionAuditFormService, 'getSubmissionAudit').mockReturnValue({ id: null });
      jest.spyOn(submissionAuditService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ submissionAudit: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: submissionAudit }));
      saveSubject.complete();

      // THEN
      expect(submissionAuditFormService.getSubmissionAudit).toHaveBeenCalled();
      expect(submissionAuditService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISubmissionAudit>>();
      const submissionAudit = { id: 123 };
      jest.spyOn(submissionAuditService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ submissionAudit });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(submissionAuditService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSubmission', () => {
      it('Should forward to submissionService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(submissionService, 'compareSubmission');
        comp.compareSubmission(entity, entity2);
        expect(submissionService.compareSubmission).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
