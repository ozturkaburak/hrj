import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { EducationLevel } from 'app/shared/model/enumerations/education-level.model';

export interface IEducation {
  id?: number;
  name?: string;
  faculty?: string | null;
  level?: keyof typeof EducationLevel | null;
  degree?: string | null;
  startDate?: dayjs.Dayjs;
  endDate?: dayjs.Dayjs | null;
  activities?: string | null;
  clubs?: string | null;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IEducation> = {};
