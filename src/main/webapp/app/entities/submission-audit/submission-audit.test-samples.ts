import dayjs from 'dayjs/esm';

import { ISubmissionAudit, NewSubmissionAudit } from './submission-audit.model';

export const sampleWithRequiredData: ISubmissionAudit = {
  id: 13990,
  afm: '867580934',
  adt: 'and',
  lastName: 'Balistreri',
  firstName: 'Maymie',
  fatherName: 'even supposing foolishly',
  acquisitionDate: dayjs('2025-03-18'),
  organizationUnit: 'stoop er deprave',
  protocolNumber: 'under sophisticated',
  decisionDate: dayjs('2025-03-18'),
  previousSubmission: false,
  modifiedDate: dayjs('2025-03-18T04:20'),
  modifiedBy: 'snoopy beneath garrote',
  changeType: 'because',
};

export const sampleWithPartialData: ISubmissionAudit = {
  id: 2473,
  afm: '003916822',
  adt: 'hard-to-find zowie',
  lastName: 'Grimes',
  firstName: 'Eveline',
  fatherName: 'singe',
  acquisitionDate: dayjs('2025-03-17'),
  lossDate: dayjs('2025-03-17'),
  organizationUnit: 'spring grouper',
  protocolNumber: 'where',
  decisionDate: dayjs('2025-03-17'),
  previousSubmission: true,
  modifiedDate: dayjs('2025-03-17T14:29'),
  modifiedBy: 'paltry',
  changeType: 'like about chops',
};

export const sampleWithFullData: ISubmissionAudit = {
  id: 9046,
  afm: '688098125',
  adt: 'access harvest license',
  lastName: 'Fisher',
  firstName: 'Jarrell',
  fatherName: 'abaft',
  acquisitionDate: dayjs('2025-03-17'),
  lossDate: dayjs('2025-03-17'),
  organizationUnit: 'furthermore and',
  newOrganizationUnit: 'hubris miskey triumphantly',
  protocolNumber: 'each',
  decisionDate: dayjs('2025-03-17'),
  previousSubmission: false,
  modifiedDate: dayjs('2025-03-18T03:39'),
  modifiedBy: 'gleefully handover hospitable',
  changeType: 'blend',
};

export const sampleWithNewData: NewSubmissionAudit = {
  afm: '830929002',
  adt: 'as motor archive',
  lastName: 'Predovic',
  firstName: 'Derek',
  fatherName: 'psst anti',
  acquisitionDate: dayjs('2025-03-18'),
  organizationUnit: 'ethyl',
  protocolNumber: 'closely',
  decisionDate: dayjs('2025-03-18'),
  previousSubmission: true,
  modifiedDate: dayjs('2025-03-17T15:15'),
  modifiedBy: 'irritably psst nougat',
  changeType: 'kowtow from strong',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
