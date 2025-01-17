import { IGrade, NewGrade } from './grade.model';

export const sampleWithRequiredData: IGrade = {
  id: 29029,
  name: 'trustworthy',
};

export const sampleWithPartialData: IGrade = {
  id: 29141,
  name: 'pish or down',
};

export const sampleWithFullData: IGrade = {
  id: 3895,
  name: 'jungle',
};

export const sampleWithNewData: NewGrade = {
  name: 'ew median',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
