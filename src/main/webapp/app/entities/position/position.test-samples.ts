import { IPosition, NewPosition } from './position.model';

export const sampleWithRequiredData: IPosition = {
  id: 11010,
  name: 'atomize how only',
};

export const sampleWithPartialData: IPosition = {
  id: 22192,
  name: 'hm',
};

export const sampleWithFullData: IPosition = {
  id: 16975,
  name: 'loaf unless',
};

export const sampleWithNewData: NewPosition = {
  name: 'oh connect',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
