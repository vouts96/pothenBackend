import { IAuthority, NewAuthority } from './authority.model';

export const sampleWithRequiredData: IAuthority = {
  name: 'cd70e8fe-7e9c-468a-bbb4-df91db04aa3d',
};

export const sampleWithPartialData: IAuthority = {
  name: '02e70e0b-608d-4c00-a6f9-ef8a3f6a0c19',
};

export const sampleWithFullData: IAuthority = {
  name: '886a4f73-6abe-4751-83f7-7982d9aaecbf',
};

export const sampleWithNewData: NewAuthority = {
  name: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
