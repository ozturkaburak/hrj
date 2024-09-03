import dayjs from 'dayjs';
import { IQuestion } from 'app/shared/model/question.model';
import { AssignmentType } from 'app/shared/model/enumerations/assignment-type.model';

export interface IAssignment {
  id?: number;
  type?: keyof typeof AssignmentType;
  visible?: boolean;
  hashtags?: string | null;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  questions?: IQuestion[] | null;
}

export const defaultValue: Readonly<IAssignment> = {
  visible: false,
};
