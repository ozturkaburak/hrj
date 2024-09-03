import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface IUpload {
  id?: number;
  fileContentType?: string;
  file?: string;
  fileType?: string;
  uploadDate?: dayjs.Dayjs;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IUpload> = {};
