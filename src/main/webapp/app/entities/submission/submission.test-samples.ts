import dayjs from 'dayjs/esm';

import { ISubmission, NewSubmission } from './submission.model';

export const sampleWithRequiredData: ISubmission = {
  id: 21433,
  afm: '195096581',
  adt: 'tusk distinction coliseum',
  lastName: 'Waters',
  firstName: 'Mortimer',
  fatherName: 'linseed peaceful',
  acquisitionDate: dayjs('2025-01-16'),
  organizationUnit: 'casement cautious absentmindedly',
  protocolNumber: 'happy',
  decisionDate: dayjs('2025-01-16'),
  previousSubmission: true,
};

export const sampleWithPartialData: ISubmission = {
  id: 8505,
  afm: '747672687',
  adt: 'old-fashioned',
  lastName: 'Muller',
  firstName: 'Germaine',
  fatherName: 'whenever',
  acquisitionDate: dayjs('2025-01-17'),
  organizationUnit: 'er unlike',
  protocolNumber: 'attend',
  decisionDate: dayjs('2025-01-16'),
  previousSubmission: false,
};

export const sampleWithFullData: ISubmission = {
  id: 26381,
  afm: '546006053',
  adt: 'making drat oblong',
  lastName: 'Halvorson',
  firstName: 'Treva',
  fatherName: 'punctually barring',
  acquisitionDate: dayjs('2025-01-16'),
  lossDate: dayjs('2025-01-17'),
  organizationUnit: 'substitution',
  newOrganizationUnit: 'radiant glider now',
  protocolNumber: 'abaft helpless',
  decisionDate: dayjs('2025-01-17'),
  previousSubmission: false,
};

export const sampleWithNewData: NewSubmission = {
  afm: '295586497',
  adt: 'er yahoo so',
  lastName: 'Pfeffer',
  firstName: 'Kiana',
  fatherName: 'eventually',
  acquisitionDate: dayjs('2025-01-17'),
  organizationUnit: 'an calmly',
  protocolNumber: 'continually',
  decisionDate: dayjs('2025-01-17'),
  previousSubmission: false,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
