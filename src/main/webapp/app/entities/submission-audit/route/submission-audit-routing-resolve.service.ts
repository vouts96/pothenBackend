import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISubmissionAudit } from '../submission-audit.model';
import { SubmissionAuditService } from '../service/submission-audit.service';

const submissionAuditResolve = (route: ActivatedRouteSnapshot): Observable<null | ISubmissionAudit> => {
  const id = route.params.id;
  if (id) {
    return inject(SubmissionAuditService)
      .find(id)
      .pipe(
        mergeMap((submissionAudit: HttpResponse<ISubmissionAudit>) => {
          if (submissionAudit.body) {
            return of(submissionAudit.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default submissionAuditResolve;
