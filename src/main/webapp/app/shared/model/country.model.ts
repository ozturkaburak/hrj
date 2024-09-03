import dayjs from 'dayjs';

export interface ICountry {
  id?: number;
  name?: string;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
}

export const defaultValue: Readonly<ICountry> = {};
