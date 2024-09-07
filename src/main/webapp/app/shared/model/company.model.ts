import dayjs from 'dayjs';
import { ICity } from 'app/shared/model/city.model';

export interface ICompany {
  id?: number;
  name?: string;
  logo?: string;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  active?: boolean;
  city?: ICity | null;
}

export const defaultValue: Readonly<ICompany> = {
  active: false,
};
