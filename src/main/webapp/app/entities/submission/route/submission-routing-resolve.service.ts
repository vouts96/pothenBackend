import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISubmission } from '../submission.model';
import { SubmissionService } from '../service/submission.service';

const submissionResolve = (route: ActivatedRouteSnapshot): Observable<null | ISubmission> => {
  const id = route.params.id;
  if (id) {
    return inject(SubmissionService)
      .find(id)
      .pipe(
        mergeMap((submission: HttpResponse<ISubmission>) => {
          if (submission.body) {
            return of(submission.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default submissionResolve;
