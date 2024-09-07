import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IAssignment } from 'app/shared/model/assignment.model';
import { UserAssignmentStatus } from 'app/shared/model/enumerations/user-assignment-status.model';

export interface IUserAssignment {
  id?: number;
  orderOfQuestions?: string;
  totalDurationInMins?: number | null;
  accessUrl?: string;
  accessExpiryDate?: dayjs.Dayjs | null;
  userAssignmentStatus?: keyof typeof UserAssignmentStatus;
  assignedAt?: dayjs.Dayjs;
  joinedAt?: dayjs.Dayjs | null;
  finishedAt?: dayjs.Dayjs | null;
  user?: IUser | null;
  assignment?: IAssignment | null;
}

export const defaultValue: Readonly<IUserAssignment> = {};
