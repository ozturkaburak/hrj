import dayjs from 'dayjs';
import { JobStatus } from 'app/shared/model/enumerations/job-status.model';

export interface IJobPosting {
  id?: number;
  title?: string;
  description?: string;
  location?: string | null;
  department?: string | null;
  status?: keyof typeof JobStatus | null;
  createdDate?: dayjs.Dayjs | null;
  expireDate?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<IJobPosting> = {};
