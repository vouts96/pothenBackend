import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGrade } from '../grade.model';
import { GradeService } from '../service/grade.service';

const gradeResolve = (route: ActivatedRouteSnapshot): Observable<null | IGrade> => {
  const id = route.params.id;
  if (id) {
    return inject(GradeService)
      .find(id)
      .pipe(
        mergeMap((grade: HttpResponse<IGrade>) => {
          if (grade.body) {
            return of(grade.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default gradeResolve;
