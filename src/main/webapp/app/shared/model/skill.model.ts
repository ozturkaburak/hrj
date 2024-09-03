import dayjs from 'dayjs';
import { IExperience } from 'app/shared/model/experience.model';

export interface ISkill {
  id?: number;
  name?: string;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  experience?: IExperience | null;
}

export const defaultValue: Readonly<ISkill> = {};
