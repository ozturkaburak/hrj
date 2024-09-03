import dayjs from 'dayjs';
import { ICountry } from 'app/shared/model/country.model';

export interface ICity {
  id?: number;
  name?: string;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  country?: ICountry | null;
}

export const defaultValue: Readonly<ICity> = {};
