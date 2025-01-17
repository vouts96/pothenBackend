import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICommittee } from '../committee.model';
import { CommitteeService } from '../service/committee.service';

const committeeResolve = (route: ActivatedRouteSnapshot): Observable<null | ICommittee> => {
  const id = route.params.id;
  if (id) {
    return inject(CommitteeService)
      .find(id)
      .pipe(
        mergeMap((committee: HttpResponse<ICommittee>) => {
          if (committee.body) {
            return of(committee.body);
          }
          inject(Router).navigate(['404']);
          return EMPTY;
        }),
      );
  }
  return of(null);
};

export default committeeResolve;
