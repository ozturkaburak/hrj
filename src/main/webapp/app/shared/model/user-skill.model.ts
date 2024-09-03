import dayjs from 'dayjs';
import { ISkill } from 'app/shared/model/skill.model';
import { IUser } from 'app/shared/model/user.model';
import { SkillLevel } from 'app/shared/model/enumerations/skill-level.model';

export interface IUserSkill {
  id?: number;
  year?: number | null;
  level?: keyof typeof SkillLevel;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  skill?: ISkill | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IUserSkill> = {};
