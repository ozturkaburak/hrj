import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { ILanguage } from 'app/shared/model/language.model';
import { LanguageLevel } from 'app/shared/model/enumerations/language-level.model';

export interface IUserLanguage {
  id?: number;
  level?: keyof typeof LanguageLevel;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  user?: IUser | null;
  language?: ILanguage | null;
}

export const defaultValue: Readonly<IUserLanguage> = {};
