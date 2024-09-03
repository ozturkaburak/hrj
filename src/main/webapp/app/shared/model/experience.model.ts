import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { WorkType } from 'app/shared/model/enumerations/work-type.model';
import { ContractType } from 'app/shared/model/enumerations/contract-type.model';

export interface IExperience {
  id?: number;
  title?: string;
  companyName?: string;
  workType?: keyof typeof WorkType;
  contractType?: keyof typeof ContractType;
  officeLocation?: string | null;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs | null;
  description?: string | null;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IExperience> = {};
