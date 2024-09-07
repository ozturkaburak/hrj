import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { FileType } from 'app/shared/model/enumerations/file-type.model';
import { FileExtention } from 'app/shared/model/enumerations/file-extention.model';

export interface IUpload {
  id?: number;
  url?: string;
  type?: keyof typeof FileType;
  extension?: keyof typeof FileExtention;
  uploadDate?: dayjs.Dayjs;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IUpload> = {};
