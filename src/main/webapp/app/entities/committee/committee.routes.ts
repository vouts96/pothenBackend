import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import CommitteeResolve from './route/committee-routing-resolve.service';

const committeeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/committee.component').then(m => m.CommitteeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/committee-detail.component').then(m => m.CommitteeDetailComponent),
    resolve: {
      committee: CommitteeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/committee-update.component').then(m => m.CommitteeUpdateComponent),
    resolve: {
      committee: CommitteeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/committee-update.component').then(m => m.CommitteeUpdateComponent),
    resolve: {
      committee: CommitteeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default committeeRoute;
