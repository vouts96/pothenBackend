import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SubmissionResolve from './route/submission-routing-resolve.service';

const submissionRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/submission.component').then(m => m.SubmissionComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/submission-detail.component').then(m => m.SubmissionDetailComponent),
    resolve: {
      submission: SubmissionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/submission-update.component').then(m => m.SubmissionUpdateComponent),
    resolve: {
      submission: SubmissionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/submission-update.component').then(m => m.SubmissionUpdateComponent),
    resolve: {
      submission: SubmissionResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default submissionRoute;
