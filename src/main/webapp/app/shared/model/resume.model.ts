import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';

export interface IResume {
  id?: number;
  fileContentType?: string;
  file?: string;
  fileType?: string;
  uploadDate?: dayjs.Dayjs;
  user?: IUser | null;
}

export const defaultValue: Readonly<IResume> = {};
