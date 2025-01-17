import { ICommittee, NewCommittee } from './committee.model';

export const sampleWithRequiredData: ICommittee = {
  id: 20427,
  name: 'pish',
};

export const sampleWithPartialData: ICommittee = {
  id: 30543,
  name: 'breed cleverly cheerfully',
};

export const sampleWithFullData: ICommittee = {
  id: 9983,
  name: 'mask alongside',
};

export const sampleWithNewData: NewCommittee = {
  name: 'justly',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
