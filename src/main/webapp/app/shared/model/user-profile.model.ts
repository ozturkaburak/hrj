import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IUserProfile {
  id?: number;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IUserProfile> = {};
