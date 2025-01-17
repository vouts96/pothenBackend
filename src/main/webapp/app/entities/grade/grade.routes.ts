import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import GradeResolve from './route/grade-routing-resolve.service';

const gradeRoute: Routes = [
  {
    path: '',
    loadComponent: () => import('./list/grade.component').then(m => m.GradeComponent),
    data: {
      defaultSort: `id,${ASC}`,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    loadComponent: () => import('./detail/grade-detail.component').then(m => m.GradeDetailComponent),
    resolve: {
      grade: GradeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    loadComponent: () => import('./update/grade-update.component').then(m => m.GradeUpdateComponent),
    resolve: {
      grade: GradeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./update/grade-update.component').then(m => m.GradeUpdateComponent),
    resolve: {
      grade: GradeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default gradeRoute;
