import { IRefrence } from 'app/shared/model/refrence.model';

export interface IBaseExtra {
  id?: number;
  name?: string | null;
  cost?: number | null;
  refrence?: IRefrence | null;
}

export const defaultValue: Readonly<IBaseExtra> = {};
