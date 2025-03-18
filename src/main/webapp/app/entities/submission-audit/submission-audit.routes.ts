import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import SubmissionAuditResolve from './route/submission-audit-routing-resolve.service';

const submissionAuditRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/submission-audit.component').then(m => m.SubmissionAuditComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/submission-audit-detail.component').then(m => m.SubmissionAuditDetailComponent),
    resolve: {
      submissionAudit: SubmissionAuditResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/submission-audit-update.component').then(m => m.SubmissionAuditUpdateComponent),
    resolve: {
      submissionAudit: SubmissionAuditResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/submission-audit-update.component').then(m => m.SubmissionAuditUpdateComponent),
    resolve: {
      submissionAudit: SubmissionAuditResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default submissionAuditRoute;
