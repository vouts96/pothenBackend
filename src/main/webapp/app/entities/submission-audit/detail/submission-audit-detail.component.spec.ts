import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness } from '@angular/router/testing';
import { of } from 'rxjs';

import { SubmissionAuditDetailComponent } from './submission-audit-detail.component';

describe('SubmissionAudit Management Detail Component', () => {
  let comp: SubmissionAuditDetailComponent;
  let fixture: ComponentFixture<SubmissionAuditDetailComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubmissionAuditDetailComponent],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              loadComponent: () => import('./submission-audit-detail.component').then(m => m.SubmissionAuditDetailComponent),
              resolve: { submissionAudit: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(SubmissionAuditDetailComponent, '')
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SubmissionAuditDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load submissionAudit on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', SubmissionAuditDetailComponent);

      // THEN
      expect(instance.submissionAudit()).toEqual(expect.objectContaining({ id: 123 }));
    });
  });

  describe('PreviousState', () => {
    it('Should navigate to previous state', () => {
      jest.spyOn(window.history, 'back');
      comp.previousState();
      expect(window.history.back).toHaveBeenCalled();
    });
  });
});
