import { IUser } from './user.model';

export const sampleWithRequiredData: IUser = {
  id: 23494,
  login: 'gZ',
};

export const sampleWithPartialData: IUser = {
  id: 7883,
  login: 'moNk|@mgK6\\kbU1\\`trfj\\oOIEhFS\\uyC\\xf',
};

export const sampleWithFullData: IUser = {
  id: 30673,
  login: 'pn~=.k@DSK14\\sMBWM\\Dij3Df\\Cy18Ltm\\M3IcJn',
};
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
