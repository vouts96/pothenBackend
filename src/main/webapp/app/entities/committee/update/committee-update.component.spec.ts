import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse, provideHttpClient } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Subject, from, of } from 'rxjs';

import { CommitteeService } from '../service/committee.service';
import { ICommittee } from '../committee.model';
import { CommitteeFormService } from './committee-form.service';

import { CommitteeUpdateComponent } from './committee-update.component';

describe('Committee Management Update Component', () => {
  let comp: CommitteeUpdateComponent;
  let fixture: ComponentFixture<CommitteeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let committeeFormService: CommitteeFormService;
  let committeeService: CommitteeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CommitteeUpdateComponent],
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
      .overrideTemplate(CommitteeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(CommitteeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    committeeFormService = TestBed.inject(CommitteeFormService);
    committeeService = TestBed.inject(CommitteeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const committee: ICommittee = { id: 456 };

      activatedRoute.data = of({ committee });
      comp.ngOnInit();

      expect(comp.committee).toEqual(committee);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommittee>>();
      const committee = { id: 123 };
      jest.spyOn(committeeFormService, 'getCommittee').mockReturnValue(committee);
      jest.spyOn(committeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ committee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: committee }));
      saveSubject.complete();

      // THEN
      expect(committeeFormService.getCommittee).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(committeeService.update).toHaveBeenCalledWith(expect.objectContaining(committee));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommittee>>();
      const committee = { id: 123 };
      jest.spyOn(committeeFormService, 'getCommittee').mockReturnValue({ id: null });
      jest.spyOn(committeeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ committee: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: committee }));
      saveSubject.complete();

      // THEN
      expect(committeeFormService.getCommittee).toHaveBeenCalled();
      expect(committeeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ICommittee>>();
      const committee = { id: 123 };
      jest.spyOn(committeeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ committee });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(committeeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
