import dayjs from 'dayjs';
import { ICompany } from 'app/shared/model/company.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { WorkType } from 'app/shared/model/enumerations/work-type.model';
import { ContractType } from 'app/shared/model/enumerations/contract-type.model';

export interface IExperience {
  id?: number;
  title?: string;
  workType?: keyof typeof WorkType;
  contractType?: keyof typeof ContractType;
  startDate?: dayjs.Dayjs | null;
  endDate?: dayjs.Dayjs | null;
  description?: string | null;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  company?: ICompany | null;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IExperience> = {};
