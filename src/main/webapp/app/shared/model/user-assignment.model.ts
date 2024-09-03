import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IAssignment } from 'app/shared/model/assignment.model';

export interface IUserAssignment {
  id?: number;
  assignedAt?: dayjs.Dayjs;
  joinedAt?: dayjs.Dayjs | null;
  finishedAt?: dayjs.Dayjs | null;
  user?: IUser | null;
  assignment?: IAssignment | null;
}

export const defaultValue: Readonly<IUserAssignment> = {};
