import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ICity } from 'app/shared/model/city.model';

export interface IContact {
  id?: number;
  secondaryEmail?: string | null;
  phoneNumber?: string | null;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  user?: IUser | null;
  city?: ICity | null;
}

export const defaultValue: Readonly<IContact> = {};
