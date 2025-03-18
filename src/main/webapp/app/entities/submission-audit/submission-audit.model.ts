import dayjs from 'dayjs/esm';
import { ISubmission } from 'app/entities/submission/submission.model';

export interface ISubmissionAudit {
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
  modifiedDate?: dayjs.Dayjs | null;
  modifiedBy?: string | null;
  changeType?: string | null;
  originalSubmission?: Pick<ISubmission, 'id'> | null;
}

export type NewSubmissionAudit = Omit<ISubmissionAudit, 'id'> & { id: null };
