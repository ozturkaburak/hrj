import dayjs from 'dayjs';
import { ICompany } from 'app/shared/model/company.model';
import { JobStatus } from 'app/shared/model/enumerations/job-status.model';

export interface IJobPosting {
  id?: number;
  title?: string;
  description?: string;
  status?: keyof typeof JobStatus | null;
  createdDate?: dayjs.Dayjs;
  expireDate?: dayjs.Dayjs | null;
  company?: ICompany | null;
}

export const defaultValue: Readonly<IJobPosting> = {};
