import dayjs from 'dayjs';
import { IQuestion } from 'app/shared/model/question.model';
import { IUser } from 'app/shared/model/user.model';

export interface IAnswer {
  id?: number;
  content?: string;
  answeredAt?: dayjs.Dayjs;
  question?: IQuestion | null;
  user?: IUser | null;
}

export const defaultValue: Readonly<IAnswer> = {};
