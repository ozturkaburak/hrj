import dayjs from 'dayjs';
import { IAssignment } from 'app/shared/model/assignment.model';
import { QuestionType } from 'app/shared/model/enumerations/question-type.model';

export interface IQuestion {
  id?: number;
  content?: string;
  options?: string | null;
  type?: keyof typeof QuestionType;
  correctAnswer?: string | null;
  createdAt?: dayjs.Dayjs;
  updatedAt?: dayjs.Dayjs | null;
  deletedAt?: dayjs.Dayjs | null;
  assignments?: IAssignment[] | null;
}

export const defaultValue: Readonly<IQuestion> = {};
