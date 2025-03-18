import { Routes } from '@angular/router';

const routes: Routes = [
  {
    path: 'authority',
    data: { pageTitle: 'myApp.adminAuthority.home.title' },
    loadChildren: () => import('./admin/authority/authority.routes'),
  },
  {
    path: 'submission',
    data: { pageTitle: 'myApp.submission.home.title' },
    loadChildren: () => import('./submission/submission.routes'),
  },
  {
    path: 'position',
    data: { pageTitle: 'myApp.position.home.title' },
    loadChildren: () => import('./position/position.routes'),
  },
  {
    path: 'grade',
    data: { pageTitle: 'myApp.grade.home.title' },
    loadChildren: () => import('./grade/grade.routes'),
  },
  {
    path: 'committee',
    data: { pageTitle: 'myApp.committee.home.title' },
    loadChildren: () => import('./committee/committee.routes'),
  },
  {
    path: 'submission-audit',
    data: { pageTitle: 'myApp.submissionAudit.home.title' },
    loadChildren: () => import('./submission-audit/submission-audit.routes'),
  },
  /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
];

export default routes;
