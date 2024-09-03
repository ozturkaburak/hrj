import dayjs from 'dayjs';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { SocialMediaType } from 'app/shared/model/enumerations/social-media-type.model';

export interface IAboutMe {
  id?: number;
  socialMedia?: keyof typeof SocialMediaType | null;
  url?: string;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  userProfile?: IUserProfile | null;
}

export const defaultValue: Readonly<IAboutMe> = {};
