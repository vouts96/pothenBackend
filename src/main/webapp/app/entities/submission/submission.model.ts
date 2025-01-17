import dayjs from 'dayjs/esm';
import { IPosition } from 'app/entities/position/position.model';
import { IGrade } from 'app/entities/grade/grade.model';
import { ICommittee } from 'app/entities/committee/committee.model';
import { IUser } from 'app/entities/user/user.model';

export interface ISubmission {
  id: number;
  afm?: string | null;
  adt?: string | null;
  lastName?: string | null;
  firstName?: string | null;
  fatherName?: string | null;
  acquisitionDate?: dayjs.Dayjs | null;
  lossDate?: dayjs.Dayjs | null;
  organizationUnit?: string | null;
  newOrganizationUnit?: string | null;
  protocolNumber?: string | null;
  decisionDate?: dayjs.Dayjs | null;
  previousSubmission?: boolean | null;
  position?: Pick<IPosition, 'id' | 'name'> | null;
  grade?: Pick<IGrade, 'id' | 'name'> | null;
  committeeName?: Pick<ICommittee, 'id' | 'name'> | null;
  user?: Pick<IUser, 'id' | 'login'> | null;
}

export type NewSubmission = Omit<ISubmission, 'id'> & { id: null };
